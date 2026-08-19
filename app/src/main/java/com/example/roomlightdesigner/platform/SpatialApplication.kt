package com.example.roomlightdesigner.platform

import android.app.Application
import com.pico.spatial.ui.foundation.dsl.launch
import com.example.roomlightdesigner.mainApp

class SpatialApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RoomLightService.initialize(this)
        launch(::mainApp)
    }
}
