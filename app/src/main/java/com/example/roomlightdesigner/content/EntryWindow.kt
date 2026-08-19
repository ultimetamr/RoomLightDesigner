package com.example.roomlightdesigner.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roomlightdesigner.domain.RoomLightRepository
import com.pico.spatial.ui.design.Button
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.foundation.material.backgroundMaterial
import com.pico.spatial.ui.platform.Material
import com.pico.spatial.ui.platform.containers.OpenStageResult
import com.pico.spatial.ui.platform.containers.StageStyle
import com.pico.spatial.ui.platform.containers.openStage
import kotlinx.coroutines.launch

const val ROOM_STAGE_ID = "room-stage"

@Composable
fun EntryWindow(repository: RoomLightRepository) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by repository.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.width(620.dp).backgroundMaterial(true, Material.Regular).padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text("房间灯光师", style = PicoTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("混合现实 · 真实房间可见", color = PicoTheme.colorScheme.labelSecondary)
            Text(
                if (state.orbs.isEmpty()) "从空白房间开始，用手柄射线放置光球。"
                else "将恢复最近方案：${state.orbs.size} 个光球${if (state.layoutLocked) " · 已锁定" else ""}",
                style = PicoTheme.typography.bodyLarge,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    scope.launch {
                        when (context.openStage(ROOM_STAGE_ID, StageStyle.Mixed)) {
                            is OpenStageResult.Allowed -> Unit
                            else -> repository.clearNotice()
                        }
                    }
                }) { Text("进入房间布光") }
                Text("进入后可随时关闭 Stage 返回这里", color = PicoTheme.colorScheme.labelSecondary)
            }
        }
    }
}
