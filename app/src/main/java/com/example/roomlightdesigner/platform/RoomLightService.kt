package com.example.roomlightdesigner.platform

import android.app.Application
import android.app.Activity
import com.example.roomlightdesigner.data.SharedPreferencesLayoutStorage
import com.example.roomlightdesigner.domain.RoomLightRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.lang.ref.WeakReference

enum class ControllerAction { MENU, TRIGGER }

object RoomLightService {
    lateinit var repository: RoomLightRepository
        private set

    private val mutableControllerActions = MutableSharedFlow<ControllerAction>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val controllerActions = mutableControllerActions.asSharedFlow()
    private var activityReference = WeakReference<Activity>(null)
    val currentActivity: Activity? get() = activityReference.get()

    fun initialize(application: Application) {
        if (!::repository.isInitialized) {
            repository = RoomLightRepository(SharedPreferencesLayoutStorage(application))
        }
    }

    fun emit(action: ControllerAction) {
        mutableControllerActions.tryEmit(action)
    }

    fun setCurrentActivity(activity: Activity) {
        activityReference = WeakReference(activity)
    }

    fun clearCurrentActivity(activity: Activity) {
        if (activityReference.get() === activity) activityReference.clear()
    }
}
