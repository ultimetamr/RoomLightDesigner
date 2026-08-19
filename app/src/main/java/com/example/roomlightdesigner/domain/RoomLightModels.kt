package com.example.roomlightdesigner.domain

import androidx.compose.ui.graphics.Color
import java.util.UUID

const val MAX_LIGHT_ORBS = 8
const val MIN_LIGHT_RADIUS_METERS = 0.5f
const val MAX_LIGHT_RADIUS_METERS = 5.0f

enum class LightPreset(
    val displayName: String,
    val symbol: String,
    val subtitle: String,
    val defaultColor: Color,
    val defaultBrightness: Float,
    val defaultRadius: Float,
) {
    SUNSET("日落", "☀", "暖橙柔光 · 暖雾", Color(0xFFFF8A4C), 0.72f, 2.4f), // design-style: fixed-figma-color Visual@4 sunset identity
    MOON("月光", "☾", "冷蓝散光 · 星尘", Color(0xFF7CB8FF), 0.56f, 3.2f), // design-style: fixed-figma-color Visual@4 moon identity
    NEON("霓虹", "◇", "粉紫辉光 · 动态扫描", Color(0xFFFF4FD8), 0.82f, 1.8f), // design-style: fixed-figma-color Visual@4 neon identity
    STAGE("舞台", "✦", "白色聚光 · 清晰边缘", Color(0xFFF6F8FF), 0.94f, 2.8f), // design-style: fixed-figma-color Visual@4 stage identity
    CUSTOM("自定义", "●", "个人色彩 · 柔和光晕", Color(0xFF55E6C1), 0.70f, 2.0f), // design-style: fixed-figma-color Visual@4 custom default
}

enum class PhotoFrame(val displayName: String) {
    WHITE("简约白边"),
    FILM("胶片黑边"),
    INSTANT("拍立得质感"),
}

enum class PhotoPhase { OFF, SETUP, CLEAN_PREROLL, COUNTDOWN, CAPTURING, RESULT }

data class OrbPosition(val x: Float, val y: Float, val z: Float) {
    fun clamped(): OrbPosition = OrbPosition(
        // These are world coordinates, not offsets from the app's launch
        // origin. A small, negative-Z-only box makes valid placements vanish
        // on a physical device when the Stage origin is elsewhere.
        x = x.finiteOr(0f).coerceIn(-WORLD_COORDINATE_LIMIT_METERS, WORLD_COORDINATE_LIMIT_METERS),
        y = y.finiteOr(1.35f).coerceIn(-WORLD_HEIGHT_LIMIT_METERS, WORLD_HEIGHT_LIMIT_METERS),
        z = z.finiteOr(-1.8f).coerceIn(-WORLD_COORDINATE_LIMIT_METERS, WORLD_COORDINATE_LIMIT_METERS),
    )
}

private fun Float.finiteOr(fallback: Float): Float = if (isFinite()) this else fallback

data class LightOrb(
    val id: String = UUID.randomUUID().toString(),
    val preset: LightPreset,
    val colorArgb: ULong,
    val brightness: Float,
    val radiusMeters: Float,
    val position: OrbPosition,
) {
    val color: Color get() = Color(colorArgb)

    fun sanitized(): LightOrb = copy(
        brightness = brightness.coerceIn(0f, 1f),
        radiusMeters = radiusMeters.coerceIn(MIN_LIGHT_RADIUS_METERS, MAX_LIGHT_RADIUS_METERS),
        position = position.clamped(),
    )
}

data class PersistedLayout(
    val schemaVersion: Int = 4,
    val orbs: List<LightOrb> = emptyList(),
    val layoutLocked: Boolean = false,
    val starterContentInitialized: Boolean = false,
)

data class RoomLightState(
    val orbs: List<LightOrb> = emptyList(),
    val selectedOrbId: String? = null,
    val pendingPreset: LightPreset? = null,
    val customColorArgb: ULong = LightPreset.CUSTOM.defaultColor.value,
    val layoutLocked: Boolean = false,
    val panelVisible: Boolean = true,
    val clearConfirmationVisible: Boolean = false,
    val photoPhase: PhotoPhase = PhotoPhase.OFF,
    val photoFrame: PhotoFrame = PhotoFrame.WHITE,
    val photoCountdownSeconds: Int = 3,
    val photoCountdownRemaining: Int = 0,
    val photoResultMessage: String? = null,
    val notice: String? = null,
) {
    val selectedOrb: LightOrb? get() = orbs.firstOrNull { it.id == selectedOrbId }
    val atCapacity: Boolean get() = orbs.size >= MAX_LIGHT_ORBS
    val inPhotoMode: Boolean get() = photoPhase !in setOf(PhotoPhase.OFF, PhotoPhase.SETUP)
}

private const val WORLD_COORDINATE_LIMIT_METERS = 50f
private const val WORLD_HEIGHT_LIMIT_METERS = 10f
