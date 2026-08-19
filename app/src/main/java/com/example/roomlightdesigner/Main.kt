package com.example.roomlightdesigner

import com.example.roomlightdesigner.content.EntryWindow
import com.example.roomlightdesigner.content.HomeStage
import com.example.roomlightdesigner.content.ROOM_STAGE_ID
import com.example.roomlightdesigner.platform.RoomLightService
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.foundation.dsl.DefaultWindowContainer
import com.pico.spatial.ui.foundation.dsl.SpatialAppScope
import com.pico.spatial.ui.foundation.dsl.Stage

fun mainApp(scope: SpatialAppScope) = with(scope) {
    DefaultWindowContainer {
        PicoTheme { EntryWindow(RoomLightService.repository) }
    }
    Stage(id = ROOM_STAGE_ID) {
        PicoTheme { HomeStage(RoomLightService.repository) }
    }
}
