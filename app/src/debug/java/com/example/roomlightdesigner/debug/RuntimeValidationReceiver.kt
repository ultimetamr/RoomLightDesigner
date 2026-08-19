package com.example.roomlightdesigner.debug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.example.roomlightdesigner.domain.LightPreset
import com.example.roomlightdesigner.domain.OrbPosition
import com.example.roomlightdesigner.domain.PhotoFrame
import com.example.roomlightdesigner.platform.RoomLightService

/**
 * Debug-only deterministic driver used by emulator smoke tests.
 *
 * PICO's CLI intentionally cannot inject controller-ray hits into volumetric
 * windows. This receiver exercises the exact same repository state transitions
 * without adding a back door to release builds.
 */
class RuntimeValidationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val repository = RoomLightService.repository
        when (intent.action) {
            ACTION_SHOWCASE -> {
                repository.setLayoutLocked(false)
                repository.confirmClear()

                repository.choosePreset(LightPreset.SUNSET)
                repository.placePending(OrbPosition(0.05f, 1.92f, -1.72f))

                repository.choosePreset(LightPreset.MOON)
                repository.placePending(OrbPosition(0.82f, 1.42f, -2.02f))
                repository.updateSelectedColor(Color.hsv(218f, 0.62f, 1f))
                repository.updateSelectedBrightness(0.78f)
                repository.updateSelectedRadius(3.6f)
            }

            ACTION_LOCK -> repository.setLayoutLocked(true)

            ACTION_PHOTO -> {
                repository.openPhotoSetup()
                repository.setPhotoFrame(PhotoFrame.FILM)
                repository.setPhotoCountdown(3)
                repository.beginPhotoMode()
            }

            ACTION_EXIT_PHOTO -> repository.exitPhotoMode()
        }
    }

    companion object {
        const val ACTION_SHOWCASE = "com.example.roomlightdesigner.debug.SHOWCASE"
        const val ACTION_LOCK = "com.example.roomlightdesigner.debug.LOCK"
        const val ACTION_PHOTO = "com.example.roomlightdesigner.debug.PHOTO"
        const val ACTION_EXIT_PHOTO = "com.example.roomlightdesigner.debug.EXIT_PHOTO"
    }
}
