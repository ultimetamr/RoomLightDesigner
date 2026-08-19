package com.example.roomlightdesigner.platform

import android.util.Log
import com.example.roomlightdesigner.domain.HeadPose
import com.example.roomlightdesigner.domain.HorizontalObstacle
import com.example.roomlightdesigner.domain.OrbPosition
import com.example.roomlightdesigner.domain.PanelPlacementController
import com.example.roomlightdesigner.domain.PanelPose
import com.example.roomlightdesigner.domain.headRelativeOrbPosition
import com.example.roomlightdesigner.domain.withResolvedStageEyeHeight
import com.pico.spatial.core.lifecycle.Cancellable
import com.pico.spatial.core.math.Quat
import com.pico.spatial.core.math.Vector3
import com.pico.spatial.sense.base.AnchorUpdate
import com.pico.spatial.sense.plane.PlaneAnchor
import com.pico.spatial.sense.plane.PlaneOrientation
import com.pico.spatial.sense.plane.PlaneTrackingManager
import com.pico.spatial.tracking.hmd.HMDTrackingData
import com.pico.spatial.tracking.hmd.HMDTrackingProvider
import kotlinx.coroutines.flow.SharedFlow
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Public-SDK bridge for HMD pose and detected room planes. Domain placement
 * math stays testable while lifecycle/error handling remains at the Stage edge.
 */
class SpatialTrackingRuntime {
    private val hmdProvider = HMDTrackingProvider()
    private val panelController = PanelPlacementController()
    private val planeObstacles = ConcurrentHashMap<UUID, HorizontalObstacle>()
    private var planeSubscription: Cancellable? = null
    private var heightFallbackLogged = false

    @Volatile
    var latestHeadPose: HeadPose? = null
        private set

    val headData: SharedFlow<HMDTrackingData> get() = hmdProvider.dataFlow

    fun start() {
        // A missing sensing permission or unavailable room scan must not make
        // the rest of the app crash; panel placement simply proceeds without
        // obstacle shortening until plane data becomes available.
        runCatching { hmdProvider.start() }
        runCatching {
            PlaneTrackingManager.start()
            planeSubscription = PlaneTrackingManager.subscribeAnchorUpdate { update ->
                when (update.event) {
                    AnchorUpdate.Event.ADDED,
                    AnchorUpdate.Event.UPDATED,
                    AnchorUpdate.Event.LOADED,
                    -> upsertObstacle(update.anchor)
                    AnchorUpdate.Event.REMOVED -> planeObstacles.remove(update.anchor.anchorUUID)
                    else -> Unit
                }
            }
        }
    }

    suspend fun loadKnownPlanes() {
        runCatching { PlaneTrackingManager.loadAllAnchors() }
            .getOrNull()
            ?.forEach(::upsertObstacle)
    }

    fun stop() {
        planeSubscription?.cancel()
        planeSubscription = null
        runCatching { PlaneTrackingManager.stop() }
        runCatching { hmdProvider.stop() }
    }

    fun accept(data: HMDTrackingData): HeadPose {
        val raw = HeadPose(data.hmdPose.position, data.hmdPose.rotation)
        val resolved = raw.withResolvedStageEyeHeight()
        if (!heightFallbackLogged && resolved.position.y != raw.position.y) {
            heightFallbackLogged = true
            Log.w(TAG, "Invalid Stage HMD height ${raw.position.y}; using ${resolved.position.y} m eye-height fallback")
        }
        return resolved.also { latestHeadPose = it }
    }

    fun acceptLatestIfAvailable(): HeadPose? = runCatching { hmdProvider.latestData }
        .getOrNull()
        ?.let(::accept)

    fun resetPanelPlacement() = panelController.reset()

    fun updatePanel(nowNanos: Long): PanelPose? {
        val head = latestHeadPose ?: return null
        return panelController.update(head, planeObstacles.values, nowNanos)
    }

    fun nextOrbPosition(index: Int): OrbPosition? = latestHeadPose?.let { headRelativeOrbPosition(it, index) }

    fun poseInFront(distanceMeters: Float): PanelPose? {
        val head = latestHeadPose ?: return null
        val forward = horizontalForward(head.rotation)
        val position = Vector3(
            head.position.x + forward.x * distanceMeters,
            head.position.y,
            head.position.z + forward.z * distanceMeters,
        )
        return PanelPose(position, faceViewerRotation(forward))
    }

    fun faceCurrentHead(position: OrbPosition): Quat? {
        val head = latestHeadPose ?: return null
        val dx = head.position.x - position.x
        val dz = head.position.z - position.z
        val length = kotlin.math.sqrt(dx * dx + dz * dz)
        if (length < 0.0001f) return Quat.identity()
        val normal = Vector3(dx / length, 0f, dz / length)
        val yaw = atan2(normal.x, normal.z)
        return yawRotation(yaw)
    }

    private fun upsertObstacle(anchor: PlaneAnchor) {
        if (anchor.planeOrientation !in setOf(PlaneOrientation.VERTICAL, PlaneOrientation.ARBITRARY)) {
            planeObstacles.remove(anchor.anchorUUID)
            return
        }
        val bounds = anchor.boundingBoxSize
        planeObstacles[anchor.anchorUUID] = HorizontalObstacle(
            center = anchor.transform.position,
            halfWidthMeters = max(0.05f, bounds.x * 0.5f),
            halfHeightMeters = max(0.05f, bounds.y * 0.5f),
        )
    }
}

private const val TAG = "SpatialTracking"

private fun horizontalForward(rotation: Quat): Vector3 {
    val raw = rotation.rotateVector(Vector3.BACK)
    val length = kotlin.math.sqrt(raw.x * raw.x + raw.z * raw.z)
    return if (length < 0.0001f) Vector3.BACK else Vector3(raw.x / length, 0f, raw.z / length)
}

private fun faceViewerRotation(forward: Vector3): Quat = yawRotation(atan2(-forward.x, -forward.z))

private fun yawRotation(yaw: Float): Quat {
    val halfYaw = yaw * 0.5f
    return Quat(0f, sin(halfYaw), 0f, cos(halfYaw)).normalize()
}
