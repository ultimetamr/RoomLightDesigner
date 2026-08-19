package com.example.roomlightdesigner.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.LocalIndication
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.roomlightdesigner.domain.LightOrb
import com.example.roomlightdesigner.domain.LightPreset
import com.example.roomlightdesigner.domain.MAX_LIGHT_ORBS
import com.example.roomlightdesigner.domain.MAX_LIGHT_RADIUS_METERS
import com.example.roomlightdesigner.domain.MIN_LIGHT_RADIUS_METERS
import com.example.roomlightdesigner.domain.OrbPosition
import com.example.roomlightdesigner.domain.PhotoFrame
import com.example.roomlightdesigner.domain.PhotoPhase
import com.example.roomlightdesigner.domain.RoomLightRepository
import com.example.roomlightdesigner.domain.RoomLightState
import com.example.roomlightdesigner.platform.ControllerAction
import com.example.roomlightdesigner.platform.ControllerInputRuntime
import com.example.roomlightdesigner.platform.RoomLightService
import com.example.roomlightdesigner.platform.SpatialTrackingRuntime
import com.pico.spatial.core.container.SpatialViewContent
import com.pico.spatial.core.ecs.Entity
import com.pico.spatial.core.ecs.TransformComponent
import com.pico.spatial.core.math.Vector3
import com.pico.spatial.ui.design.Button
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Slider
import com.pico.spatial.ui.design.Switch
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.foundation.content.SpatialView
import com.pico.spatial.ui.foundation.content.SpatialViewAttachments
import com.pico.spatial.ui.foundation.gesture.TargetEntity
import com.pico.spatial.ui.foundation.gesture.detectSpatialDragGesture
import com.pico.spatial.ui.foundation.gesture.detectSpatialTapGesture
import com.pico.spatial.ui.foundation.haptic.controllerHapticFeedback
import com.pico.spatial.ui.foundation.hover.spatialHoverEffect
import com.pico.spatial.ui.foundation.material.backgroundMaterial
import com.pico.spatial.ui.platform.Material
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.math.atan2

@Composable
fun HomeStage(repository: RoomLightRepository) {
    val state by repository.state.collectAsState()
    val entityRefs = remember { mutableMapOf<String, Entity>() }
    val tracking = remember { SpatialTrackingRuntime() }
    val orb3DScene = remember { Orb3DScene() }
    val controllerInput = remember {
        ControllerInputRuntime { RoomLightService.emit(ControllerAction.TRIGGER) }
    }

    DisposableEffect(tracking) {
        tracking.start()
        onDispose(tracking::stop)
    }

    DisposableEffect(orb3DScene) {
        onDispose(orb3DScene::close)
    }

    DisposableEffect(controllerInput) {
        controllerInput.start()
        onDispose(controllerInput::close)
    }

    LaunchedEffect(tracking) {
        tracking.acceptLatestIfAvailable()?.let { head ->
            val visiblePositions = (0..1).map { index ->
                com.example.roomlightdesigner.domain.headRelativeOrbPosition(head, index)
            }
            repository.reanchorLegacyLayout(
                repository.state.value.orbs.indices.map { index ->
                    com.example.roomlightdesigner.domain.headRelativeOrbPosition(head, index)
                },
            )
            repository.seedStarterLights(visiblePositions)
        }
        tracking.headData.collectLatest { data ->
            val head = tracking.accept(data)
            val visiblePositions = (0..1).map { index ->
                com.example.roomlightdesigner.domain.headRelativeOrbPosition(head, index)
            }
            repository.reanchorLegacyLayout(
                repository.state.value.orbs.indices.map { index ->
                    com.example.roomlightdesigner.domain.headRelativeOrbPosition(head, index)
                },
            )
            repository.seedStarterLights(visiblePositions)
        }
    }

    // Plane loading is independent from HMD sampling. A slow or unavailable
    // room-scan service must never delay first-frame panel placement.
    LaunchedEffect(tracking) {
        tracking.loadKnownPlanes()
    }

    LaunchedEffect(state.panelVisible) {
        if (state.panelVisible) tracking.resetPanelPlacement()
    }

    // Keep threshold detection and the 0.3 s reset animation off the Compose
    // state path. Small HMD motion never changes the panel's world transform.
    LaunchedEffect(tracking, state.panelVisible, state.inPhotoMode) {
        while (true) {
            if (state.panelVisible && !state.inPhotoMode) {
                tracking.updatePanel(System.nanoTime())?.let { pose ->
                    entityRefs[CONTROL_PANEL]
                        ?.components
                        ?.get(TransformComponent::class.java)
                        ?.apply {
                            setPosition(pose.position)
                            setQuaternion(pose.rotation)
                        }
                }
            }
            delay(16)
        }
    }

    LaunchedEffect(Unit) {
        RoomLightService.controllerActions.collectLatest { action ->
            when (action) {
                ControllerAction.MENU -> if (!repository.state.value.inPhotoMode) repository.togglePanel()
                ControllerAction.TRIGGER -> {
                    val current = repository.state.value
                    if (current.inPhotoMode || current.photoPhase == PhotoPhase.RESULT) repository.exitPhotoMode()
                    else if (current.pendingPreset != null) {
                        val position = tracking.nextOrbPosition(current.orbs.size)
                            ?: nextDefaultPosition(current.orbs.size)
                        repository.placePending(position)
                    }
                }
            }
        }
    }

    LaunchedEffect(state.photoPhase) {
        when (state.photoPhase) {
            PhotoPhase.CLEAN_PREROLL -> {
                delay(150)
                repository.beginCountdown()
            }
            PhotoPhase.COUNTDOWN -> {
                while (repository.state.value.photoCountdownRemaining > 0 && repository.state.value.photoPhase == PhotoPhase.COUNTDOWN) {
                    delay(1_000)
                    repository.tickCountdown()
                }
                if (repository.state.value.photoPhase == PhotoPhase.COUNTDOWN) repository.beginCapture()
            }
            PhotoPhase.CAPTURING -> {
                // PICO OS keeps the Stage/Passthrough compositor out of public
                // app capture surfaces. Stay in a clean, frame-only view while
                // the hardware Capture button invokes the privileged system
                // screenshot path; trigger exits when the user is done.
                Unit
            }
            else -> Unit
        }
    }

    SpatialView(
        modifier = Modifier.fillMaxSize(),
        initial = { content, attachments -> syncAttachments(content, attachments, state, entityRefs, tracking, orb3DScene) },
        update = { content, attachments -> syncAttachments(content, attachments, state, entityRefs, tracking, orb3DScene) },
        attachments = {
            // The large hit surface only exists while a preset is armed. Keeping
            // a transparent Android surface alive in the scene can occlude the
            // MR compositor on emulator builds even though it draws no pixels.
            if (state.pendingPreset != null) {
                AttachmentPanel(id = PLACEMENT_SURFACE) {
                    PlacementSurface(state, repository)
                }
            }
            state.orbs.forEach { orb ->
                AttachmentPanel(id = orb.id) {
                    OrbVisual(
                        orb = orb,
                        true3D = orb.preset.isTrue3DOrb(),
                        selected = state.selectedOrbId == orb.id,
                        locked = state.layoutLocked,
                        onSelect = { repository.selectOrb(orb.id) },
                        onMove = { dx, dy, dz -> repository.moveOrbBy(orb.id, dx, dy, dz) },
                        onDelete = { repository.deleteOrb(orb.id) },
                    )
                }
            }
            if (state.panelVisible && !state.inPhotoMode) {
                AttachmentPanel(id = CONTROL_PANEL) {
                    ControlPanel(state, repository)
                }
            }
            if (state.inPhotoMode || state.photoPhase == PhotoPhase.RESULT) {
                AttachmentPanel(id = PHOTO_OVERLAY) {
                    PhotoOverlay(state, onExit = repository::exitPhotoMode)
                }
            }
        },
    )
}

private fun syncAttachments(
    content: SpatialViewContent,
    attachments: SpatialViewAttachments,
    state: RoomLightState,
    refs: MutableMap<String, Entity>,
    tracking: SpatialTrackingRuntime,
    orb3DScene: Orb3DScene,
) {
    orb3DScene.sync(content, state.orbs)
    val activeIds = buildSet {
        if (state.pendingPreset != null) add(PLACEMENT_SURFACE)
        addAll(state.orbs.map(LightOrb::id))
        if (state.panelVisible && !state.inPhotoMode) add(CONTROL_PANEL)
        if (state.inPhotoMode || state.photoPhase == PhotoPhase.RESULT) add(PHOTO_OVERLAY)
    }
    // Attachment entities are owned by SpatialView. Keep only our bookkeeping
    // reference here; destroying them ourselves corrupts the compositor surface
    // when Compose recreates an attachment after a state change.
    refs.keys.filterNot(activeIds::contains).toList().forEach(refs::remove)

    fun attach(id: String, targetPosition: OrbPosition? = null, rotation: com.pico.spatial.core.math.Quat? = null) {
        val entity = attachments.entity(id) ?: return
        if (refs[id] !== entity) {
            refs[id] = entity
            // AttachmentPanel already supplies the correct spatial hit surface.
            // Replacing it with an ECS sphere corrupts the panel compositor on
            // emulator builds and also makes the visible target mismatch hits.
            content.addEntity(entity)
        }
        entity.components[TransformComponent::class.java]?.apply {
            targetPosition?.let { setPosition(Vector3(it.x, it.y, it.z)) }
            rotation?.let(::setQuaternion)
        }
    }

    if (state.pendingPreset != null) {
        val pose = tracking.poseInFront(2.2f)
        attach(
            PLACEMENT_SURFACE,
            pose?.position?.let { OrbPosition(it.x, it.y, it.z) },
            pose?.rotation,
        )
    }
    state.orbs.forEach { orb -> attach(orb.id, orb.position, tracking.faceCurrentHead(orb.position)) }
    if (state.panelVisible && !state.inPhotoMode) {
        attach(CONTROL_PANEL)
        tracking.updatePanel(System.nanoTime())?.let { pose ->
            refs[CONTROL_PANEL]?.components?.get(TransformComponent::class.java)?.apply {
                setPosition(pose.position)
                setQuaternion(pose.rotation)
            }
        }
    }
    if (state.inPhotoMode || state.photoPhase == PhotoPhase.RESULT) {
        val pose = tracking.poseInFront(1.05f)
        attach(
            PHOTO_OVERLAY,
            pose?.position?.let { OrbPosition(it.x, it.y, it.z) },
            pose?.rotation,
        )
    }
}

@Composable
private fun PlacementSurface(state: RoomLightState, repository: RoomLightRepository) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .size(1100.dp, 720.dp)
            .pointerInput(state.pendingPreset, state.layoutLocked, state.atCapacity) {
                detectSpatialTapGesture(context, targetedToEntity = TargetEntity.any()) { tap ->
                    if (state.pendingPreset != null && !state.layoutLocked && !state.atCapacity) {
                        repository.placePending(OrbPosition(tap.position.x, tap.position.y, tap.position.z))
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (state.pendingPreset != null) {
            Text(
                if (state.atCapacity) "已达 8 个上限" else "射线指向空间 · 扣动扳机放置 ${state.pendingPreset.displayName}",
                modifier = Modifier.backgroundMaterial(true, Material.Regular).padding(14.dp),
            )
        }
    }
}

@Composable
private fun OrbVisual(
    orb: LightOrb,
    true3D: Boolean,
    selected: Boolean,
    locked: Boolean,
    onSelect: () -> Unit,
    onMove: (Float, Float, Float) -> Unit,
    onDelete: () -> Unit,
) {
    val context = LocalContext.current
    val baseColor = orb.color
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(156.dp)
            .pointerInput(orb.id, locked) {
                if (!locked) {
                    detectSpatialDragGesture(context, targetedToEntity = TargetEntity.any()) { drag ->
                        onSelect()
                        onMove(drag.dragAmount.x * 0.0015f, -drag.dragAmount.y * 0.0015f, drag.dragAmount.z * 0.0015f)
                    }
                }
            }
            .pointerInput(orb.id, locked) {
                if (!locked) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)
                        // waitForUpOrCancellation() also returns null for a
                        // cancelled gesture. Wrap its result so only the outer
                        // timeout—not ray cancellation—counts as a 2 s hold.
                        val finishedBeforeTimeout = withTimeoutOrNull(2_000) {
                            waitForUpOrCancellation()
                            true
                        } ?: false
                        if (!finishedBeforeTimeout) onDelete()
                    }
                }
            }
            .spatialHoverEffect(enabled = !locked)
            .clickable(
                enabled = !locked,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onSelect,
            )
            .controllerHapticFeedback(interactionSource = interactionSource)
            .drawBehind {
                if (true3D) {
                    // A very soft planar bloom keeps the target discoverable
                    // if the device temporarily cannot allocate its 3D mesh;
                    // the visible core itself is rendered by Orb3DScene.
                    drawCircle(
                        brush = Brush.radialGradient(listOf(baseColor.copy(alpha = 0.15f), Color.Transparent)),
                        radius = size.minDimension * 0.48f,
                    )
                } else {
                    drawCircle(
                        brush = Brush.radialGradient(listOf(baseColor.copy(alpha = 0.78f * orb.brightness), baseColor.copy(alpha = 0.18f), Color.Transparent)),
                        radius = size.minDimension * 0.49f,
                    )
                    drawCircle(baseColor.copy(alpha = 0.96f), radius = size.minDimension * 0.18f)
                }
                if (selected) drawCircle(Color.White, radius = size.minDimension * 0.34f, style = Stroke(width = 5f))
                if (!true3D && orb.preset != LightPreset.STAGE) {
                    repeat(12) { index ->
                        val x = size.width * (0.22f + ((index * 37) % 57) / 100f)
                        val y = size.height * (0.20f + ((index * 23) % 61) / 100f)
                        drawCircle(baseColor.copy(alpha = 0.42f), radius = 2f + index % 3, center = Offset(x, y))
                    }
                }
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Text(
            text = if (locked) "🔒 ${orb.preset.symbol} ${orb.preset.displayName}" else "${orb.preset.symbol} ${orb.preset.displayName}",
            modifier = Modifier.backgroundMaterial(true, Material.Regular).padding(horizontal = 10.dp, vertical = 4.dp),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ControlPanel(state: RoomLightState, repository: RoomLightRepository) {
    Box(
        modifier = Modifier
            .size(720.dp, 540.dp)
            .clip(RoundedCornerShape(28.dp))
            .backgroundMaterial(true, Material.Regular)
            .padding(26.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state.clearConfirmationVisible) {
            ClearConfirmation(state.orbs.size, repository)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.width(668.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("房间灯光师", style = PicoTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("${state.orbs.size}/$MAX_LIGHT_ORBS 光球 · 自动保存", color = PicoTheme.colorScheme.labelSecondary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(if (state.layoutLocked) "布局已锁定" else "锁定布局")
                        Switch(checked = state.layoutLocked, onCheckedChange = repository::setLayoutLocked)
                    }
                }

                if (state.photoPhase == PhotoPhase.SETUP) {
                    PhotoSetupPanel(state, repository)
                } else {
                    Text("光球预设", style = PicoTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LightPreset.entries.forEach { preset ->
                            Button(
                                onClick = { repository.choosePreset(preset) },
                                enabled = !state.layoutLocked && !state.atCapacity,
                            ) { Text("${preset.symbol} ${preset.displayName}") }
                        }
                    }
                    if (state.pendingPreset == LightPreset.CUSTOM || state.selectedOrb?.preset == LightPreset.CUSTOM) {
                        ColorWheel(
                            selected = state.selectedOrb?.color ?: Color(state.customColorArgb),
                            onColor = { color ->
                                if (state.selectedOrb != null) repository.updateSelectedColor(color) else repository.setCustomColor(color)
                            },
                        )
                    }
                    state.selectedOrb?.let { orb -> OrbInspector(orb, state.layoutLocked, repository) }
                        ?: Text(
                            if (state.atCapacity) "已达上限；新增按钮已禁用。" else "选择预设后，用射线点击空间放置；点击光球可编辑。",
                            color = PicoTheme.colorScheme.labelSecondary,
                        )
                    Spacer(Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = repository::requestClear, enabled = state.orbs.isNotEmpty() && !state.layoutLocked) { Text("清空") }
                        Button(onClick = repository::openPhotoSetup) { Text("摄影模式") }
                        state.notice?.let { Text(it, color = PicoTheme.colorScheme.labelSecondary) }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrbInspector(orb: LightOrb, locked: Boolean, repository: RoomLightRepository) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("已选中 · ${orb.preset.displayName}", style = PicoTheme.typography.titleMedium)
        Text("颜色")
        ColorWheel(orb.color, repository::updateSelectedColor, enabled = !locked)
        Text("亮度 ${(orb.brightness * 100).toInt()}%")
        Slider(value = orb.brightness, onValueChange = repository::updateSelectedBrightness, enabled = !locked, valueRange = 0f..1f)
        Text("影响范围 ${"%.1f".format(orb.radiusMeters)}m")
        Slider(
            value = orb.radiusMeters,
            onValueChange = repository::updateSelectedRadius,
            enabled = !locked,
            valueRange = MIN_LIGHT_RADIUS_METERS..MAX_LIGHT_RADIUS_METERS,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { repository.deleteOrb(orb.id) }, enabled = !locked) { Text("删除") }
            Button(onClick = { repository.selectOrb(null) }) { Text("完成") }
            Text("按住扳机 2 秒也可删除", color = PicoTheme.colorScheme.labelSecondary)
        }
    }
}

@Composable
private fun PhotoSetupPanel(state: RoomLightState, repository: RoomLightRepository) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text("摄影模式", style = PicoTheme.typography.titleLarge)
        Text("相框")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PhotoFrame.entries.forEach { frame ->
                Button(onClick = { repository.setPhotoFrame(frame) }) {
                    Text(if (state.photoFrame == frame) "✓ ${frame.displayName}" else frame.displayName)
                }
            }
        }
        Text("倒计时")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(3, 5, 10).forEach { seconds ->
                Button(onClick = { repository.setPhotoCountdown(seconds) }) {
                    Text(if (state.photoCountdownSeconds == seconds) "✓ ${seconds}秒" else "${seconds}秒")
                }
            }
        }
        Text(
            "倒计时归零后画面保持纯净；按右手柄截图键（相机图标）保存完整 MR 画面，扣动扳机退出。",
            color = PicoTheme.colorScheme.labelSecondary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = repository::beginPhotoMode) { Text("开始倒计时") }
            Button(onClick = repository::cancelPhotoSetup) { Text("取消") }
        }
    }
}

@Composable
private fun ClearConfirmation(count: Int, repository: RoomLightRepository) {
    Column(
        modifier = Modifier.width(480.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("清空全部 $count 个光球？", style = PicoTheme.typography.titleLarge)
        Text("此操作无法撤销，最近保存的方案也会同步更新。", textAlign = TextAlign.Center)
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Button(onClick = repository::cancelClear) { Text("取消") }
            Button(onClick = repository::confirmClear) { Text("确认清空") }
        }
    }
}

@Composable
private fun ColorWheel(selected: Color, onColor: (Color) -> Unit, enabled: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Canvas(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .pointerInput(enabled) {
                    if (enabled) detectTapGestures { offset ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val hue = ((Math.toDegrees(atan2((offset.y - center.y).toDouble(), (offset.x - center.x).toDouble())) + 360.0) % 360.0).toFloat()
                        onColor(Color.hsv(hue, 0.78f, 1f))
                    }
                },
        ) {
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red),
                ),
            )
            drawCircle(Color.Black.copy(alpha = 0.2f), radius = size.minDimension * 0.18f)
        }
        Box(Modifier.size(40.dp).clip(CircleShape).background(selected))
    }
}

@Composable
private fun PhotoOverlay(state: RoomLightState, onExit: () -> Unit) {
    val border = when (state.photoFrame) {
        PhotoFrame.WHITE -> Color.White
        PhotoFrame.FILM -> Color(0xFF0A0A0C) // design-style: fixed-figma-color Visual@4 film frame
        PhotoFrame.INSTANT -> Color(0xFFF4EBDD) // design-style: fixed-figma-color Visual@4 instant frame
    }
    val filmPerforation = Color(0xFFDBD7CD) // design-style: fixed-figma-color Visual@4 film perforation
    Box(
        modifier = Modifier
            .size(1040.dp, 650.dp)
            .drawBehind {
                val stroke = if (state.photoFrame == PhotoFrame.INSTANT) 34f else 22f
                drawRect(border, style = Stroke(stroke))
                if (state.photoFrame == PhotoFrame.FILM) {
                    repeat(12) { index ->
                        val x = 24f + index * (size.width - 48f) / 11f
                        drawRect(filmPerforation, topLeft = Offset(x - 6f, 8f), size = androidx.compose.ui.geometry.Size(12f, 8f))
                        drawRect(filmPerforation, topLeft = Offset(x - 6f, size.height - 16f), size = androidx.compose.ui.geometry.Size(12f, 8f))
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        when (state.photoPhase) {
            PhotoPhase.CLEAN_PREROLL -> Unit
            PhotoPhase.COUNTDOWN -> Text(
                state.photoCountdownRemaining.toString(),
                style = PicoTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.backgroundMaterial(true, Material.Regular).padding(30.dp),
            )
            // Keep only the selected frame visible in the captured MR image.
            PhotoPhase.CAPTURING -> Unit
            PhotoPhase.RESULT -> Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(state.photoResultMessage ?: "拍摄完成", style = PicoTheme.typography.titleLarge)
                Button(onClick = onExit) { Text("返回布光") }
            }
            else -> Unit
        }
    }
}

private fun nextDefaultPosition(index: Int): OrbPosition {
    val positions = listOf(
        OrbPosition(-0.65f, 1.25f, -1.9f),
        OrbPosition(0.65f, 1.45f, -2.2f),
        OrbPosition(-1.0f, 1.75f, -2.7f),
        OrbPosition(1.0f, 1.15f, -2.8f),
        OrbPosition(0f, 2.0f, -3.0f),
        OrbPosition(-1.35f, 0.9f, -3.2f),
        OrbPosition(1.35f, 1.8f, -3.4f),
        OrbPosition(0f, 0.8f, -3.6f),
    )
    return positions[index.coerceIn(0, positions.lastIndex)]
}

private const val CONTROL_PANEL = "room-controls"
private const val PHOTO_OVERLAY = "photo-overlay"
private const val PLACEMENT_SURFACE = "placement-surface"
