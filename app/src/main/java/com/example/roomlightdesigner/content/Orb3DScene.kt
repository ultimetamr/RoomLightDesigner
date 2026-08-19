package com.example.roomlightdesigner.content

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.roomlightdesigner.domain.LightOrb
import com.example.roomlightdesigner.domain.LightPreset
import com.pico.spatial.core.container.SpatialViewContent
import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.ModelEntity
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.ecs.resource.BlendingMode
import com.pico.spatial.core.ecs.resource.MeshResource
import com.pico.spatial.core.ecs.resource.PhysicallyBasedMaterial
import com.pico.spatial.core.ecs.resource.UnlitMaterial
import com.pico.spatial.core.math.Color4
import com.pico.spatial.core.math.Vector3

/**
 * Owns the true 3D representation of Sunset and Moon lights. The Compose
 * attachment at the same world position remains only as an interaction target,
 * so existing select/drag/long-press behavior is preserved.
 */
class Orb3DScene : AutoCloseable {
    private val visuals = mutableMapOf<String, Orb3DVisual>()
    private var coreMesh: MeshResource? = null
    private var haloMesh: MeshResource? = null
    private var particleMesh: MeshResource? = null

    fun sync(content: SpatialViewContent, orbs: List<LightOrb>) {
        val supported = orbs.filter { it.preset.isTrue3DOrb() }
        val activeIds = supported.mapTo(mutableSetOf(), LightOrb::id)

        visuals.keys.filterNot(activeIds::contains).toList().forEach { id ->
            visuals.remove(id)?.close()
        }

        supported.forEach { orb ->
            val visual = visuals[orb.id] ?: runCatching { createVisual(orb) }
                .onFailure { error -> Log.e(TAG, "Unable to create 3D orb ${orb.id}", error) }
                .getOrNull()
                ?.also {
                    visuals[orb.id] = it
                    content.addEntity(it.root)
                }
                ?: return@forEach
            runCatching { visual.update(orb) }
                .onFailure { error -> Log.e(TAG, "Unable to update 3D orb ${orb.id}", error) }
        }
    }

    private fun createVisual(orb: LightOrb): Orb3DVisual {
        val coreMaterial = PhysicallyBasedMaterial.create(BlendingMode.OPAQUE).apply {
            setRoughness(if (orb.preset == LightPreset.MOON) 0.58f else 0.30f)
            setMetallic(if (orb.preset == LightPreset.MOON) 0.08f else 0f)
        }
        val haloMaterial = UnlitMaterial.create(BlendingMode.ADD).apply {
            setDepthWrite(false)
            setDepthTest(true)
        }
        val particleMaterial = UnlitMaterial.create(BlendingMode.ADD).apply {
            setDepthWrite(false)
            setDepthTest(true)
        }

        // Entity names are optional. Physical PICO OS builds reject several
        // characters accepted by the emulator (notably UUID separators), so
        // leave SDK-generated names intact instead of risking scene creation.
        val root = Entity()
        val core = ModelEntity(coreMesh(), coreMaterial)
        val halo = ModelEntity(haloMesh(), haloMaterial)
        root.addChild(core)
        root.addChild(halo)

        val particles = particleOffsets(orb.preset).map { offset ->
            ModelEntity(particleMesh(), particleMaterial).apply {
                components[TransformComponent::class.java]?.apply {
                    position = offset
                    scaleVector = Vector3(if (orb.preset == LightPreset.MOON) 0.72f else 1.15f)
                }
                root.addChild(this)
            }
        }

        return Orb3DVisual(root, core, halo, particles, coreMaterial, haloMaterial, particleMaterial)
    }

    private fun coreMesh(): MeshResource = coreMesh ?: MeshResource.createSphere(CORE_RADIUS_METERS)
        .also { coreMesh = it }

    private fun haloMesh(): MeshResource = haloMesh ?: MeshResource.createSphere(HALO_RADIUS_METERS)
        .also { haloMesh = it }

    private fun particleMesh(): MeshResource = particleMesh ?: MeshResource.createSphere(PARTICLE_RADIUS_METERS)
        .also { particleMesh = it }

    override fun close() {
        visuals.values.forEach(Orb3DVisual::close)
        visuals.clear()
        coreMesh?.close()
        haloMesh?.close()
        particleMesh?.close()
        coreMesh = null
        haloMesh = null
        particleMesh = null
    }

    private class Orb3DVisual(
        val root: Entity,
        private val core: Entity,
        private val halo: Entity,
        private val particles: List<Entity>,
        private val coreMaterial: PhysicallyBasedMaterial,
        private val haloMaterial: UnlitMaterial,
        private val particleMaterial: UnlitMaterial,
    ) : AutoCloseable {
        fun update(orb: LightOrb) {
            root.components[TransformComponent::class.java]?.position = Vector3(
                orb.position.x,
                orb.position.y,
                orb.position.z,
            )

            val base = orb.color.toColor4()
            val brightness = orb.brightness.coerceIn(0f, 1f)
            coreMaterial.setBaseColor(base.scaled(0.72f + brightness * 0.28f, alpha = 1f))
            coreMaterial.setEmissiveColor(base.scaled(0.85f + brightness * 1.65f, alpha = 1f))
            haloMaterial.setBaseColor(base.scaled(1.15f, alpha = 0.16f + brightness * 0.18f))
            haloMaterial.setOpacity(0.18f + brightness * 0.20f)
            particleMaterial.setBaseColor(if (orb.preset == LightPreset.MOON) {
                Color4(0.80f, 0.90f, 1.0f, 0.88f)
            } else {
                base.scaled(1.10f, alpha = 0.58f)
            })
            particleMaterial.setOpacity(if (orb.preset == LightPreset.MOON) 0.86f else 0.48f)

            val pulseScale = 0.94f + brightness * 0.12f
            core.components[TransformComponent::class.java]?.scaleVector = Vector3(pulseScale)
            halo.components[TransformComponent::class.java]?.scaleVector = Vector3(0.92f + brightness * 0.20f)
            particles.forEach { it.enabled = brightness > 0.02f }
        }

        override fun close() {
            root.destroy()
            coreMaterial.close()
            haloMaterial.close()
            particleMaterial.close()
        }
    }

    private companion object {
        const val TAG = "Orb3DScene"
        const val CORE_RADIUS_METERS = 0.105f
        const val HALO_RADIUS_METERS = 0.148f
        const val PARTICLE_RADIUS_METERS = 0.012f
    }
}

internal fun LightPreset.isTrue3DOrb(): Boolean = this == LightPreset.SUNSET || this == LightPreset.MOON

private fun Color.toColor4(): Color4 = Color4(red, green, blue, alpha)

private fun Color4.scaled(multiplier: Float, alpha: Float): Color4 = Color4(
    red = red * multiplier,
    green = green * multiplier,
    blue = blue * multiplier,
    alpha = alpha,
)

private fun particleOffsets(preset: LightPreset): List<Vector3> {
    return if (preset == LightPreset.MOON) {
        listOf(
            Vector3(-0.18f, 0.10f, 0.03f), Vector3(0.16f, 0.14f, -0.05f),
            Vector3(-0.09f, -0.18f, 0.08f), Vector3(0.21f, -0.07f, 0.02f),
            Vector3(0.04f, 0.22f, 0.06f), Vector3(-0.22f, -0.03f, -0.04f),
            Vector3(0.11f, -0.22f, -0.06f), Vector3(-0.02f, 0.04f, 0.23f),
            Vector3(0.07f, 0.02f, -0.22f), Vector3(-0.15f, 0.18f, -0.02f),
        )
    } else {
        listOf(
            Vector3(-0.17f, 0.06f, 0.04f), Vector3(0.15f, 0.10f, -0.02f),
            Vector3(-0.10f, -0.14f, 0.07f), Vector3(0.19f, -0.05f, 0.03f),
            Vector3(0.02f, 0.18f, 0.08f), Vector3(-0.20f, -0.02f, -0.05f),
            Vector3(0.08f, -0.19f, -0.07f), Vector3(-0.03f, 0.03f, 0.20f),
        )
    }
}
