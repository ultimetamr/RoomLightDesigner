package com.example.roomlightdesigner

import com.example.roomlightdesigner.domain.HeadPose
import com.example.roomlightdesigner.domain.HorizontalObstacle
import com.example.roomlightdesigner.domain.PanelPlacementController
import com.example.roomlightdesigner.domain.headRelativeOrbPosition
import com.example.roomlightdesigner.domain.withResolvedStageEyeHeight
import com.pico.spatial.core.math.Quat
import com.pico.spatial.core.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin

class PanelPlacementControllerTest {
    @Test
    fun zeroHeightTrackingPoseFallsBackToStageEyeHeight() {
        val resolved = HeadPose(Vector3(3f, 0f, -2f), Quat.identity()).withResolvedStageEyeHeight()

        assertEquals(1.62f, resolved.position.y, EPSILON)
        assertEquals(3f, resolved.position.x, EPSILON)
        assertEquals(-2f, resolved.position.z, EPSILON)
    }

    @Test
    fun validSeatedHeightIsPreserved() {
        val resolved = HeadPose(Vector3(0f, 1.15f, 0f), Quat.identity()).withResolvedStageEyeHeight()

        assertEquals(1.15f, resolved.position.y, EPSILON)
    }

    @Test
    fun newOrbUsesCurrentWorldHeadPoseInsteadOfLaunchOrigin() {
        val head = HeadPose(Vector3(12f, 1.68f, 7f), Quat.identity())

        val position = headRelativeOrbPosition(head, 0)

        assertTrue(position.x > 11f)
        assertEquals(1.68f, position.y, EPSILON)
        assertTrue(position.z < 6f)
    }

    @Test
    fun initialPlacementIsOneMeterForwardAtEyeHeightAndFacesViewer() {
        val controller = PanelPlacementController()
        val head = HeadPose(Vector3(0f, 1.72f, 0f), Quat.identity())

        val panel = controller.update(head, emptyList(), 0L)

        assertEquals(0f, panel.position.x, EPSILON)
        assertEquals(1.72f, panel.position.y, EPSILON)
        assertEquals(-1f, panel.position.z, EPSILON)
        val panelFront = panel.rotation.rotateVector(Vector3.FORWARD)
        assertEquals(1f, Vector3.dot(panelFront, Vector3.FORWARD), EPSILON)
    }

    @Test
    fun smallHeadTurnDoesNotMoveWorldLockedPanel() {
        val controller = PanelPlacementController()
        val initialHead = HeadPose(Vector3(0f, 1.7f, 0f), Quat.identity())
        val initial = controller.update(initialHead, emptyList(), 0L)

        val turnedHead = initialHead.copy(rotation = yawDegrees(30f))
        val afterTurn = controller.update(turnedHead, emptyList(), 100_000_000L)

        assertEquals(initial, afterTurn)
        assertFalse(controller.requiresReset(afterTurn, turnedHead))
    }

    @Test
    fun anglePastNinetyDegreesResetsOverThreeHundredMilliseconds() {
        val controller = PanelPlacementController()
        val initialHead = HeadPose(Vector3(0f, 1.7f, 0f), Quat.identity())
        val initial = controller.update(initialHead, emptyList(), 0L)
        val reversedHead = initialHead.copy(rotation = yawDegrees(180f))

        assertEquals(initial, controller.update(reversedHead, emptyList(), 1_000_000_000L))
        val halfway = controller.update(reversedHead, emptyList(), 1_150_000_000L)
        val complete = controller.update(reversedHead, emptyList(), 1_300_000_000L)

        assertEquals(0f, halfway.position.z, 0.02f)
        assertEquals(1f, complete.position.z, 0.02f)
        assertFalse(controller.requiresReset(complete, reversedHead))
    }

    @Test
    fun largeHorizontalDistanceDeviationTriggersReset() {
        val controller = PanelPlacementController()
        val head = HeadPose(Vector3(0f, 1.7f, 0f), Quat.identity())
        val panel = controller.update(head, emptyList(), 0L)

        val movedHead = HeadPose(Vector3(0f, 1.7f, 1.0f), Quat.identity())

        assertTrue(controller.requiresReset(panel, movedHead))
    }

    @Test
    fun obstacleInPlacementCorridorMovesPanelBackTowardViewer() {
        val controller = PanelPlacementController()
        val head = HeadPose(Vector3(0f, 1.7f, 0f), Quat.identity())
        val wall = HorizontalObstacle(
            center = Vector3(0f, 1.7f, -0.75f),
            halfWidthMeters = 1f,
            halfHeightMeters = 1f,
        )

        val panel = controller.update(head, listOf(wall), 0L)

        assertEquals(-0.55f, panel.position.z, EPSILON)
        assertEquals(1.7f, panel.position.y, EPSILON)
    }

    private fun yawDegrees(degrees: Float): Quat {
        val radians = Math.toRadians(degrees.toDouble()).toFloat()
        return Quat(0f, sin(radians / 2f), 0f, cos(radians / 2f)).normalize()
    }

    private companion object {
        const val EPSILON = 0.001f
    }
}
