# RoomLightDesigner

RoomLightDesigner（房间灯光师）是一款面向 PICO OS 6 的混合现实空间布光工具。用户可以在真实房间中放置和调整 3D 光球，通过伪光晕与体积粒子快速预览日落、月光、霓虹和舞台等灯光氛围。

## 主要功能

- 四款预设光球与自定义颜色
- 亮度、颜色和影响范围实时调节
- 光球放置、选择、拖拽、删除及布局锁定
- 最多同时管理 8 个光球
- 自动保存并恢复最近一套空间布光方案
- 面板头部相对生成、世界坐标锁定和自动复位
- 摄影模式、相框切换和 3/5/10 秒倒计时

## 技术栈

- Kotlin
- Jetpack Compose
- PICO Spatial UI
- PICO Spatial SDK BOM 0.13.3
- Mixed Passthrough Stage
- Android SDK 35

## 构建

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug
```

Debug APK 生成位置：

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 项目信息

- 包名：`com.example.roomlightdesigner`
- 主入口：`app/src/main/java/com/example/roomlightdesigner/platform/LaunchActivity.kt`
- 空间场景：`app/src/main/java/com/example/roomlightdesigner/content/HomeStage.kt`
- 3D 光球：`app/src/main/java/com/example/roomlightdesigner/content/Orb3DScene.kt`

摄影模式在倒计时结束后使用 PICO 系统截图键保存完整 MR 画面。Android 公共截图接口无法捕获 PICO Stage、Passthrough 与 ECS 合成层。
