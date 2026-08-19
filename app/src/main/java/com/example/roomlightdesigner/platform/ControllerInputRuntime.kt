package com.example.roomlightdesigner.platform

import android.util.Log
import com.pico.spatial.tracking.controller.ControllerActionData
import com.pico.spatial.tracking.controller.ControllerTrackingProvider

/**
 * Bridges PICO's public controller tracking stream into the app command flow.
 * Physical controller triggers are not guaranteed to arrive as Android
 * KeyEvents, so LaunchActivity's key mapping remains only a desktop fallback.
 */
class ControllerInputRuntime(
    private val onTriggerPressed: () -> Unit,
) : AutoCloseable {
    private val provider = ControllerTrackingProvider()
    @Volatile private var triggerWasDown = false

    private val listener = object : ControllerTrackingProvider.ControllerActionListener {
        override fun onControllerAction(data: ControllerActionData) {
            val triggerIsDown = data.left.triggerPressed ||
                data.right.triggerPressed ||
                data.left.triggerValue >= TRIGGER_THRESHOLD ||
                data.right.triggerValue >= TRIGGER_THRESHOLD
            if (triggerIsDown && !triggerWasDown) {
                Log.i(TAG, "Physical controller trigger pressed")
                onTriggerPressed()
            }
            triggerWasDown = triggerIsDown
        }
    }

    fun start() {
        provider.addControllerActionListener(listener)
        runCatching { provider.start() }
            .onSuccess { result -> Log.i(TAG, "Controller tracking start: $result") }
            .onFailure { error -> Log.e(TAG, "Controller tracking unavailable", error) }
    }

    override fun close() {
        provider.removeControllerActionListener(listener)
        runCatching { provider.stop() }
        triggerWasDown = false
    }

    private companion object {
        const val TAG = "ControllerInput"
        const val TRIGGER_THRESHOLD = 0.65f
    }
}
