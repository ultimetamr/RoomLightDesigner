package com.example.roomlightdesigner.platform

import android.view.KeyEvent
import com.pico.spatial.ui.platform.stub.SpatialLaunchActivity

class LaunchActivity : SpatialLaunchActivity() {
    override fun onResume() {
        super.onResume()
        RoomLightService.setCurrentActivity(this)
    }

    override fun onPause() {
        RoomLightService.clearCurrentActivity(this)
        super.onPause()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_MENU,
                KeyEvent.KEYCODE_BUTTON_START,
                -> RoomLightService.emit(ControllerAction.MENU)

                KeyEvent.KEYCODE_BUTTON_R1,
                KeyEvent.KEYCODE_BUTTON_R2,
                KeyEvent.KEYCODE_BUTTON_A,
                -> RoomLightService.emit(ControllerAction.TRIGGER)
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
