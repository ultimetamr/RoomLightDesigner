package com.example.roomlightdesigner.domain

import com.pico.spatial.core.math.Quat
import com.pico.spatial.core.math.Vector3
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

data class HeadPose(val position: Vector3, val rotation: Quat)

/**
 * Stage world space is floor-origin, but some physical-device tracking builds
 * initially report an HMD-local Y of zero. Resolve only implausible heights;
 * valid standing and seated Stage heights pass through unchanged.
 */
fun HeadPose.withResolvedStageEyeHeight(): HeadPose {
    if (position.y in MIN_PLAUSIBLE_EYE_HEIGHT_METERS..MAX_PLAUSIBLE_EYE_HEIGHT_METERS) return this
    return copy(position = Vector3(position.x, DEFAULT_STAGE_EYE_HEIGHT_METERS, position.z))
}

data class PanelPose(val position: Vector3, val rotation: Quat)

/** A conservative horizontal footprint derived from a sensed spatial plane. */
data class HorizontalObstacle(
    val center: Vector3,
    val halfWidthMeters: Float,
    val halfHeightMeters: Float,
)

/**
 * Keeps the operation panel world-locked between explicit auto-reset events.
 * HMD samples are read continuously, but no panel transform is written until
 * one of the documented reset thresholds is exceeded.
 */
class PanelPlacementController(
    private val nominalDistanceMeters: Float = 1.0f,
    private val distanceDeviationThresholdMeters: Float = 0.8f,
    private val angleThresholdDegrees: Float = 90f,
    private val resetDurationNanos: Long = 300_000_000L,
) {
    private var currentPose: PanelPose? = null
    private var animation: ResetAnimation? = null

    fun update(
        head: HeadPose,
        obstacles: Collection<HorizontalObstacle>,
        nowNanos: Long,
    ): PanelPose {
        val existing = currentPose
        if (existing == null) {
            return targetPose(head, obstacles).also { currentPose = it }
        }

        animation?.let { active ->
            val progress = ((nowNanos - active.startedAtNanos).toFloat() / resetDurationNanos)
                .coerceIn(0f, 1f)
            val animated = PanelPose(
                position = Vector3.lerp(active.from.position, active.to.position, progress),
                rotation = Quat.slerp(active.from.rotation, active.to.rotation, progress),
            )
            currentPose = animated
            if (progress >= 1f) animation = null
            return animated
        }

        if (requiresReset(existing, head)) {
            val reset = ResetAnimation(existing, targetPose(head, obstacles), nowNanos)
            animation = reset
            return reset.from
        }

        return existing
    }

    fun current(): PanelPose? = currentPose

    /** Opening the panel is an explicit placement event, so it must sample the
     * current head pose instead of reviving the previous hidden world pose. */
    fun reset() {
        currentPose = null
        animation = null
    }

    internal fun requiresReset(panel: PanelPose, head: HeadPose): Boolean {
        val gaze = horizontalForward(head.rotation)
        val toPanel = horizontalDirection(head.position, panel.position)
        val horizontalDistance = horizontalDistance(head.position, panel.position)
        val distanceOutsideTolerance = abs(horizontalDistance - nominalDistanceMeters) >
            distanceDeviationThresholdMeters

        if (toPanel == null) return true
        val cosine = Vector3.dot(gaze, toPanel).coerceIn(-1f, 1f)
        val angleDegrees = Math.toDegrees(acos(cosine).toDouble()).toFloat()
        return angleDegrees > angleThresholdDegrees || distanceOutsideTolerance
    }

    private fun targetPose(
        head: HeadPose,
        obstacles: Collection<HorizontalObstacle>,
    ): PanelPose {
        val forward = horizontalForward(head.rotation)
        val distance = obstacleAwareDistance(head.position, forward, obstacles)
        val position = Vector3(
            head.position.x + forward.x * distance,
            head.position.y,
            head.position.z + forward.z * distance,
        )
        return PanelPose(position, faceViewerRotation(forward))
    }

    private fun obstacleAwareDistance(
        headPosition: Vector3,
        forward: Vector3,
        obstacles: Collection<HorizontalObstacle>,
    ): Float {
        var resolvedDistance = nominalDistanceMeters
        obstacles.forEach { obstacle ->
            val dx = obstacle.center.x - headPosition.x
            val dz = obstacle.center.z - headPosition.z
            val along = dx * forward.x + dz * forward.z
            if (along <= MIN_PANEL_DISTANCE_METERS || along > nominalDistanceMeters + OBSTACLE_CLEARANCE_METERS) {
                return@forEach
            }

            val lateral = abs(dx * forward.z - dz * forward.x)
            val overlapsWidth = lateral <= obstacle.halfWidthMeters + PANEL_HALF_WIDTH_METERS
            val overlapsHeight = abs(obstacle.center.y - headPosition.y) <=
                obstacle.halfHeightMeters + PANEL_HALF_HEIGHT_METERS
            if (overlapsWidth && overlapsHeight) {
                resolvedDistance = minOf(
                    resolvedDistance,
                    max(MIN_PANEL_DISTANCE_METERS, along - OBSTACLE_CLEARANCE_METERS),
                )
            }
        }
        return resolvedDistance
    }

    private data class ResetAnimation(
        val from: PanelPose,
        val to: PanelPose,
        val startedAtNanos: Long,
    )

    private companion object {
        const val OBSTACLE_CLEARANCE_METERS = 0.20f
        const val MIN_PANEL_DISTANCE_METERS = 0.35f
        const val PANEL_HALF_WIDTH_METERS = 0.34f
        const val PANEL_HALF_HEIGHT_METERS = 0.24f
    }
}

internal fun horizontalForward(rotation: Quat): Vector3 {
    // Stage +Z points back toward the user; headset gaze is local -Z.
    val raw = rotation.rotateVector(Vector3.BACK)
    val length = sqrt(raw.x * raw.x + raw.z * raw.z)
    return if (length < 0.0001f) Vector3.BACK else Vector3(raw.x / length, 0f, raw.z / length)
}

/** Places new lights in a compact arc around the current gaze, always in view. */
fun headRelativeOrbPosition(head: HeadPose, index: Int): OrbPosition {
    val forward = horizontalForward(head.rotation)
    val right = Vector3(-forward.z, 0f, forward.x)
    val slots = arrayOf(
        Triple(-0.48f, 0.00f, 1.55f),
        Triple(0.48f, 0.12f, 1.65f),
        Triple(-0.78f, 0.34f, 1.90f),
        Triple(0.78f, -0.20f, 1.95f),
        Triple(0.00f, 0.50f, 2.10f),
        Triple(-1.02f, -0.36f, 2.20f),
        Triple(1.02f, 0.30f, 2.25f),
        Triple(0.00f, -0.50f, 2.30f),
    )
    val (lateral, vertical, distance) = slots[index.coerceIn(0, slots.lastIndex)]
    return OrbPosition(
        x = head.position.x + forward.x * distance + right.x * lateral,
        y = head.position.y + vertical,
        z = head.position.z + forward.z * distance + right.z * lateral,
    ).clamped()
}

private fun horizontalDirection(from: Vector3, to: Vector3): Vector3? {
    val dx = to.x - from.x
    val dz = to.z - from.z
    val length = sqrt(dx * dx + dz * dz)
    return if (length < 0.0001f) null else Vector3(dx / length, 0f, dz / length)
}

private fun horizontalDistance(a: Vector3, b: Vector3): Float {
    val dx = a.x - b.x
    val dz = a.z - b.z
    return sqrt(dx * dx + dz * dz)
}

/** Default panel front is +Z, so rotate +Z opposite the viewer's gaze. */
private fun faceViewerRotation(horizontalForward: Vector3): Quat {
    val normalX = -horizontalForward.x
    val normalZ = -horizontalForward.z
    val yaw = atan2(normalX, normalZ)
    val halfYaw = yaw * 0.5f
    return Quat(0f, sin(halfYaw), 0f, cos(halfYaw)).normalize()
}

const val DEFAULT_STAGE_EYE_HEIGHT_METERS = 1.62f
private const val MIN_PLAUSIBLE_EYE_HEIGHT_METERS = 0.75f
private const val MAX_PLAUSIBLE_EYE_HEIGHT_METERS = 2.40f
