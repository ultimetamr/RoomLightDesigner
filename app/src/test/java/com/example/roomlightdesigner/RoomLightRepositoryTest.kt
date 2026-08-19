package com.example.roomlightdesigner

import com.example.roomlightdesigner.data.InMemoryLayoutStorage
import com.example.roomlightdesigner.content.isTrue3DOrb
import com.example.roomlightdesigner.domain.LightPreset
import com.example.roomlightdesigner.domain.LightOrb
import com.example.roomlightdesigner.domain.MAX_LIGHT_ORBS
import com.example.roomlightdesigner.domain.MAX_LIGHT_RADIUS_METERS
import com.example.roomlightdesigner.domain.MIN_LIGHT_RADIUS_METERS
import com.example.roomlightdesigner.domain.OrbPosition
import com.example.roomlightdesigner.domain.PersistedLayout
import com.example.roomlightdesigner.domain.RoomLightRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomLightRepositoryTest {
    @Test
    fun `sunset and moon use true 3D visuals`() {
        assertTrue(LightPreset.SUNSET.isTrue3DOrb())
        assertTrue(LightPreset.MOON.isTrue3DOrb())
        assertFalse(LightPreset.NEON.isTrue3DOrb())
    }

    @Test
    fun `capacity is hard limited to eight`() {
        val repository = RoomLightRepository(InMemoryLayoutStorage())
        repeat(MAX_LIGHT_ORBS) { index ->
            repository.choosePreset(if (index % 2 == 0) LightPreset.SUNSET else LightPreset.MOON)
            assertTrue(repository.placePending(OrbPosition(0f, 1f, -1f - index * 0.1f)))
        }

        repository.choosePreset(LightPreset.NEON)

        assertEquals(MAX_LIGHT_ORBS, repository.state.value.orbs.size)
        assertTrue(repository.state.value.atCapacity)
        assertFalse(repository.placePending(OrbPosition(0f, 1f, -2f)))
    }

    @Test
    fun `parameters and positions are clamped before persistence`() {
        val storage = InMemoryLayoutStorage()
        val repository = RoomLightRepository(storage)
        repository.choosePreset(LightPreset.STAGE)
        repository.placePending(OrbPosition(99f, -3f, 5f))
        repository.updateSelectedBrightness(4f)
        repository.updateSelectedRadius(99f)

        val orb = repository.state.value.selectedOrb!!
        assertEquals(1f, orb.brightness)
        assertEquals(MAX_LIGHT_RADIUS_METERS, orb.radiusMeters)
        assertEquals(50f, orb.position.x)
        assertEquals(-3f, orb.position.y)
        assertEquals(5f, orb.position.z)

        repository.updateSelectedBrightness(-1f)
        repository.updateSelectedRadius(-4f)
        assertEquals(0f, repository.state.value.selectedOrb!!.brightness)
        assertEquals(MIN_LIGHT_RADIUS_METERS, repository.state.value.selectedOrb!!.radiusMeters)
        assertEquals(repository.state.value.orbs, storage.value!!.orbs)
    }

    @Test
    fun `locked layout rejects selection movement deletion and clear`() {
        val repository = RoomLightRepository(InMemoryLayoutStorage())
        repository.choosePreset(LightPreset.NEON)
        repository.placePending(OrbPosition(0f, 1f, -2f))
        val original = repository.state.value.orbs.single()

        repository.setLayoutLocked(true)
        repository.selectOrb(original.id)
        repository.moveOrbBy(original.id, 1f, 1f, 1f)
        repository.deleteOrb(original.id)
        repository.requestClear()

        assertNull(repository.state.value.selectedOrbId)
        assertEquals(listOf(original), repository.state.value.orbs)
        assertFalse(repository.state.value.clearConfirmationVisible)
    }

    @Test
    fun `latest layout restores automatically`() {
        val storage = InMemoryLayoutStorage()
        val first = RoomLightRepository(storage)
        first.choosePreset(LightPreset.MOON)
        first.placePending(OrbPosition(0.6f, 1.4f, -2.2f))
        first.setLayoutLocked(true)

        val restored = RoomLightRepository(storage)

        assertEquals(1, restored.state.value.orbs.size)
        assertEquals(LightPreset.MOON, restored.state.value.orbs.single().preset)
        assertTrue(restored.state.value.layoutLocked)
    }

    @Test
    fun `first usable head pose seeds sunset and moon once`() {
        val storage = InMemoryLayoutStorage()
        val repository = RoomLightRepository(storage)
        val positions = listOf(OrbPosition(-0.5f, 1.6f, -1.5f), OrbPosition(0.5f, 1.7f, -1.6f))

        assertTrue(repository.seedStarterLights(positions))
        assertEquals(listOf(LightPreset.SUNSET, LightPreset.MOON), repository.state.value.orbs.map { it.preset })
        assertTrue(storage.value!!.starterContentInitialized)
        assertFalse(repository.seedStarterLights(positions))
        assertEquals(2, repository.state.value.orbs.size)
    }

    @Test
    fun `cleared starter lights stay empty after restart`() {
        val storage = InMemoryLayoutStorage()
        val repository = RoomLightRepository(storage)
        val positions = listOf(OrbPosition(-0.5f, 1.6f, -1.5f), OrbPosition(0.5f, 1.7f, -1.6f))
        repository.seedStarterLights(positions)
        repository.requestClear()
        repository.confirmClear()

        val restored = RoomLightRepository(storage)

        assertTrue(restored.state.value.orbs.isEmpty())
        assertFalse(restored.seedStarterLights(positions))
    }

    @Test
    fun `schema three ground lights migrate to current eye-relative positions`() {
        val preset = LightPreset.SUNSET
        val storage = InMemoryLayoutStorage(
            PersistedLayout(
                schemaVersion = 3,
                orbs = listOf(
                    LightOrb(
                        preset = preset,
                        colorArgb = preset.defaultColor.value,
                        brightness = preset.defaultBrightness,
                        radiusMeters = preset.defaultRadius,
                        position = OrbPosition(-0.48f, 0f, -1.55f),
                    ),
                ),
                starterContentInitialized = true,
            ),
        )
        val repository = RoomLightRepository(storage)

        repository.reanchorLegacyLayout(listOf(OrbPosition(-0.48f, 1.62f, -1.55f)))

        assertEquals(1.62f, repository.state.value.orbs.single().position.y)
        assertEquals(4, storage.value!!.schemaVersion)
    }
}
