package com.example.roomlightdesigner.domain

import androidx.compose.ui.graphics.Color
import com.example.roomlightdesigner.data.LayoutStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RoomLightRepository(private val storage: LayoutStorage) {
    private val restored = storage.load()
    private var legacyLayoutNeedsReanchor = restored?.let { layout ->
        layout.schemaVersion == 1 ||
            (layout.schemaVersion < 4 && layout.orbs.any { it.position.y < MIN_VISIBLE_ORB_HEIGHT_METERS })
    } ?: false
    private var starterContentInitialized = restored?.starterContentInitialized
        ?: restored?.orbs?.isNotEmpty()
        ?: false
    private val mutableState = MutableStateFlow(
        RoomLightState(
            orbs = restored?.orbs.orEmpty().take(MAX_LIGHT_ORBS).map(LightOrb::sanitized),
            layoutLocked = restored?.layoutLocked ?: false,
        ),
    )
    val state: StateFlow<RoomLightState> = mutableState.asStateFlow()

    fun choosePreset(preset: LightPreset) {
        mutableState.update { current ->
            if (current.layoutLocked) current.copy(notice = "布局已锁定，请先解锁")
            else if (current.atCapacity) current.copy(notice = "最多放置 8 个光球")
            else current.copy(pendingPreset = preset, selectedOrbId = null, notice = "射线指向空间并扣动扳机放置")
        }
    }

    fun setCustomColor(color: Color) {
        mutableState.update { it.copy(customColorArgb = color.value, pendingPreset = LightPreset.CUSTOM) }
    }

    fun placePending(position: OrbPosition): Boolean {
        var placed = false
        mutableState.update { current ->
            val preset = current.pendingPreset
            if (current.layoutLocked || current.atCapacity || preset == null) {
                current.copy(notice = if (current.atCapacity) "最多放置 8 个光球" else "当前无法放置")
            } else {
                val color = if (preset == LightPreset.CUSTOM) current.customColorArgb else preset.defaultColor.value
                val orb = LightOrb(
                    preset = preset,
                    colorArgb = color,
                    brightness = preset.defaultBrightness,
                    radiusMeters = preset.defaultRadius,
                    position = position.clamped(),
                )
                placed = true
                current.copy(
                    orbs = current.orbs + orb,
                    selectedOrbId = orb.id,
                    pendingPreset = null,
                    notice = "已放置 ${preset.displayName}光球",
                )
            }
        }
        persist()
        return placed
    }

    fun selectOrb(id: String?) {
        mutableState.update { current ->
            if (current.layoutLocked) current.copy(notice = "布局锁定中，光球不可选中")
            else current.copy(selectedOrbId = id?.takeIf { candidate -> current.orbs.any { it.id == candidate } })
        }
    }

    fun moveOrbBy(id: String, dxMeters: Float, dyMeters: Float, dzMeters: Float) {
        updateOrb(id) { orb ->
            orb.copy(position = OrbPosition(
                orb.position.x + dxMeters,
                orb.position.y + dyMeters,
                orb.position.z + dzMeters,
            ).clamped())
        }
    }

    fun updateSelectedColor(color: Color) = updateSelected { it.copy(colorArgb = color.value) }
    fun updateSelectedBrightness(value: Float) = updateSelected { it.copy(brightness = value.coerceIn(0f, 1f)) }
    fun updateSelectedRadius(value: Float) = updateSelected {
        it.copy(radiusMeters = value.coerceIn(MIN_LIGHT_RADIUS_METERS, MAX_LIGHT_RADIUS_METERS))
    }

    fun deleteOrb(id: String) {
        mutableState.update { current ->
            if (current.layoutLocked) current.copy(notice = "布局锁定中，无法删除")
            else current.copy(
                orbs = current.orbs.filterNot { it.id == id },
                selectedOrbId = current.selectedOrbId.takeUnless { it == id },
                notice = "光球已删除",
            )
        }
        persist()
    }

    fun requestClear() {
        mutableState.update { current ->
            if (current.layoutLocked) current.copy(notice = "布局锁定中，无法清空")
            else if (current.orbs.isEmpty()) current.copy(notice = "当前没有光球")
            else current.copy(clearConfirmationVisible = true)
        }
    }

    fun cancelClear() = mutableState.update { it.copy(clearConfirmationVisible = false) }

    fun confirmClear() {
        mutableState.update { current ->
            if (current.layoutLocked) current.copy(clearConfirmationVisible = false, notice = "布局锁定中，无法清空")
            else current.copy(orbs = emptyList(), selectedOrbId = null, clearConfirmationVisible = false, notice = "已清空全部光球")
        }
        // An intentional empty layout is still a valid saved layout. Persist
        // the starter marker so a later restart does not recreate demo lights.
        starterContentInitialized = true
        persist()
    }

    fun setLayoutLocked(locked: Boolean) {
        mutableState.update { it.copy(layoutLocked = locked, selectedOrbId = null, pendingPreset = null, notice = if (locked) "布局已锁定" else "布局已解锁") }
        persist()
    }

    fun togglePanel() = mutableState.update { it.copy(panelVisible = !it.panelVisible) }

    /**
     * Schema 1 saved coordinates inside a fixed launch-origin box. Rebase that
     * one legacy layout once the first valid HMD pose is available, preserving
     * every light's identity and parameters.
     */
    fun reanchorLegacyLayout(positions: List<OrbPosition>) {
        if (!legacyLayoutNeedsReanchor) return
        mutableState.update { current ->
            current.copy(
                orbs = current.orbs.mapIndexed { index, orb ->
                    orb.copy(position = positions.getOrNull(index)?.clamped() ?: orb.position)
                },
                notice = if (current.orbs.isEmpty()) current.notice else "已将旧版光球移回当前视野",
            )
        }
        legacyLayoutNeedsReanchor = false
        persist()
    }

    /**
     * Seeds one Sunset and one Moon on the first usable HMD pose only. This
     * makes the true 3D content visible immediately on a physical headset,
     * while the persisted marker ensures an explicit Clear remains empty.
     */
    fun seedStarterLights(positions: List<OrbPosition>): Boolean {
        if (starterContentInitialized || positions.size < 2) return false
        starterContentInitialized = true
        mutableState.update { current ->
            if (current.orbs.isNotEmpty()) current
            else {
                val presets = listOf(LightPreset.SUNSET, LightPreset.MOON)
                val starterOrbs = presets.mapIndexed { index, preset ->
                    LightOrb(
                        preset = preset,
                        colorArgb = preset.defaultColor.value,
                        brightness = preset.defaultBrightness,
                        radiusMeters = preset.defaultRadius,
                        position = positions[index].clamped(),
                    )
                }
                current.copy(
                    orbs = starterOrbs,
                    selectedOrbId = null,
                    notice = "已在视线前方生成日落与月光 3D 光球",
                )
            }
        }
        persist()
        return true
    }

    fun openPhotoSetup() = mutableState.update { it.copy(photoPhase = PhotoPhase.SETUP, selectedOrbId = null, photoResultMessage = null) }
    fun cancelPhotoSetup() = mutableState.update { it.copy(photoPhase = PhotoPhase.OFF, photoResultMessage = null) }
    fun setPhotoFrame(frame: PhotoFrame) = mutableState.update { it.copy(photoFrame = frame) }
    fun setPhotoCountdown(seconds: Int) = mutableState.update { it.copy(photoCountdownSeconds = seconds.coerceIn(3, 10)) }
    fun beginPhotoMode() = mutableState.update {
        it.copy(photoPhase = PhotoPhase.CLEAN_PREROLL, panelVisible = false, photoCountdownRemaining = it.photoCountdownSeconds, photoResultMessage = null)
    }
    fun beginCountdown() = mutableState.update { it.copy(photoPhase = PhotoPhase.COUNTDOWN) }
    fun tickCountdown() = mutableState.update { current -> current.copy(photoCountdownRemaining = (current.photoCountdownRemaining - 1).coerceAtLeast(0)) }
    fun beginCapture() = mutableState.update { it.copy(photoPhase = PhotoPhase.CAPTURING) }
    fun finishCapture(message: String) = mutableState.update { it.copy(photoPhase = PhotoPhase.RESULT, photoResultMessage = message) }
    fun exitPhotoMode() = mutableState.update { it.copy(photoPhase = PhotoPhase.OFF, panelVisible = true, photoCountdownRemaining = 0, photoResultMessage = null) }
    fun clearNotice() = mutableState.update { it.copy(notice = null) }

    private fun updateSelected(transform: (LightOrb) -> LightOrb) {
        val selected = mutableState.value.selectedOrbId ?: return
        updateOrb(selected, transform)
    }

    private fun updateOrb(id: String, transform: (LightOrb) -> LightOrb) {
        mutableState.update { current ->
            if (current.layoutLocked) current.copy(notice = "布局锁定中，参数不可修改")
            else current.copy(orbs = current.orbs.map { if (it.id == id) transform(it).sanitized() else it })
        }
        persist()
    }

    private fun persist() {
        val current = mutableState.value
        storage.save(
            PersistedLayout(
                orbs = current.orbs.map(LightOrb::sanitized),
                layoutLocked = current.layoutLocked,
                starterContentInitialized = starterContentInitialized,
            ),
        )
    }
}

private const val MIN_VISIBLE_ORB_HEIGHT_METERS = 0.75f
