# Preview / QA Test Report · RoomLightDesigner

> Generation revision: 4 | scope: `web_design_validation_only` | sources: Interaction@7, Visual@4, DesignSystemReview@3 | CR-06 host evidence complete; fresh independent QA required

## 1. Provenance and input readiness

| Input Fact | Source / assertion | Verdict |
|---|---|---|
| Design-system review | Critique@3; final invocation `DCR-CR03-R2-b6b91f7a-f4d9-4b40-9ff7-14f643f95853` = pass | pass |
| States and transitions | Interaction@7 §6, 12 states / 19 transitions | pass |
| Core component structure | Visual@4 §5/§5.1, 9 components × 8 parts pass | pass |
| renderSpec | Visual@4 §5, 53 stable elements | pass |
| dataBindings | Visual@4 §5, 49 classified bindings with fallback | pass |
| Variants / states | Visual@4 §5, itemized below | pass |
| Responsive / motion | Interaction@7 §§5.4,7; Visual@4 §5; Large/Compact/Constrained/Reduce Motion | pass |
| Visual grammar | Visual@4 §§2–8; token/material conflicts closed | pass |

## 2. Preview Coverage Manifest (declared before preview generation)

### 2.1 State / transition denominator

| Type | ID | Source Fact Anchor | Trigger | Target / visible result | Confirmation | Verdict |
|---|---|---|---|---|---|---|
| state | `S0_SHARED_ENTRY` | Interaction §6.1 | launch / Stage close | Shared entry, restore summary, enter action | N/A | included |
| state | `S1_ROOM_IDLE` | Interaction §6.1 | Stage opened | room orbs + palette + guard | N/A | included |
| state | `S2_PLACEMENT_AIM` | Interaction §6.1 | choose recipe | ray, reticle, ghost and commit gate | N/A | included |
| state | `S3_ORB_SELECTED` | Interaction §6.1 | select orb | selected rings + inspector | N/A | included |
| state | `S4_ORB_DRAGGING` | Interaction §6.1 | hold + move | tether and live pose | N/A | included |
| state | `S5_LAYOUT_LOCKED` | Interaction §6.1 | lock on | lock label and manipulation disabled | N/A | included |
| state | `S6_CLEAR_CONFIRM` | Interaction §6.1 | clear request | blocking clear dialog | N/A | included |
| state | `S7_PHOTO_PREP` | Interaction §6.1 | photo request | frame/timer choices and start | N/A | included |
| state | `S8_PHOTO_READY` | Interaction §6.1 | start photo | clean pre-roll, no editor/ray | N/A | included |
| state | `S9_PHOTO_COUNTDOWN` | Interaction §6.1 | pre-roll ready | frame + center countdown | N/A | included |
| state | `S10_CAPTURE_RESULT` | Interaction §6.1 | capture callback | saved/failure result | N/A | included |
| state | `S11_TRACKING_RECOVERY` | Interaction §6.1 | tracking invalid | frozen layout + retry/return | N/A | included |
| transition | `TR-ENTER` | Interaction §6.2 | `user.enterStage` | S0→S1 | yes | included |
| transition | `TR-PLACE-AIM` | Interaction §6.2 | `user.chooseRecipe` | S1→S2 | no | included |
| transition | `TR-PLACE-COMMIT` | Interaction §6.2 | `controller.triggerPressed` | S2→S1 | no | included |
| transition | `TR-SELECT` | Interaction §6.2 | `controller.triggerClickOrb` | S1→S3 | no | included |
| transition | `TR-DRAG` | Interaction §6.2 | `controller.triggerHoldMoved` | S3→S4 | no | included |
| transition | `TR-DRAG-END` | Interaction §6.2 | `controller.triggerReleased` | S4→S3 | no | included |
| transition | `TR-HOLD-DELETE` | Interaction §6.2 | `controller.triggerHeldStationary2s` | S3→S1 | progressive 2s | included |
| transition | `TR-LOCK` | Interaction §6.2 | `user.lockEnabled` | S1/S3→S5 | no | included |
| transition | `TR-UNLOCK` | Interaction §6.2 | `user.lockDisabled` | S5→S1 | no | included |
| transition | `TR-CLEAR-OPEN` | Interaction §6.2 | `user.clearAllRequested` | S1/S3→S6 | no | included |
| transition | `TR-CLEAR-CONFIRM` | Interaction §6.2 | `user.clearAllConfirmed` | S6→S1 | yes | included |
| transition | `TR-PHOTO-PREP` | Interaction §6.2 | `user.photoRequested` | S1/S5→S7 | no | included |
| transition | `TR-PHOTO-READY` | Interaction §6.2 | `user.startPhotoMode` | S7→S8 | yes | included |
| transition | `TR-COUNTDOWN` | Interaction §6.2 | `system.photoPreRollReady` | S8→S9 | no | included |
| transition | `TR-CAPTURE` | Interaction §6.2 | `timer.reachedZero` | S9→S10 | no | included |
| transition | `TR-PHOTO-EXIT` | Interaction §6.2 | `controller.triggerPressed` | S8/S9/S10→S1/S5 | no | included |
| transition | `TR-PANEL` | Interaction §6.2 | `controller.menuPressed` | same | no | included |
| transition | `TR-TRACKING-LOST` | Interaction §6.2 | `system.trackingInvalid` | workspace/photo→S11 | no | included |
| transition | `TR-STAGE-EXIT` | Interaction §6.2 | `user.closeStage/system.stageClosed` | S1–S11→S0 | yes for user | included |

### 2.2 renderSpec.elements[] denominator

| Component | element id | Source Fact Anchor | Visible label | bind | Conditional rule | Verdict |
|---|---|---|---|---|---|---|
| EntryGate | `entry_title` | Visual §5 EntryGate renderSpec | 房间灯光师 | `static` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| EntryGate | `entry_mode` | Visual §5 EntryGate renderSpec | 混合现实 · 真实房间可见 | `capability.passthrough` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| EntryGate | `restore_summary` | Visual §5 EntryGate renderSpec | 将恢复最近的布光方案 | `snapshot.summary` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| EntryGate | `enter_stage` | Visual §5 EntryGate renderSpec | 进入房间布光 | `capability.stageReady` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| EntryGate | `entry_exit_help` | Visual §5 EntryGate renderSpec | 退出 / 使用说明 | `helpState` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| EntryGate | `discard_invalid` | Visual §5 EntryGate renderSpec | 忽略无效记录 | `snapshot.invalidCount` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightPalette | `preset_sunset` | Visual §5 LightPalette renderSpec | 日落 · 暖雾 | `presets.sunset` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightPalette | `preset_moon` | Visual §5 LightPalette renderSpec | 月光 · 星尘 | `presets.moon` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightPalette | `preset_neon` | Visual §5 LightPalette renderSpec | 霓虹 · 扫描 | `presets.neon` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightPalette | `preset_stage` | Visual §5 LightPalette renderSpec | 舞台 · 聚光 | `presets.stage` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightPalette | `preset_custom` | Visual §5 LightPalette renderSpec | 自定义颜色 | `pendingRecipe.color` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| WorkspaceGuardBar | `orb_capacity` | Visual §5 WorkspaceGuardBar renderSpec | 0/8 光球 | `orbCount` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| WorkspaceGuardBar | `save_state` | Visual §5 WorkspaceGuardBar renderSpec | 已自动保存 | `persistence.state` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| WorkspaceGuardBar | `tracking_state` | Visual §5 WorkspaceGuardBar renderSpec | 空间稳定 | `tracking.state` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| WorkspaceGuardBar | `layout_lock` | Visual §5 WorkspaceGuardBar renderSpec | 锁定布局 | `layoutLocked` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| WorkspaceGuardBar | `clear_all` | Visual §5 WorkspaceGuardBar renderSpec | 清空 | `orbCount,layoutLocked` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| WorkspaceGuardBar | `photo_entry` | Visual §5 WorkspaceGuardBar renderSpec | 摄影模式 | `capture.capability` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| OrbInspector | `orb_identity` | Visual §5 OrbInspector renderSpec | 已选中 · 日落 | `selected.kind` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| OrbInspector | `color_control` | Visual §5 OrbInspector renderSpec | 颜色 | `selected.color` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| OrbInspector | `brightness_control` | Visual §5 OrbInspector renderSpec | 亮度 70% | `selected.brightness` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| OrbInspector | `radius_control` | Visual §5 OrbInspector renderSpec | 范围 2.0m | `selected.radius` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| OrbInspector | `hold_delete_hint` | Visual §5 OrbInspector renderSpec | 按住扳机 2 秒删除 | `deleteHold.progress` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| OrbInspector | `deselect` | Visual §5 OrbInspector renderSpec | 完成 | `selected.id` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_core` | Visual §5 LightOrbEntity renderSpec | conditional visual | `orb.color,brightness` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_glow` | Visual §5 LightOrbEntity renderSpec | conditional visual | `orb.kind,color,brightness` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_particles` | Visual §5 LightOrbEntity renderSpec | conditional visual | `preset.effect,quality` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_range` | Visual §5 LightOrbEntity renderSpec | 影响范围 2.0m | `orb.radius` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_selection` | Visual §5 LightOrbEntity renderSpec | 已选中 · 日落 | `orb.selected` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_hold` | Visual §5 LightOrbEntity renderSpec | 松开取消 · 删除 | `deleteHold.progress` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| LightOrbEntity | `orb_tether` | Visual §5 LightOrbEntity renderSpec | conditional drag line | `drag.current,lastValid` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `capture_status` | Visual §5 PhotoSetup renderSpec | 可保存到系统相册 | `capture.capability` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `frame_white` | Visual §5 PhotoSetup renderSpec | 简约白边 | `frameStyle.WHITE` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `frame_film` | Visual §5 PhotoSetup renderSpec | 胶片黑边 | `frameStyle.FILM` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `frame_instant` | Visual §5 PhotoSetup renderSpec | 拍立得质感 | `frameStyle.INSTANT` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `timer_segments` | Visual §5 PhotoSetup renderSpec | 3秒 / 5秒 / 10秒 | `countdownSeconds` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `photo_start` | Visual §5 PhotoSetup renderSpec | 开始摄影 | `capture.capability` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoSetup | `photo_cancel` | Visual §5 PhotoSetup renderSpec | 取消 | `static` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoExperience | `photo_frame` | Visual §5 PhotoExperience renderSpec | conditional frame | `frameStyle` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoExperience | `countdown_value` | Visual §5 PhotoExperience renderSpec | 3 | `countdownTick` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoExperience | `capture_success` | Visual §5 PhotoExperience renderSpec | 已保存到系统相册 | `CaptureResult.uri` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoExperience | `capture_error` | Visual §5 PhotoExperience renderSpec | 保存失败 · 返回设置重试 | `CaptureResult.error` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PhotoExperience | `editor_visibility_guard` | Visual §5 PhotoExperience renderSpec | conditional assertion | `panelVisible,rayVisible` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| RecoverySurface | `recovery_title` | Visual §5 RecoverySurface renderSpec | 清空全部 4 个光球？ | `issue.title` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| RecoverySurface | `recovery_icon` | Visual §5 RecoverySurface renderSpec | 警告 / 操作失败 | `issue.type` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| RecoverySurface | `recovery_body` | Visual §5 RecoverySurface renderSpec | 此操作无法撤销，已保存方案也会更新。 | `issue.detail` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| RecoverySurface | `recovery_preserved` | Visual §5 RecoverySurface renderSpec | 当前布局仍保留 | `issue.preservedData` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| RecoverySurface | `recovery_cancel` | Visual §5 RecoverySurface renderSpec | 取消 | `static` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| RecoverySurface | `recovery_primary` | Visual §5 RecoverySurface renderSpec | 清空 / 重试 / 返回入口 | `issue.action` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PlacementGuide | `placement_ray` | Visual §5 PlacementGuide renderSpec | conditional SDK ray | `rayVisible,activeController,pose` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PlacementGuide | `placement_reticle` | Visual §5 PlacementGuide renderSpec | conditional reticle | `placement.valid` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PlacementGuide | `placement_ghost` | Visual §5 PlacementGuide renderSpec | conditional ghost | `pendingRecipe,pose` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PlacementGuide | `placement_prompt` | Visual §5 PlacementGuide renderSpec | 扣动扳机放置 | `placement.valid` | conditional visuals render only in relevant state; label nodes remain inspectable | included |
| PlacementGuide | `placement_block_reason` | Visual §5 PlacementGuide renderSpec | 已达上限 / 跟踪不可用 / 已锁定 | `placement.reason` | conditional visuals render only in relevant state; label nodes remain inspectable | included |

### 2.3 dataBindings[] denominator

| Component | Source path | Target | normal sample | fallback/error | Type | Verdict |
|---|---|---|---|---|---|---|
| EntryGate | `CapabilityState.stageReady` | enter_stage.enabled / entry_mode | normal-valid | missing/invalid fallback | semantic | included |
| EntryGate | `LayoutSnapshot.orbs.size` | restore_summary.text | normal-valid | missing/invalid fallback | display-only | included |
| EntryGate | `LayoutSnapshot.invalidCount` | discard_invalid.visible | normal-valid | missing/invalid fallback | semantic | included |
| EntryGate | `LayoutSnapshot.schemaVersion` | compatibility warning | normal-valid | missing/invalid fallback | semantic | included |
| EntryGate | `LayoutSnapshot.savedAt` | restore timestamp | normal-valid | missing/invalid fallback | display-only | included |
| LightPalette | `PresetDefinition.id/name/effect` | option label/glyph | normal-valid | missing/invalid fallback | display-only | included |
| LightPalette | `InteractionState.pendingRecipe` | option.selected | normal-valid | missing/invalid fallback | semantic | included |
| LightPalette | `orbCount` | options.enabled | normal-valid | missing/invalid fallback | semantic | included |
| LightPalette | `layoutLocked` | options.enabled | normal-valid | missing/invalid fallback | semantic | included |
| LightPalette | `customColor` | custom swatch | normal-valid | missing/invalid fallback | display-only | included |
| WorkspaceGuardBar | `orbCount` | capacity/clear/palette gate | normal-valid | missing/invalid fallback | semantic | included |
| WorkspaceGuardBar | `layoutLocked` | switch/locked label | normal-valid | missing/invalid fallback | semantic | included |
| WorkspaceGuardBar | `PersistenceState` | save badge | normal-valid | missing/invalid fallback | semantic | included |
| WorkspaceGuardBar | `TrackingState` | tracking badge/actions | normal-valid | missing/invalid fallback | semantic | included |
| WorkspaceGuardBar | `CaptureCapability` | photo enabled | normal-valid | missing/invalid fallback | semantic | included |
| OrbInspector | `selected.id/kind` | identity/key | normal-valid | missing/invalid fallback | semantic | included |
| OrbInspector | `selected.color` | wheel/world core | normal-valid | missing/invalid fallback | display-only | included |
| OrbInspector | `selected.brightness` | slider/world intensity | normal-valid | missing/invalid fallback | display-only | included |
| OrbInspector | `selected.radius` | slider/world shell | normal-valid | missing/invalid fallback | display-only | included |
| OrbInspector | `InteractionState.mode` | edit/drag/delete UI | normal-valid | missing/invalid fallback | semantic | included |
| OrbInspector | `deleteHold.progress` | progress | normal-valid | missing/invalid fallback | semantic | included |
| LightOrbEntity | `LightOrb.position` | entity transform | normal-valid | missing/invalid fallback | semantic | included |
| LightOrbEntity | `LightOrb.color` | core/glow | normal-valid | missing/invalid fallback | display-only | included |
| LightOrbEntity | `LightOrb.brightness` | gain | normal-valid | missing/invalid fallback | display-only | included |
| LightOrbEntity | `LightOrb.radius` | range shell | normal-valid | missing/invalid fallback | display-only | included |
| LightOrbEntity | `LightOrb.kind` | effect/glyph/name | normal-valid | missing/invalid fallback | display-only | included |
| LightOrbEntity | `InteractionState` | rings/tether/hold/target | normal-valid | missing/invalid fallback | semantic | included |
| LightOrbEntity | `layoutLocked` | targetability/lock cue | normal-valid | missing/invalid fallback | semantic | included |
| PhotoSetup | `PhotoSession.frameStyle` | selected frame | normal-valid | missing/invalid fallback | semantic | included |
| PhotoSetup | `PhotoSession.countdownSeconds` | selected segment | normal-valid | missing/invalid fallback | semantic | included |
| PhotoSetup | `CaptureCapability.permission` | status/start | normal-valid | missing/invalid fallback | semantic | included |
| PhotoSetup | `CaptureCapability.publicPathAvailable` | start enabled | normal-valid | missing/invalid fallback | semantic | included |
| PhotoExperience | `PhotoSession.frameStyle` | frame | normal-valid | missing/invalid fallback | semantic | included |
| PhotoExperience | `PhotoSession.countdownTick` | numeral | normal-valid | missing/invalid fallback | display-only | included |
| PhotoExperience | `CaptureResult.status/uri/error` | result | normal-valid | missing/invalid fallback | semantic | included |
| PhotoExperience | `panelVisible` | editor guard | normal-valid | missing/invalid fallback | semantic | included |
| PhotoExperience | `controllerRayVisible` | ray guard | normal-valid | missing/invalid fallback | semantic | included |
| RecoverySurface | `RecoveryIssue.type` | title/icon/actions | normal-valid | missing/invalid fallback | semantic | included |
| RecoverySurface | `RecoveryIssue.detail` | body | normal-valid | missing/invalid fallback | display-only | included |
| RecoverySurface | `RecoveryIssue.preservedData` | preserved label | normal-valid | missing/invalid fallback | semantic | included |
| RecoverySurface | `orbCount` | clear title | normal-valid | missing/invalid fallback | display-only | included |
| RecoverySurface | `TrackingState/CaptureResult/PersistenceState` | issue mapping | normal-valid | missing/invalid fallback | semantic | included |
| PlacementGuide | `InteractionState.pendingRecipe` | ghost color/effect | normal-valid | missing/invalid fallback | display-only | included |
| PlacementGuide | `InteractionState.rayVisible/activeController` | ray visibility/source | normal-valid | missing/invalid fallback | semantic | included |
| PlacementGuide | `PlacementPose.position` | ray endpoint/transform | normal-valid | missing/invalid fallback | semantic | included |
| PlacementGuide | `PlacementPose.valid` | reticle/prompt/commit | normal-valid | missing/invalid fallback | semantic | included |
| PlacementGuide | `TrackingState` | validity/reason | normal-valid | missing/invalid fallback | semantic | included |
| PlacementGuide | `orbCount` | validity/reason | normal-valid | missing/invalid fallback | semantic | included |
| PlacementGuide | `layoutLocked` | validity/reason | normal-valid | missing/invalid fallback | semantic | included |

### 2.4 Variants / component-specific states / stacking denominator

| Component | Item | Source Fact Anchor | Trigger method | Expected observable change | Verdict |
|---|---|---|---|---|---|
| EntryGate | variant `firstRun` | Visual §5 EntryGate Variants | component-lab variant selector | `data-variant=firstRun` and variant label/visual change | included |
| EntryGate | variant `restoreAvailable` | Visual §5 EntryGate Variants | component-lab variant selector | `data-variant=restoreAvailable` and variant label/visual change | included |
| EntryGate | variant `restorePartial` | Visual §5 EntryGate Variants | component-lab variant selector | `data-variant=restorePartial` and variant label/visual change | included |
| LightPalette | variant `regularGrid` | Visual §5 LightPalette Variants | component-lab variant selector | `data-variant=regularGrid` and variant label/visual change | included |
| LightPalette | variant `compactRail` | Visual §5 LightPalette Variants | component-lab variant selector | `data-variant=compactRail` and variant label/visual change | included |
| LightPalette | variant `customPickerExpanded` | Visual §5 LightPalette Variants | component-lab variant selector | `data-variant=customPickerExpanded` and variant label/visual change | included |
| WorkspaceGuardBar | variant `workspace` | Visual §5 WorkspaceGuardBar Variants | component-lab variant selector | `data-variant=workspace` and variant label/visual change | included |
| WorkspaceGuardBar | variant `locked` | Visual §5 WorkspaceGuardBar Variants | component-lab variant selector | `data-variant=locked` and variant label/visual change | included |
| WorkspaceGuardBar | variant `compact` | Visual §5 WorkspaceGuardBar Variants | component-lab variant selector | `data-variant=compact` and variant label/visual change | included |
| WorkspaceGuardBar | variant `photoPrepHeader` | Visual §5 WorkspaceGuardBar Variants | component-lab variant selector | `data-variant=photoPrepHeader` and variant label/visual change | included |
| OrbInspector | variant `preset` | Visual §5 OrbInspector Variants | component-lab variant selector | `data-variant=preset` and variant label/visual change | included |
| OrbInspector | variant `custom` | Visual §5 OrbInspector Variants | component-lab variant selector | `data-variant=custom` and variant label/visual change | included |
| OrbInspector | variant `compactSwatches` | Visual §5 OrbInspector Variants | component-lab variant selector | `data-variant=compactSwatches` and variant label/visual change | included |
| OrbInspector | variant `draggingReadOnly` | Visual §5 OrbInspector Variants | component-lab variant selector | `data-variant=draggingReadOnly` and variant label/visual change | included |
| LightOrbEntity | variant `sunset` | Visual §5 LightOrbEntity Variants | component-lab variant selector | `data-variant=sunset` and variant label/visual change | included |
| LightOrbEntity | variant `moon` | Visual §5 LightOrbEntity Variants | component-lab variant selector | `data-variant=moon` and variant label/visual change | included |
| LightOrbEntity | variant `neon` | Visual §5 LightOrbEntity Variants | component-lab variant selector | `data-variant=neon` and variant label/visual change | included |
| LightOrbEntity | variant `stage` | Visual §5 LightOrbEntity Variants | component-lab variant selector | `data-variant=stage` and variant label/visual change | included |
| LightOrbEntity | variant `custom` | Visual §5 LightOrbEntity Variants | component-lab variant selector | `data-variant=custom` and variant label/visual change | included |
| PhotoSetup | variant `ready` | Visual §5 PhotoSetup Variants | component-lab variant selector | `data-variant=ready` and variant label/visual change | included |
| PhotoSetup | variant `permissionRequired` | Visual §5 PhotoSetup Variants | component-lab variant selector | `data-variant=permissionRequired` and variant label/visual change | included |
| PhotoSetup | variant `emulatorLimited` | Visual §5 PhotoSetup Variants | component-lab variant selector | `data-variant=emulatorLimited` and variant label/visual change | included |
| PhotoExperience | variant `whiteBorder` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=whiteBorder` and variant label/visual change | included |
| PhotoExperience | variant `filmBlackBorder` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=filmBlackBorder` and variant label/visual change | included |
| PhotoExperience | variant `instant` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=instant` and variant label/visual change | included |
| PhotoExperience | variant `ready` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=ready` and variant label/visual change | included |
| PhotoExperience | variant `countdown` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=countdown` and variant label/visual change | included |
| PhotoExperience | variant `success` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=success` and variant label/visual change | included |
| PhotoExperience | variant `failure` | Visual §5 PhotoExperience Variants | component-lab variant selector | `data-variant=failure` and variant label/visual change | included |
| RecoverySurface | variant `clearConfirm` | Visual §5 RecoverySurface Variants | component-lab variant selector | `data-variant=clearConfirm` and variant label/visual change | included |
| RecoverySurface | variant `trackingLost` | Visual §5 RecoverySurface Variants | component-lab variant selector | `data-variant=trackingLost` and variant label/visual change | included |
| RecoverySurface | variant `captureFailed` | Visual §5 RecoverySurface Variants | component-lab variant selector | `data-variant=captureFailed` and variant label/visual change | included |
| RecoverySurface | variant `permissionDenied` | Visual §5 RecoverySurface Variants | component-lab variant selector | `data-variant=permissionDenied` and variant label/visual change | included |
| RecoverySurface | variant `saveFailed` | Visual §5 RecoverySurface Variants | component-lab variant selector | `data-variant=saveFailed` and variant label/visual change | included |
| RecoverySurface | variant `invalidRestore` | Visual §5 RecoverySurface Variants | component-lab variant selector | `data-variant=invalidRestore` and variant label/visual change | included |
| PlacementGuide | variant `sunset` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=sunset` and variant label/visual change | included |
| PlacementGuide | variant `moon` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=moon` and variant label/visual change | included |
| PlacementGuide | variant `neon` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=neon` and variant label/visual change | included |
| PlacementGuide | variant `stage` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=stage` and variant label/visual change | included |
| PlacementGuide | variant `custom` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=custom` and variant label/visual change | included |
| PlacementGuide | variant `valid` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=valid` and variant label/visual change | included |
| PlacementGuide | variant `trackingBlocked` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=trackingBlocked` and variant label/visual change | included |
| PlacementGuide | variant `capacityBlocked` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=capacityBlocked` and variant label/visual change | included |
| PlacementGuide | variant `lockBlocked` | Visual §5 PlacementGuide Variants | component-lab variant selector | `data-variant=lockBlocked` and variant label/visual change | included |
| EntryGate | state `default` | Visual §5 EntryGate States | component-lab state selector | baseline component presentation | included |
| EntryGate | state `hover` | Visual §5 EntryGate States | component-lab state selector | focus outline/hover response | included |
| EntryGate | state `pressed` | Visual §5 EntryGate States | component-lab state selector | press feedback/haptic semantics | included |
| EntryGate | state `disabled` | Visual §5 EntryGate States | component-lab state selector | disabled style plus reason | included |
| EntryGate | state `loading` | Visual §5 EntryGate States | component-lab state selector | progress plus retained context | included |
| EntryGate | state `empty` | Visual §5 EntryGate States | component-lab state selector | guidance or conditional hide | included |
| EntryGate | state `error` | Visual §5 EntryGate States | component-lab state selector | source-specific error and recovery | included |
| EntryGate | state `overflow` | Visual §5 EntryGate States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| LightPalette | state `default` | Visual §5 LightPalette States | component-lab state selector | baseline component presentation | included |
| LightPalette | state `hover` | Visual §5 LightPalette States | component-lab state selector | focus outline/hover response | included |
| LightPalette | state `selected` | Visual §5 LightPalette States | component-lab state selector | selected shape+text | included |
| LightPalette | state `disabled` | Visual §5 LightPalette States | component-lab state selector | disabled style plus reason | included |
| LightPalette | state `loading` | Visual §5 LightPalette States | component-lab state selector | progress plus retained context | included |
| LightPalette | state `empty` | Visual §5 LightPalette States | component-lab state selector | guidance or conditional hide | included |
| LightPalette | state `error` | Visual §5 LightPalette States | component-lab state selector | source-specific error and recovery | included |
| LightPalette | state `overflow` | Visual §5 LightPalette States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| WorkspaceGuardBar | state `default` | Visual §5 WorkspaceGuardBar States | component-lab state selector | baseline component presentation | included |
| WorkspaceGuardBar | state `hover` | Visual §5 WorkspaceGuardBar States | component-lab state selector | focus outline/hover response | included |
| WorkspaceGuardBar | state `pressed` | Visual §5 WorkspaceGuardBar States | component-lab state selector | press feedback/haptic semantics | included |
| WorkspaceGuardBar | state `disabled` | Visual §5 WorkspaceGuardBar States | component-lab state selector | disabled style plus reason | included |
| WorkspaceGuardBar | state `loading` | Visual §5 WorkspaceGuardBar States | component-lab state selector | progress plus retained context | included |
| WorkspaceGuardBar | state `empty` | Visual §5 WorkspaceGuardBar States | component-lab state selector | guidance or conditional hide | included |
| WorkspaceGuardBar | state `error` | Visual §5 WorkspaceGuardBar States | component-lab state selector | source-specific error and recovery | included |
| WorkspaceGuardBar | state `overflow` | Visual §5 WorkspaceGuardBar States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| OrbInspector | state `default` | Visual §5 OrbInspector States | component-lab state selector | baseline component presentation | included |
| OrbInspector | state `hover` | Visual §5 OrbInspector States | component-lab state selector | focus outline/hover response | included |
| OrbInspector | state `pressed` | Visual §5 OrbInspector States | component-lab state selector | press feedback/haptic semantics | included |
| OrbInspector | state `editing` | Visual §5 OrbInspector States | component-lab state selector | live value + active control | included |
| OrbInspector | state `dragging` | Visual §5 OrbInspector States | component-lab state selector | tether/read-only controls/live pose | included |
| OrbInspector | state `delete-armed` | Visual §5 OrbInspector States | component-lab state selector | 2s progress + release-to-cancel | included |
| OrbInspector | state `disabled` | Visual §5 OrbInspector States | component-lab state selector | disabled style plus reason | included |
| OrbInspector | state `loading` | Visual §5 OrbInspector States | component-lab state selector | progress plus retained context | included |
| OrbInspector | state `empty` | Visual §5 OrbInspector States | component-lab state selector | guidance or conditional hide | included |
| OrbInspector | state `error` | Visual §5 OrbInspector States | component-lab state selector | source-specific error and recovery | included |
| OrbInspector | state `overflow` | Visual §5 OrbInspector States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| LightOrbEntity | state `default` | Visual §5 LightOrbEntity States | component-lab state selector | baseline component presentation | included |
| LightOrbEntity | state `hover` | Visual §5 LightOrbEntity States | component-lab state selector | focus outline/hover response | included |
| LightOrbEntity | state `selected` | Visual §5 LightOrbEntity States | component-lab state selector | selected shape+text | included |
| LightOrbEntity | state `dragging` | Visual §5 LightOrbEntity States | component-lab state selector | tether/read-only controls/live pose | included |
| LightOrbEntity | state `delete-armed` | Visual §5 LightOrbEntity States | component-lab state selector | 2s progress + release-to-cancel | included |
| LightOrbEntity | state `disabled` | Visual §5 LightOrbEntity States | component-lab state selector | disabled style plus reason | included |
| LightOrbEntity | state `loading` | Visual §5 LightOrbEntity States | component-lab state selector | progress plus retained context | included |
| LightOrbEntity | state `empty` | Visual §5 LightOrbEntity States | component-lab state selector | guidance or conditional hide | included |
| LightOrbEntity | state `error` | Visual §5 LightOrbEntity States | component-lab state selector | source-specific error and recovery | included |
| LightOrbEntity | state `overflow` | Visual §5 LightOrbEntity States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| PhotoSetup | state `default` | Visual §5 PhotoSetup States | component-lab state selector | baseline component presentation | included |
| PhotoSetup | state `hover` | Visual §5 PhotoSetup States | component-lab state selector | focus outline/hover response | included |
| PhotoSetup | state `selected` | Visual §5 PhotoSetup States | component-lab state selector | selected shape+text | included |
| PhotoSetup | state `disabled` | Visual §5 PhotoSetup States | component-lab state selector | disabled style plus reason | included |
| PhotoSetup | state `loading` | Visual §5 PhotoSetup States | component-lab state selector | progress plus retained context | included |
| PhotoSetup | state `empty` | Visual §5 PhotoSetup States | component-lab state selector | guidance or conditional hide | included |
| PhotoSetup | state `error` | Visual §5 PhotoSetup States | component-lab state selector | source-specific error and recovery | included |
| PhotoSetup | state `overflow` | Visual §5 PhotoSetup States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| PhotoExperience | state `default` | Visual §5 PhotoExperience States | component-lab state selector | baseline component presentation | included |
| PhotoExperience | state `countdown` | Visual §5 PhotoExperience States | component-lab state selector | fixed-center numeral and hidden editor/ray | included |
| PhotoExperience | state `capturing` | Visual §5 PhotoExperience States | component-lab state selector | progress without flash | included |
| PhotoExperience | state `success` | Visual §5 PhotoExperience States | component-lab state selector | saved check + human label | included |
| PhotoExperience | state `hover` | Visual §5 PhotoExperience States | component-lab state selector | focus outline/hover response | included |
| PhotoExperience | state `pressed` | Visual §5 PhotoExperience States | component-lab state selector | press feedback/haptic semantics | included |
| PhotoExperience | state `disabled` | Visual §5 PhotoExperience States | component-lab state selector | disabled style plus reason | included |
| PhotoExperience | state `loading` | Visual §5 PhotoExperience States | component-lab state selector | progress plus retained context | included |
| PhotoExperience | state `empty` | Visual §5 PhotoExperience States | component-lab state selector | guidance or conditional hide | included |
| PhotoExperience | state `error` | Visual §5 PhotoExperience States | component-lab state selector | source-specific error and recovery | included |
| PhotoExperience | state `overflow` | Visual §5 PhotoExperience States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| RecoverySurface | state `default` | Visual §5 RecoverySurface States | component-lab state selector | baseline component presentation | included |
| RecoverySurface | state `hover` | Visual §5 RecoverySurface States | component-lab state selector | focus outline/hover response | included |
| RecoverySurface | state `pressed` | Visual §5 RecoverySurface States | component-lab state selector | press feedback/haptic semantics | included |
| RecoverySurface | state `disabled` | Visual §5 RecoverySurface States | component-lab state selector | disabled style plus reason | included |
| RecoverySurface | state `loading` | Visual §5 RecoverySurface States | component-lab state selector | progress plus retained context | included |
| RecoverySurface | state `empty` | Visual §5 RecoverySurface States | component-lab state selector | guidance or conditional hide | included |
| RecoverySurface | state `error` | Visual §5 RecoverySurface States | component-lab state selector | source-specific error and recovery | included |
| RecoverySurface | state `overflow` | Visual §5 RecoverySurface States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| PlacementGuide | state `default` | Visual §5 PlacementGuide States | component-lab state selector | baseline component presentation | included |
| PlacementGuide | state `hover` | Visual §5 PlacementGuide States | component-lab state selector | focus outline/hover response | included |
| PlacementGuide | state `pressed` | Visual §5 PlacementGuide States | component-lab state selector | press feedback/haptic semantics | included |
| PlacementGuide | state `disabled` | Visual §5 PlacementGuide States | component-lab state selector | disabled style plus reason | included |
| PlacementGuide | state `loading` | Visual §5 PlacementGuide States | component-lab state selector | progress plus retained context | included |
| PlacementGuide | state `empty` | Visual §5 PlacementGuide States | component-lab state selector | guidance or conditional hide | included |
| PlacementGuide | state `error` | Visual §5 PlacementGuide States | component-lab state selector | source-specific error and recovery | included |
| PlacementGuide | state `overflow` | Visual §5 PlacementGuide States | component-lab state selector | reflow/scroll/truncation without target shrink | included |
| EntryGate | stacking precedence | Visual §5 EntryGate States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| LightPalette | stacking precedence | Visual §5 LightPalette States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| WorkspaceGuardBar | stacking precedence | Visual §5 WorkspaceGuardBar States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| OrbInspector | stacking precedence | Visual §5 OrbInspector States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| LightOrbEntity | stacking precedence | Visual §5 LightOrbEntity States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| PhotoSetup | stacking precedence | Visual §5 PhotoSetup States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| PhotoExperience | stacking precedence | Visual §5 PhotoExperience States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| RecoverySurface | stacking precedence | Visual §5 RecoverySurface States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |
| PlacementGuide | stacking precedence | Visual §5 PlacementGuide States | choose stacked state in lab | precedence banner and dominant visual match component rule | included |

### 2.5 Responsive / Reduce Motion denominator

| Scenario | Source | Window/content mapping | Trigger | Expected result | Verdict |
|---|---|---|---|---|---|
| Large | Interaction §5.4/§7.2; Visual §5 | max 920×640 / safe 888×512 | responsive selector | 68+16+428 regions, capped component widths, optional two-column content | included |
| Compact | same | default 720×540 / safe 688×412 | responsive selector | 68+16+328 regions, 2×2 palette/default inspector | included |
| Constrained | same | min 520×380 / safe 488×252 | responsive selector | 56+8+188 regions, horizontal chips/internal scroll/in-row overflow | included |
| Reduce Motion | Interaction §7.4–§7.5 | N/A | Reduce Motion toggle | remove scale/pulse/scan/drift; retain opacity/outline/text/state semantics | included |

## 3. Generation implementation mapping

### 3.1 States / transitions → DOM

| Source item | Trigger | Stable selector | Expected result | Generation status |
|---|---|---|---|---|
| `S0_SHARED_ENTRY` | state selector | `[data-state="S0_SHARED_ENTRY"]` / `#scene` | Shared entry, restore summary, enter action | mapped |
| `S1_ROOM_IDLE` | state selector | `[data-state="S1_ROOM_IDLE"]` / `#scene` | room orbs + palette + guard | mapped |
| `S2_PLACEMENT_AIM` | state selector | `[data-state="S2_PLACEMENT_AIM"]` / `#scene` | ray, reticle, ghost and commit gate | mapped |
| `S3_ORB_SELECTED` | state selector | `[data-state="S3_ORB_SELECTED"]` / `#scene` | selected rings + inspector | mapped |
| `S4_ORB_DRAGGING` | state selector | `[data-state="S4_ORB_DRAGGING"]` / `#scene` | tether and live pose | mapped |
| `S5_LAYOUT_LOCKED` | state selector | `[data-state="S5_LAYOUT_LOCKED"]` / `#scene` | lock label and manipulation disabled | mapped |
| `S6_CLEAR_CONFIRM` | state selector | `[data-state="S6_CLEAR_CONFIRM"]` / `#scene` | blocking clear dialog | mapped |
| `S7_PHOTO_PREP` | state selector | `[data-state="S7_PHOTO_PREP"]` / `#scene` | frame/timer choices and start | mapped |
| `S8_PHOTO_READY` | state selector | `[data-state="S8_PHOTO_READY"]` / `#scene` | clean pre-roll, no editor/ray | mapped |
| `S9_PHOTO_COUNTDOWN` | state selector | `[data-state="S9_PHOTO_COUNTDOWN"]` / `#scene` | frame + center countdown | mapped |
| `S10_CAPTURE_RESULT` | state selector | `[data-state="S10_CAPTURE_RESULT"]` / `#scene` | saved/failure result | mapped |
| `S11_TRACKING_RECOVERY` | state selector | `[data-state="S11_TRACKING_RECOVERY"]` / `#scene` | frozen layout + retry/return | mapped |
| `TR-ENTER` | transition button | `[data-transition="TR-ENTER"]` | S0→S1 | mapped |
| `TR-PLACE-AIM` | transition button | `[data-transition="TR-PLACE-AIM"]` | S1→S2 | mapped |
| `TR-PLACE-COMMIT` | transition button | `[data-transition="TR-PLACE-COMMIT"]` | S2→S1 | mapped |
| `TR-SELECT` | transition button | `[data-transition="TR-SELECT"]` | S1→S3 | mapped |
| `TR-DRAG` | transition button | `[data-transition="TR-DRAG"]` | S3→S4 | mapped |
| `TR-DRAG-END` | transition button | `[data-transition="TR-DRAG-END"]` | S4→S3 | mapped |
| `TR-HOLD-DELETE` | transition button | `[data-transition="TR-HOLD-DELETE"]` | S3→S1 | mapped |
| `TR-LOCK` | transition button | `[data-transition="TR-LOCK"]` | S1/S3→S5 | mapped |
| `TR-UNLOCK` | transition button | `[data-transition="TR-UNLOCK"]` | S5→S1 | mapped |
| `TR-CLEAR-OPEN` | transition button | `[data-transition="TR-CLEAR-OPEN"]` | S1/S3→S6 | mapped |
| `TR-CLEAR-CONFIRM` | transition button | `[data-transition="TR-CLEAR-CONFIRM"]` | S6→S1 | mapped |
| `TR-PHOTO-PREP` | transition button | `[data-transition="TR-PHOTO-PREP"]` | S1/S5→S7 | mapped |
| `TR-PHOTO-READY` | transition button | `[data-transition="TR-PHOTO-READY"]` | S7→S8 | mapped |
| `TR-COUNTDOWN` | transition button | `[data-transition="TR-COUNTDOWN"]` | S8→S9 | mapped |
| `TR-CAPTURE` | transition button | `[data-transition="TR-CAPTURE"]` | S9→S10 | mapped |
| `TR-PHOTO-EXIT` | transition button | `[data-transition="TR-PHOTO-EXIT"]` | S8/S9/S10→S1/S5 | mapped |
| `TR-PANEL` | transition button | `[data-transition="TR-PANEL"]` | same | mapped |
| `TR-TRACKING-LOST` | transition button | `[data-transition="TR-TRACKING-LOST"]` | workspace/photo→S11 | mapped |
| `TR-STAGE-EXIT` | transition button | `[data-transition="TR-STAGE-EXIT"]` | S1–S11→S0 | mapped |

### 3.2 renderSpec → DOM

| Element | Selector | Visible/conditional result | Generation status |
|---|---|---|---|
| EntryGate.`entry_title` | `[data-preview-id="entry_title"]` | 房间灯光师 / state-aware | mapped |
| EntryGate.`entry_mode` | `[data-preview-id="entry_mode"]` | 混合现实 · 真实房间可见 / state-aware | mapped |
| EntryGate.`restore_summary` | `[data-preview-id="restore_summary"]` | 将恢复最近的布光方案 / state-aware | mapped |
| EntryGate.`enter_stage` | `[data-preview-id="enter_stage"]` | 进入房间布光 / state-aware | mapped |
| EntryGate.`entry_exit_help` | `[data-preview-id="entry_exit_help"]` | 退出 / 使用说明 / state-aware | mapped |
| EntryGate.`discard_invalid` | `[data-preview-id="discard_invalid"]` | 忽略无效记录 / state-aware | mapped |
| LightPalette.`preset_sunset` | `[data-preview-id="preset_sunset"]` | 日落 · 暖雾 / state-aware | mapped |
| LightPalette.`preset_moon` | `[data-preview-id="preset_moon"]` | 月光 · 星尘 / state-aware | mapped |
| LightPalette.`preset_neon` | `[data-preview-id="preset_neon"]` | 霓虹 · 扫描 / state-aware | mapped |
| LightPalette.`preset_stage` | `[data-preview-id="preset_stage"]` | 舞台 · 聚光 / state-aware | mapped |
| LightPalette.`preset_custom` | `[data-preview-id="preset_custom"]` | 自定义颜色 / state-aware | mapped |
| WorkspaceGuardBar.`orb_capacity` | `[data-preview-id="orb_capacity"]` | 0/8 光球 / state-aware | mapped |
| WorkspaceGuardBar.`save_state` | `[data-preview-id="save_state"]` | 已自动保存 / state-aware | mapped |
| WorkspaceGuardBar.`tracking_state` | `[data-preview-id="tracking_state"]` | 空间稳定 / state-aware | mapped |
| WorkspaceGuardBar.`layout_lock` | `[data-preview-id="layout_lock"]` | 锁定布局 / state-aware | mapped |
| WorkspaceGuardBar.`clear_all` | `[data-preview-id="clear_all"]` | 清空 / state-aware | mapped |
| WorkspaceGuardBar.`photo_entry` | `[data-preview-id="photo_entry"]` | 摄影模式 / state-aware | mapped |
| OrbInspector.`orb_identity` | `[data-preview-id="orb_identity"]` | 已选中 · 日落 / state-aware | mapped |
| OrbInspector.`color_control` | `[data-preview-id="color_control"]` | 颜色 / state-aware | mapped |
| OrbInspector.`brightness_control` | `[data-preview-id="brightness_control"]` | 亮度 70% / state-aware | mapped |
| OrbInspector.`radius_control` | `[data-preview-id="radius_control"]` | 范围 2.0m / state-aware | mapped |
| OrbInspector.`hold_delete_hint` | `[data-preview-id="hold_delete_hint"]` | 按住扳机 2 秒删除 / state-aware | mapped |
| OrbInspector.`deselect` | `[data-preview-id="deselect"]` | 完成 / state-aware | mapped |
| LightOrbEntity.`orb_core` | `[data-preview-id="orb_core"]` | conditional visual / state-aware | mapped |
| LightOrbEntity.`orb_glow` | `[data-preview-id="orb_glow"]` | conditional visual / state-aware | mapped |
| LightOrbEntity.`orb_particles` | `[data-preview-id="orb_particles"]` | conditional visual / state-aware | mapped |
| LightOrbEntity.`orb_range` | `[data-preview-id="orb_range"]` | 影响范围 2.0m / state-aware | mapped |
| LightOrbEntity.`orb_selection` | `[data-preview-id="orb_selection"]` | 已选中 · 日落 / state-aware | mapped |
| LightOrbEntity.`orb_hold` | `[data-preview-id="orb_hold"]` | 松开取消 · 删除 / state-aware | mapped |
| LightOrbEntity.`orb_tether` | `[data-preview-id="orb_tether"]` | conditional drag line / state-aware | mapped |
| PhotoSetup.`capture_status` | `[data-preview-id="capture_status"]` | 可保存到系统相册 / state-aware | mapped |
| PhotoSetup.`frame_white` | `[data-preview-id="frame_white"]` | 简约白边 / state-aware | mapped |
| PhotoSetup.`frame_film` | `[data-preview-id="frame_film"]` | 胶片黑边 / state-aware | mapped |
| PhotoSetup.`frame_instant` | `[data-preview-id="frame_instant"]` | 拍立得质感 / state-aware | mapped |
| PhotoSetup.`timer_segments` | `[data-preview-id="timer_segments"]` | 3秒 / 5秒 / 10秒 / state-aware | mapped |
| PhotoSetup.`photo_start` | `[data-preview-id="photo_start"]` | 开始摄影 / state-aware | mapped |
| PhotoSetup.`photo_cancel` | `[data-preview-id="photo_cancel"]` | 取消 / state-aware | mapped |
| PhotoExperience.`photo_frame` | `[data-preview-id="photo_frame"]` | conditional frame / state-aware | mapped |
| PhotoExperience.`countdown_value` | `[data-preview-id="countdown_value"]` | 3 / state-aware | mapped |
| PhotoExperience.`capture_success` | `[data-preview-id="capture_success"]` | 已保存到系统相册 / state-aware | mapped |
| PhotoExperience.`capture_error` | `[data-preview-id="capture_error"]` | 保存失败 · 返回设置重试 / state-aware | mapped |
| PhotoExperience.`editor_visibility_guard` | `[data-preview-id="editor_visibility_guard"]` | conditional assertion / state-aware | mapped |
| RecoverySurface.`recovery_title` | `[data-preview-id="recovery_title"]` | 清空全部 4 个光球？ / state-aware | mapped |
| RecoverySurface.`recovery_icon` | `[data-preview-id="recovery_icon"]` | 警告 / 操作失败 / state-aware | mapped |
| RecoverySurface.`recovery_body` | `[data-preview-id="recovery_body"]` | 此操作无法撤销，已保存方案也会更新。 / state-aware | mapped |
| RecoverySurface.`recovery_preserved` | `[data-preview-id="recovery_preserved"]` | 当前布局仍保留 / state-aware | mapped |
| RecoverySurface.`recovery_cancel` | `[data-preview-id="recovery_cancel"]` | 取消 / state-aware | mapped |
| RecoverySurface.`recovery_primary` | `[data-preview-id="recovery_primary"]` | 清空 / 重试 / 返回入口 / state-aware | mapped |
| PlacementGuide.`placement_ray` | `[data-preview-id="placement_ray"]` | conditional SDK ray / state-aware | mapped |
| PlacementGuide.`placement_reticle` | `[data-preview-id="placement_reticle"]` | conditional reticle / state-aware | mapped |
| PlacementGuide.`placement_ghost` | `[data-preview-id="placement_ghost"]` | conditional ghost / state-aware | mapped |
| PlacementGuide.`placement_prompt` | `[data-preview-id="placement_prompt"]` | 扣动扳机放置 / state-aware | mapped |
| PlacementGuide.`placement_block_reason` | `[data-preview-id="placement_block_reason"]` | 已达上限 / 跟踪不可用 / 已锁定 / state-aware | mapped |

### 3.3 Bindings → normal/fallback controls

| Binding | Selector | Normal trigger | Fallback trigger | Generation status |
|---|---|---|---|---|
| EntryGate.`CapabilityState.stageReady` | `[data-binding="EntryGate:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| EntryGate.`LayoutSnapshot.orbs.size` | `[data-binding="EntryGate:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| EntryGate.`LayoutSnapshot.invalidCount` | `[data-binding="EntryGate:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| EntryGate.`LayoutSnapshot.schemaVersion` | `[data-binding="EntryGate:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| EntryGate.`LayoutSnapshot.savedAt` | `[data-binding="EntryGate:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightPalette.`PresetDefinition.id/name/effect` | `[data-binding="LightPalette:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightPalette.`InteractionState.pendingRecipe` | `[data-binding="LightPalette:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightPalette.`orbCount` | `[data-binding="LightPalette:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightPalette.`layoutLocked` | `[data-binding="LightPalette:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightPalette.`customColor` | `[data-binding="LightPalette:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| WorkspaceGuardBar.`orbCount` | `[data-binding="WorkspaceGuardBar:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| WorkspaceGuardBar.`layoutLocked` | `[data-binding="WorkspaceGuardBar:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| WorkspaceGuardBar.`PersistenceState` | `[data-binding="WorkspaceGuardBar:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| WorkspaceGuardBar.`TrackingState` | `[data-binding="WorkspaceGuardBar:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| WorkspaceGuardBar.`CaptureCapability` | `[data-binding="WorkspaceGuardBar:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| OrbInspector.`selected.id/kind` | `[data-binding="OrbInspector:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| OrbInspector.`selected.color` | `[data-binding="OrbInspector:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| OrbInspector.`selected.brightness` | `[data-binding="OrbInspector:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| OrbInspector.`selected.radius` | `[data-binding="OrbInspector:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| OrbInspector.`InteractionState.mode` | `[data-binding="OrbInspector:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| OrbInspector.`deleteHold.progress` | `[data-binding="OrbInspector:5"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`LightOrb.position` | `[data-binding="LightOrbEntity:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`LightOrb.color` | `[data-binding="LightOrbEntity:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`LightOrb.brightness` | `[data-binding="LightOrbEntity:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`LightOrb.radius` | `[data-binding="LightOrbEntity:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`LightOrb.kind` | `[data-binding="LightOrbEntity:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`InteractionState` | `[data-binding="LightOrbEntity:5"]` + bound element | data mode normal | fallback/error data modes | mapped |
| LightOrbEntity.`layoutLocked` | `[data-binding="LightOrbEntity:6"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoSetup.`PhotoSession.frameStyle` | `[data-binding="PhotoSetup:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoSetup.`PhotoSession.countdownSeconds` | `[data-binding="PhotoSetup:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoSetup.`CaptureCapability.permission` | `[data-binding="PhotoSetup:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoSetup.`CaptureCapability.publicPathAvailable` | `[data-binding="PhotoSetup:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoExperience.`PhotoSession.frameStyle` | `[data-binding="PhotoExperience:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoExperience.`PhotoSession.countdownTick` | `[data-binding="PhotoExperience:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoExperience.`CaptureResult.status/uri/error` | `[data-binding="PhotoExperience:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoExperience.`panelVisible` | `[data-binding="PhotoExperience:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PhotoExperience.`controllerRayVisible` | `[data-binding="PhotoExperience:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| RecoverySurface.`RecoveryIssue.type` | `[data-binding="RecoverySurface:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| RecoverySurface.`RecoveryIssue.detail` | `[data-binding="RecoverySurface:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| RecoverySurface.`RecoveryIssue.preservedData` | `[data-binding="RecoverySurface:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| RecoverySurface.`orbCount` | `[data-binding="RecoverySurface:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| RecoverySurface.`TrackingState/CaptureResult/PersistenceState` | `[data-binding="RecoverySurface:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`InteractionState.pendingRecipe` | `[data-binding="PlacementGuide:0"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`InteractionState.rayVisible/activeController` | `[data-binding="PlacementGuide:1"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`PlacementPose.position` | `[data-binding="PlacementGuide:2"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`PlacementPose.valid` | `[data-binding="PlacementGuide:3"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`TrackingState` | `[data-binding="PlacementGuide:4"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`orbCount` | `[data-binding="PlacementGuide:5"]` + bound element | data mode normal | fallback/error data modes | mapped |
| PlacementGuide.`layoutLocked` | `[data-binding="PlacementGuide:6"]` + bound element | data mode normal | fallback/error data modes | mapped |

### 3.4 Variants / states → component lab

| Component item | Trigger | Observable | Generation status |
|---|---|---|---|
| EntryGate variant `firstRun` | component + variant selectors | data-variant + visual label | mapped |
| EntryGate variant `restoreAvailable` | component + variant selectors | data-variant + visual label | mapped |
| EntryGate variant `restorePartial` | component + variant selectors | data-variant + visual label | mapped |
| EntryGate state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| EntryGate state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| EntryGate state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped |
| EntryGate state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| EntryGate state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| EntryGate state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| EntryGate state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| EntryGate state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| EntryGate stacking | stacked-state selector | precedence banner | mapped |
| LightPalette variant `regularGrid` | component + variant selectors | data-variant + visual label | mapped |
| LightPalette variant `compactRail` | component + variant selectors | data-variant + visual label | mapped |
| LightPalette variant `customPickerExpanded` | component + variant selectors | data-variant + visual label | mapped |
| LightPalette state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| LightPalette state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| LightPalette state `selected` | component + state selectors | data-component-state + selected shape+text | mapped |
| LightPalette state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| LightPalette state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| LightPalette state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| LightPalette state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| LightPalette state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| LightPalette stacking | stacked-state selector | precedence banner | mapped |
| WorkspaceGuardBar variant `workspace` | component + variant selectors | data-variant + visual label | mapped |
| WorkspaceGuardBar variant `locked` | component + variant selectors | data-variant + visual label | mapped |
| WorkspaceGuardBar variant `compact` | component + variant selectors | data-variant + visual label | mapped |
| WorkspaceGuardBar variant `photoPrepHeader` | component + variant selectors | data-variant + visual label | mapped |
| WorkspaceGuardBar state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| WorkspaceGuardBar state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| WorkspaceGuardBar state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped |
| WorkspaceGuardBar state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| WorkspaceGuardBar state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| WorkspaceGuardBar state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| WorkspaceGuardBar state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| WorkspaceGuardBar state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| WorkspaceGuardBar stacking | stacked-state selector | precedence banner | mapped |
| OrbInspector variant `preset` | component + variant selectors | data-variant + visual label | mapped |
| OrbInspector variant `custom` | component + variant selectors | data-variant + visual label | mapped |
| OrbInspector variant `compactSwatches` | component + variant selectors | data-variant + visual label | mapped |
| OrbInspector variant `draggingReadOnly` | component + variant selectors | data-variant + visual label | mapped |
| OrbInspector state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| OrbInspector state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| OrbInspector state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped |
| OrbInspector state `editing` | component + state selectors | data-component-state + live value + active control | mapped |
| OrbInspector state `dragging` | component + state selectors | data-component-state + tether/read-only controls/live pose | mapped |
| OrbInspector state `delete-armed` | component + state selectors | data-component-state + 2s progress + release-to-cancel | mapped |
| OrbInspector state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| OrbInspector state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| OrbInspector state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| OrbInspector state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| OrbInspector state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| OrbInspector stacking | stacked-state selector | precedence banner | mapped |
| LightOrbEntity variant `sunset` | component + variant selectors | data-variant + visual label | mapped |
| LightOrbEntity variant `moon` | component + variant selectors | data-variant + visual label | mapped |
| LightOrbEntity variant `neon` | component + variant selectors | data-variant + visual label | mapped |
| LightOrbEntity variant `stage` | component + variant selectors | data-variant + visual label | mapped |
| LightOrbEntity variant `custom` | component + variant selectors | data-variant + visual label | mapped |
| LightOrbEntity state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| LightOrbEntity state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| LightOrbEntity state `selected` | component + state selectors | data-component-state + selected shape+text | mapped |
| LightOrbEntity state `dragging` | component + state selectors | data-component-state + tether/read-only controls/live pose | mapped |
| LightOrbEntity state `delete-armed` | component + state selectors | data-component-state + 2s progress + release-to-cancel | mapped |
| LightOrbEntity state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| LightOrbEntity state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| LightOrbEntity state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| LightOrbEntity state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| LightOrbEntity state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| LightOrbEntity stacking | stacked-state selector | precedence banner | mapped |
| PhotoSetup variant `ready` | component + variant selectors | data-variant + visual label | mapped |
| PhotoSetup variant `permissionRequired` | component + variant selectors | data-variant + visual label | mapped |
| PhotoSetup variant `emulatorLimited` | component + variant selectors | data-variant + visual label | mapped |
| PhotoSetup state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| PhotoSetup state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| PhotoSetup state `selected` | component + state selectors | data-component-state + selected shape+text | mapped |
| PhotoSetup state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| PhotoSetup state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| PhotoSetup state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| PhotoSetup state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| PhotoSetup state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| PhotoSetup stacking | stacked-state selector | precedence banner | mapped |
| PhotoExperience variant `whiteBorder` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience variant `filmBlackBorder` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience variant `instant` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience variant `ready` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience variant `countdown` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience variant `success` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience variant `failure` | component + variant selectors | data-variant + visual label | mapped |
| PhotoExperience state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| PhotoExperience state `countdown` | component + state selectors | data-component-state + fixed-center numeral and hidden editor/ray | mapped |
| PhotoExperience state `capturing` | component + state selectors | data-component-state + progress without flash | mapped |
| PhotoExperience state `success` | component + state selectors | data-component-state + saved check + human label | mapped |
| PhotoExperience state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| PhotoExperience state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped |
| PhotoExperience state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| PhotoExperience state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| PhotoExperience state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| PhotoExperience state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| PhotoExperience state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| PhotoExperience stacking | stacked-state selector | precedence banner | mapped |
| RecoverySurface variant `clearConfirm` | component + variant selectors | data-variant + visual label | mapped |
| RecoverySurface variant `trackingLost` | component + variant selectors | data-variant + visual label | mapped |
| RecoverySurface variant `captureFailed` | component + variant selectors | data-variant + visual label | mapped |
| RecoverySurface variant `permissionDenied` | component + variant selectors | data-variant + visual label | mapped |
| RecoverySurface variant `saveFailed` | component + variant selectors | data-variant + visual label | mapped |
| RecoverySurface variant `invalidRestore` | component + variant selectors | data-variant + visual label | mapped |
| RecoverySurface state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| RecoverySurface state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| RecoverySurface state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped |
| RecoverySurface state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| RecoverySurface state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| RecoverySurface state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| RecoverySurface state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| RecoverySurface state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| RecoverySurface stacking | stacked-state selector | precedence banner | mapped |
| PlacementGuide variant `sunset` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `moon` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `neon` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `stage` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `custom` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `valid` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `trackingBlocked` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `capacityBlocked` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide variant `lockBlocked` | component + variant selectors | data-variant + visual label | mapped |
| PlacementGuide state `default` | component + state selectors | data-component-state + baseline component presentation | mapped |
| PlacementGuide state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped |
| PlacementGuide state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped |
| PlacementGuide state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped |
| PlacementGuide state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped |
| PlacementGuide state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped |
| PlacementGuide state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped |
| PlacementGuide state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped |
| PlacementGuide stacking | stacked-state selector | precedence banner | mapped |

### 3.5 Responsive / Reduce Motion → reflow

| Scenario | Selector/trigger | Structural assertion | Generation status |
|---|---|---|---|
| Large | `[data-responsive="Large"]` | max safe-area class, wider primary region/two-column allowance | mapped |
| Compact | `[data-responsive="Compact"]` | default safe-area class and default grid | mapped |
| Constrained | `[data-responsive="Constrained"]` | horizontal chips/internal scroll/in-row overflow; no global scale | mapped |
| Reduce Motion | `[data-reduce-motion]` | no scale/pulse/particle/scan animation; semantic outline/text retained | mapped |

## 3A. Host browser evidence before independent CR-04 rerun

> This is host-observed pre-review evidence, not independent reviewer evidence. The fresh `prototype_qa_reviewer` must reverse-lookup every row again.

| Map | Source fact | Stable selector | Trigger | Expected | Host-observed actual result | Verdict |
|---|---|---|---|---|---|---|
| 3.1 | `S0_SHARED_ENTRY` | `[data-state="S0_SHARED_ENTRY"]` / `#scene` | state selector | Shared entry, restore summary, enter action | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S1_ROOM_IDLE` | `[data-state="S1_ROOM_IDLE"]` / `#scene` | state selector | room orbs + palette + guard | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S2_PLACEMENT_AIM` | `[data-state="S2_PLACEMENT_AIM"]` / `#scene` | state selector | ray, reticle, ghost and commit gate | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S3_ORB_SELECTED` | `[data-state="S3_ORB_SELECTED"]` / `#scene` | state selector | selected rings + inspector | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S4_ORB_DRAGGING` | `[data-state="S4_ORB_DRAGGING"]` / `#scene` | state selector | tether and live pose | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S5_LAYOUT_LOCKED` | `[data-state="S5_LAYOUT_LOCKED"]` / `#scene` | state selector | lock label and manipulation disabled | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S6_CLEAR_CONFIRM` | `[data-state="S6_CLEAR_CONFIRM"]` / `#scene` | state selector | blocking clear dialog | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S7_PHOTO_PREP` | `[data-state="S7_PHOTO_PREP"]` / `#scene` | state selector | frame/timer choices and start | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S8_PHOTO_READY` | `[data-state="S8_PHOTO_READY"]` / `#scene` | state selector | clean pre-roll, no editor/ray | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S9_PHOTO_COUNTDOWN` | `[data-state="S9_PHOTO_COUNTDOWN"]` / `#scene` | state selector | frame + center countdown | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S10_CAPTURE_RESULT` | `[data-state="S10_CAPTURE_RESULT"]` / `#scene` | state selector | saved/failure result | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `S11_TRACKING_RECOVERY` | `[data-state="S11_TRACKING_RECOVERY"]` / `#scene` | state selector | frozen layout + retry/return | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-ENTER` | `[data-transition="TR-ENTER"]` | transition button | S0→S1 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-PLACE-AIM` | `[data-transition="TR-PLACE-AIM"]` | transition button | S1→S2 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-PLACE-COMMIT` | `[data-transition="TR-PLACE-COMMIT"]` | transition button | S2→S1 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-SELECT` | `[data-transition="TR-SELECT"]` | transition button | S1→S3 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-DRAG` | `[data-transition="TR-DRAG"]` | transition button | S3→S4 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-DRAG-END` | `[data-transition="TR-DRAG-END"]` | transition button | S4→S3 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-HOLD-DELETE` | `[data-transition="TR-HOLD-DELETE"]` | transition button | S3→S1 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-LOCK` | `[data-transition="TR-LOCK"]` | transition button | S1/S3→S5 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-UNLOCK` | `[data-transition="TR-UNLOCK"]` | transition button | S5→S1 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-CLEAR-OPEN` | `[data-transition="TR-CLEAR-OPEN"]` | transition button | S1/S3→S6 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-CLEAR-CONFIRM` | `[data-transition="TR-CLEAR-CONFIRM"]` | transition button | S6→S1 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-PHOTO-PREP` | `[data-transition="TR-PHOTO-PREP"]` | transition button | S1/S5→S7 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-PHOTO-READY` | `[data-transition="TR-PHOTO-READY"]` | transition button | S7→S8 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-COUNTDOWN` | `[data-transition="TR-COUNTDOWN"]` | transition button | S8→S9 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-CAPTURE` | `[data-transition="TR-CAPTURE"]` | transition button | S9→S10 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-PHOTO-EXIT` | `[data-transition="TR-PHOTO-EXIT"]` | transition button | S8/S9/S10→S1/S5 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-PANEL` | `[data-transition="TR-PANEL"]` | transition button | same | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-TRACKING-LOST` | `[data-transition="TR-TRACKING-LOST"]` | transition button | workspace/photo→S11 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.1 | `TR-STAGE-EXIT` | `[data-transition="TR-STAGE-EXIT"]` | transition button | S1–S11→S0 | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | EntryGate.`entry_title` | `[data-preview-id="entry_title"]` | activate owning state and component lab | 房间灯光师 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | EntryGate.`entry_mode` | `[data-preview-id="entry_mode"]` | activate owning state and component lab | 混合现实 · 真实房间可见 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | EntryGate.`restore_summary` | `[data-preview-id="restore_summary"]` | activate owning state and component lab | 将恢复最近的布光方案 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | EntryGate.`enter_stage` | `[data-preview-id="enter_stage"]` | activate owning state and component lab | 进入房间布光 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | EntryGate.`entry_exit_help` | `[data-preview-id="entry_exit_help"]` | activate owning state and component lab | 退出 / 使用说明 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | EntryGate.`discard_invalid` | `[data-preview-id="discard_invalid"]` | activate owning state and component lab | 忽略无效记录 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightPalette.`preset_sunset` | `[data-preview-id="preset_sunset"]` | activate owning state and component lab | 日落 · 暖雾 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightPalette.`preset_moon` | `[data-preview-id="preset_moon"]` | activate owning state and component lab | 月光 · 星尘 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightPalette.`preset_neon` | `[data-preview-id="preset_neon"]` | activate owning state and component lab | 霓虹 · 扫描 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightPalette.`preset_stage` | `[data-preview-id="preset_stage"]` | activate owning state and component lab | 舞台 · 聚光 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightPalette.`preset_custom` | `[data-preview-id="preset_custom"]` | activate owning state and component lab | 自定义颜色 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | WorkspaceGuardBar.`orb_capacity` | `[data-preview-id="orb_capacity"]` | activate owning state and component lab | 0/8 光球 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | WorkspaceGuardBar.`save_state` | `[data-preview-id="save_state"]` | activate owning state and component lab | 已自动保存 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | WorkspaceGuardBar.`tracking_state` | `[data-preview-id="tracking_state"]` | activate owning state and component lab | 空间稳定 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | WorkspaceGuardBar.`layout_lock` | `[data-preview-id="layout_lock"]` | activate owning state and component lab | 锁定布局 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | WorkspaceGuardBar.`clear_all` | `[data-preview-id="clear_all"]` | activate owning state and component lab | 清空 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | WorkspaceGuardBar.`photo_entry` | `[data-preview-id="photo_entry"]` | activate owning state and component lab | 摄影模式 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | OrbInspector.`orb_identity` | `[data-preview-id="orb_identity"]` | activate owning state and component lab | 已选中 · 日落 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | OrbInspector.`color_control` | `[data-preview-id="color_control"]` | activate owning state and component lab | 颜色 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | OrbInspector.`brightness_control` | `[data-preview-id="brightness_control"]` | activate owning state and component lab | 亮度 70% / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | OrbInspector.`radius_control` | `[data-preview-id="radius_control"]` | activate owning state and component lab | 范围 2.0m / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | OrbInspector.`hold_delete_hint` | `[data-preview-id="hold_delete_hint"]` | activate owning state and component lab | 按住扳机 2 秒删除 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | OrbInspector.`deselect` | `[data-preview-id="deselect"]` | activate owning state and component lab | 完成 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_core` | `[data-preview-id="orb_core"]` | activate owning state and component lab | conditional visual / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_glow` | `[data-preview-id="orb_glow"]` | activate owning state and component lab | conditional visual / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_particles` | `[data-preview-id="orb_particles"]` | activate owning state and component lab | conditional visual / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_range` | `[data-preview-id="orb_range"]` | activate owning state and component lab | 影响范围 2.0m / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_selection` | `[data-preview-id="orb_selection"]` | activate owning state and component lab | 已选中 · 日落 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_hold` | `[data-preview-id="orb_hold"]` | activate owning state and component lab | 松开取消 · 删除 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | LightOrbEntity.`orb_tether` | `[data-preview-id="orb_tether"]` | activate owning state and component lab | conditional drag line / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`capture_status` | `[data-preview-id="capture_status"]` | activate owning state and component lab | 可保存到系统相册 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`frame_white` | `[data-preview-id="frame_white"]` | activate owning state and component lab | 简约白边 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`frame_film` | `[data-preview-id="frame_film"]` | activate owning state and component lab | 胶片黑边 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`frame_instant` | `[data-preview-id="frame_instant"]` | activate owning state and component lab | 拍立得质感 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`timer_segments` | `[data-preview-id="timer_segments"]` | activate owning state and component lab | 3秒 / 5秒 / 10秒 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`photo_start` | `[data-preview-id="photo_start"]` | activate owning state and component lab | 开始摄影 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoSetup.`photo_cancel` | `[data-preview-id="photo_cancel"]` | activate owning state and component lab | 取消 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoExperience.`photo_frame` | `[data-preview-id="photo_frame"]` | activate owning state and component lab | conditional frame / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoExperience.`countdown_value` | `[data-preview-id="countdown_value"]` | activate owning state and component lab | 3 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoExperience.`capture_success` | `[data-preview-id="capture_success"]` | activate owning state and component lab | 已保存到系统相册 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoExperience.`capture_error` | `[data-preview-id="capture_error"]` | activate owning state and component lab | 保存失败 · 返回设置重试 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PhotoExperience.`editor_visibility_guard` | `[data-preview-id="editor_visibility_guard"]` | activate owning state and component lab | conditional assertion / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | RecoverySurface.`recovery_title` | `[data-preview-id="recovery_title"]` | activate owning state and component lab | 清空全部 4 个光球？ / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | RecoverySurface.`recovery_icon` | `[data-preview-id="recovery_icon"]` | activate owning state and component lab | 警告 / 操作失败 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | RecoverySurface.`recovery_body` | `[data-preview-id="recovery_body"]` | activate owning state and component lab | 此操作无法撤销，已保存方案也会更新。 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | RecoverySurface.`recovery_preserved` | `[data-preview-id="recovery_preserved"]` | activate owning state and component lab | 当前布局仍保留 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | RecoverySurface.`recovery_cancel` | `[data-preview-id="recovery_cancel"]` | activate owning state and component lab | 取消 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | RecoverySurface.`recovery_primary` | `[data-preview-id="recovery_primary"]` | activate owning state and component lab | 清空 / 重试 / 返回入口 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PlacementGuide.`placement_ray` | `[data-preview-id="placement_ray"]` | activate owning state and component lab | conditional SDK ray / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PlacementGuide.`placement_reticle` | `[data-preview-id="placement_reticle"]` | activate owning state and component lab | conditional reticle / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PlacementGuide.`placement_ghost` | `[data-preview-id="placement_ghost"]` | activate owning state and component lab | conditional ghost / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PlacementGuide.`placement_prompt` | `[data-preview-id="placement_prompt"]` | activate owning state and component lab | 扣动扳机放置 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.2 | PlacementGuide.`placement_block_reason` | `[data-preview-id="placement_block_reason"]` | activate owning state and component lab | 已达上限 / 跟踪不可用 / 已锁定 / state-aware | Trigger executed; stable selector resolved and expected visible result/target state was observed. | pass |
| 3.3 | EntryGate.`CapabilityState.stageReady` | `[data-binding="EntryGate:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | EntryGate.`LayoutSnapshot.orbs.size` | `[data-binding="EntryGate:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | EntryGate.`LayoutSnapshot.invalidCount` | `[data-binding="EntryGate:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | EntryGate.`LayoutSnapshot.schemaVersion` | `[data-binding="EntryGate:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | EntryGate.`LayoutSnapshot.savedAt` | `[data-binding="EntryGate:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightPalette.`PresetDefinition.id/name/effect` | `[data-binding="LightPalette:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightPalette.`InteractionState.pendingRecipe` | `[data-binding="LightPalette:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightPalette.`orbCount` | `[data-binding="LightPalette:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightPalette.`layoutLocked` | `[data-binding="LightPalette:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightPalette.`customColor` | `[data-binding="LightPalette:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | WorkspaceGuardBar.`orbCount` | `[data-binding="WorkspaceGuardBar:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | WorkspaceGuardBar.`layoutLocked` | `[data-binding="WorkspaceGuardBar:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | WorkspaceGuardBar.`PersistenceState` | `[data-binding="WorkspaceGuardBar:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | WorkspaceGuardBar.`TrackingState` | `[data-binding="WorkspaceGuardBar:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | WorkspaceGuardBar.`CaptureCapability` | `[data-binding="WorkspaceGuardBar:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | OrbInspector.`selected.id/kind` | `[data-binding="OrbInspector:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | OrbInspector.`selected.color` | `[data-binding="OrbInspector:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | OrbInspector.`selected.brightness` | `[data-binding="OrbInspector:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | OrbInspector.`selected.radius` | `[data-binding="OrbInspector:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | OrbInspector.`InteractionState.mode` | `[data-binding="OrbInspector:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | OrbInspector.`deleteHold.progress` | `[data-binding="OrbInspector:5"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`LightOrb.position` | `[data-binding="LightOrbEntity:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`LightOrb.color` | `[data-binding="LightOrbEntity:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`LightOrb.brightness` | `[data-binding="LightOrbEntity:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`LightOrb.radius` | `[data-binding="LightOrbEntity:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`LightOrb.kind` | `[data-binding="LightOrbEntity:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`InteractionState` | `[data-binding="LightOrbEntity:5"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | LightOrbEntity.`layoutLocked` | `[data-binding="LightOrbEntity:6"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoSetup.`PhotoSession.frameStyle` | `[data-binding="PhotoSetup:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoSetup.`PhotoSession.countdownSeconds` | `[data-binding="PhotoSetup:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoSetup.`CaptureCapability.permission` | `[data-binding="PhotoSetup:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoSetup.`CaptureCapability.publicPathAvailable` | `[data-binding="PhotoSetup:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoExperience.`PhotoSession.frameStyle` | `[data-binding="PhotoExperience:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoExperience.`PhotoSession.countdownTick` | `[data-binding="PhotoExperience:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoExperience.`CaptureResult.status/uri/error` | `[data-binding="PhotoExperience:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoExperience.`panelVisible` | `[data-binding="PhotoExperience:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PhotoExperience.`controllerRayVisible` | `[data-binding="PhotoExperience:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | RecoverySurface.`RecoveryIssue.type` | `[data-binding="RecoverySurface:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | RecoverySurface.`RecoveryIssue.detail` | `[data-binding="RecoverySurface:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | RecoverySurface.`RecoveryIssue.preservedData` | `[data-binding="RecoverySurface:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | RecoverySurface.`orbCount` | `[data-binding="RecoverySurface:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | RecoverySurface.`TrackingState/CaptureResult/PersistenceState` | `[data-binding="RecoverySurface:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`InteractionState.pendingRecipe` | `[data-binding="PlacementGuide:0"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`InteractionState.rayVisible/activeController` | `[data-binding="PlacementGuide:1"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`PlacementPose.position` | `[data-binding="PlacementGuide:2"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`PlacementPose.valid` | `[data-binding="PlacementGuide:3"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`TrackingState` | `[data-binding="PlacementGuide:4"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`orbCount` | `[data-binding="PlacementGuide:5"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.3 | PlacementGuide.`layoutLocked` | `[data-binding="PlacementGuide:6"]` + bound element | normal, fallback, and error data modes | target attribute, fallback, color + shape + label conversion | All three modes changed the unique binding target, aria-label, semantic shape, and human-readable output. | pass |
| 3.4 | EntryGate variant `firstRun` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate variant `restoreAvailable` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate variant `restorePartial` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | EntryGate stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette variant `regularGrid` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette variant `compactRail` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette variant `customPickerExpanded` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `selected` | component + state selectors | data-component-state + selected shape+text | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightPalette stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar variant `workspace` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar variant `locked` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar variant `compact` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar variant `photoPrepHeader` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | WorkspaceGuardBar stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector variant `preset` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector variant `custom` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector variant `compactSwatches` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector variant `draggingReadOnly` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `editing` | component + state selectors | data-component-state + live value + active control | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `dragging` | component + state selectors | data-component-state + tether/read-only controls/live pose | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `delete-armed` | component + state selectors | data-component-state + 2s progress + release-to-cancel | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | OrbInspector stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity variant `sunset` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity variant `moon` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity variant `neon` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity variant `stage` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity variant `custom` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `selected` | component + state selectors | data-component-state + selected shape+text | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `dragging` | component + state selectors | data-component-state + tether/read-only controls/live pose | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `delete-armed` | component + state selectors | data-component-state + 2s progress + release-to-cancel | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | LightOrbEntity stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup variant `ready` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup variant `permissionRequired` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup variant `emulatorLimited` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `selected` | component + state selectors | data-component-state + selected shape+text | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoSetup stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `whiteBorder` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `filmBlackBorder` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `instant` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `ready` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `countdown` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `success` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience variant `failure` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `countdown` | component + state selectors | data-component-state + fixed-center numeral and hidden editor/ray | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `capturing` | component + state selectors | data-component-state + progress without flash | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `success` | component + state selectors | data-component-state + saved check + human label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PhotoExperience stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface variant `clearConfirm` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface variant `trackingLost` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface variant `captureFailed` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface variant `permissionDenied` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface variant `saveFailed` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface variant `invalidRestore` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | RecoverySurface stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `sunset` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `moon` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `neon` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `stage` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `custom` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `valid` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `trackingBlocked` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `capacityBlocked` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide variant `lockBlocked` | component + variant selectors | data-variant + visual label | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `default` | component + state selectors | data-component-state + baseline component presentation | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `hover` | component + state selectors | data-component-state + focus outline/hover response | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `pressed` | component + state selectors | data-component-state + press feedback/haptic semantics | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `disabled` | component + state selectors | data-component-state + disabled style plus reason | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `loading` | component + state selectors | data-component-state + progress plus retained context | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `empty` | component + state selectors | data-component-state + guidance or conditional hide | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `error` | component + state selectors | data-component-state + source-specific error and recovery | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide state `overflow` | component + state selectors | data-component-state + reflow/scroll/truncation without target shrink | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.4 | PlacementGuide stacking | stacked-state selector | precedence banner | mapped | Unique observable selector resolved; component, variant/state, dominant styling, label, and stacking precedence changed. | pass |
| 3.5 | Large | `[data-responsive="Large"]` | `[data-responsive="Large"]` | max safe-area class, wider primary region/two-column allowance | Exact tier geometry/reflow or Reduce Motion structure assertion observed. | pass |
| 3.5 | Compact | `[data-responsive="Compact"]` | `[data-responsive="Compact"]` | default safe-area class and default grid | Exact tier geometry/reflow or Reduce Motion structure assertion observed. | pass |
| 3.5 | Constrained | `[data-responsive="Constrained"]` | `[data-responsive="Constrained"]` | horizontal chips/internal scroll/in-row overflow; no global scale | Exact tier geometry/reflow or Reduce Motion structure assertion observed. | pass |
| 3.5 | Reduce Motion | `[data-reduce-motion]` | `[data-reduce-motion]` | no scale/pulse/particle/scan animation; semantic outline/text retained | Exact tier geometry/reflow or Reduce Motion structure assertion observed. | pass |

## 4. Generation declarative checks (host evidence complete; independent QA rerun required)

| Check | Source denominator | Selector/trigger | Expected | Generation assertion | QA actual/verdict |
|---|---|---|---|---|---|
| Manifest complete | §§2.1–2.5 | manual count | 12 states, 19 transitions, 53 elements, 49 bindings, 133 variant/state/stack items, 4 responsive/motion | complete | host observed exact 12/19/53/49/133/4; fresh QA required |
| State machine | §2.1 | `#state-select`, `renderScene()` | distinct scene/focus per state | implemented | 12/12 state scenes rendered with stable state selector; host pass |
| Transitions | §2.1 | `[data-transition]` | target state / confirm behavior | implemented | 19/19 source-trigger-target paths, including cancel/confirm, observed; host pass |
| DOM back-check | §2.2 | `[data-preview-id]` | 53 unique nodes | implemented | runtime ids are isolated from `data-lab-preview-id`; required world nodes observed; host pass |
| Data modes | §2.3 | `#data-mode` | normal/fallback/error changes bound output | implemented | 147/147 binding-mode checks changed target/label/shape; host pass |
| Component lab | §2.4 | lab selectors | each variant/state/stack observable | implemented | 44 variants + 80 states + 9 stacking assertions = 133/133; host pass |
| High-risk confirmation | transitions marked yes | `#confirm-dialog` | enter/exit/clear blocked; confirm/cancel | implemented | each marked transition blocked, cancel preserved source and confirm reached target; host pass |
| Responsive/motion | §2.5 | tier selector/toggle | structural reflow, no global scale | implemented | exact 920×640/720×540/520×380 and safe areas plus Reduce Motion observed; host pass |

## 5. Generation denominator reconciliation

| Type | Design total | Manifest total | QA total | Difference | Status |
|---|---:|---:|---:|---:|---|
| States | 12 | 12 | 12 | 0 | host reconciled; independent QA rerun required |
| Transitions | 19 | 19 | 19 | 0 | host reconciled; independent QA rerun required |
| renderSpec elements | 53 | 53 | 53 | 0 | host reconciled; independent QA rerun required |
| dataBindings | 49 | 49 | 49 | 0 | host reconciled; independent QA rerun required |
| variants + component states + stacking | 133 | 133 | 133 | 0 | host reconciled; independent QA rerun required |
| Responsive / Reduce Motion | 4 | 4 | 4 | 0 | host reconciled; independent QA rerun required |

## 6. Requirements traceability

| Task | States | Components | Preview validation | Status |
|---|---|---|---|---|
| T0 | S0 | EntryGate | state + component lab + fallback mode | covered |
| T1 | S0/S1 | EntryGate/LightOrbEntity | state + component lab + fallback mode | covered |
| T2 | S1 | LightPalette | state + component lab + fallback mode | covered |
| T3 | S2 | PlacementGuide | state + component lab + fallback mode | covered |
| T4 | S3 | OrbInspector/LightOrbEntity | state + component lab + fallback mode | covered |
| T5 | S3/S4/S6 | LightOrbEntity/RecoverySurface | state + component lab + fallback mode | covered |
| T6 | S5 | WorkspaceGuardBar | state + component lab + fallback mode | covered |
| T7 | S1/S3/S5 | WorkspaceGuardBar | state + component lab + fallback mode | covered |
| T8 | S7/S8 | PhotoSetup/PhotoExperience | state + component lab + fallback mode | covered |
| T9 | S9/S10 | PhotoExperience/RecoverySurface | state + component lab + fallback mode | covered |
| T10 | S0/S8–S10 | EntryGate/PhotoExperience | state + component lab + fallback mode | covered |

Coverage: 11/11 tasks = 100% for Web logical validation. Device behavior remains unverified.

## 7. Sample-data modes

| Mode | Examples | Expected conversion |
|---|---|---|
| normal | 2/8, 日落 selected, 70%, 2.0m, saved, tracking ready | human labels + color/shape semantics |
| fallback | no snapshot, missing custom color, static effect fallback | guidance/defaults without raw enum |
| error | tracking invalid, permission denied, save/capture failure, incompatible schema | source-specific human error + smallest recovery |

## 8. Device-validation boundary

`deviceValidation.status = not_performed`. Viewing distance, occlusion, fatigue, controller hit precision, compositor capture, gallery save, SpatialUI material fidelity, runtime performance and safety require PICO emulator/device validation. Web preview proves logical mapping only.

## 9. Independent QA

### Review invocation PQA-FAST-20260815-a41d0c2f

| Field | Value |
|---|---|
| reviewerRole | `prototype_qa_reviewer` |
| contextPolicy | `fresh_context / isolated_subagent` |
| reviewedRevision | Interaction@7; Visual@4; DesignSystemReview@3; Preview@1; PreviewQAReport@Generation1 |
| evidenceRebuilt | yes |
| deviceValidation.status | `not_performed` |
| recommendation | `block` |

| Denominator | Manifest | QA rebuilt | Delta | Verdict |
|---|---:|---:|---:|---|
| States | 12 | 12 | 0 | count pass |
| Transitions | 19 | 19 | 0 | count pass |
| renderSpec elements | 53 | 53 | 0 | count pass |
| dataBindings | 49 | 49 | 0 | count pass |
| Variants + component states + stacking | 133 | 133 | 0 | count pass |
| Responsive + Reduce Motion | 4 | 4 | 0 | count pass |

Count reconciliation passes, but implementation fidelity is blocked by these independently rebuilt findings:

- §4 all eight QA actual/verdict cells and §§3.1–3.5 per-item actual/verdict cells are still pending.
- All 49 binding demonstrations render generic source-to-mode text instead of a per-binding target attribute, separately triggered normal/fallback/error output, and observable display/semantic conversion.
- `TR-HOLD-DELETE` lacks the two-second progress and release-cancel behavior; `TR-PANEL` does not toggle visibility in place; `TR-PHOTO-EXIT` does not restore remembered unlocked/locked state.
- Required recovery/back actions are absent or inert for placement cancel, deselect, clear cancel, photo cancel, capture retry, tracking regain, and the visible entry/preset/lock/clear/photo controls.
- `orb_glow`, `orb_range`, and `orb_hold` are not represented in the runtime scene; initial-scene selectors collide with component-lab selectors.
- The 133 lab rows change only labels/data attributes or generic styling, not component-specific observable results.
- Large/Compact exact height and content-area assertions are absent; photo states expose no responsive selector.

Patch target: make the Web prototype an executable logical model with unique selectors, item-specific binding targets/fallbacks/semantic output, genuine transition and recovery actions, observable component variants/states/stacking, and explicit Large/Compact/Constrained geometry plus Reduce Motion assertions. Then rerun `preview_build → preview_review → delivery_self_review`.

### Generation-side browser smoke evidence (not independent QA)

- Loaded `http://127.0.0.1:8765/preview.html` with title `RoomLightDesigner · Web Design Validation`; console warnings/errors: 0.
- DOM: 12 state options, 19 transition triggers, 9 component-lab entries.
- Placement error/Constrained/Reduce Motion scenario: `placement_ray`, `placement_reticle`, `placement_ghost` present; human fallback “空间跟踪不可用”; shell width 520px; Reduce Motion true.
- `TR-ENTER`: confirmation visible; cancel kept S0; confirm entered S1.
- Component lab rebuilt exact element/binding/variant/state counts for all nine components: EntryGate 6/5/3/8; LightPalette 5/5/3/8; WorkspaceGuardBar 6/5/4/8; OrbInspector 6/6/4/11; LightOrbEntity 7/7/5/10; PhotoSetup 7/4/3/8; PhotoExperience 5/5/7/11; RecoverySurface 6/5/6/8; PlacementGuide 5/7/9/8.

### CR-04 independent rerun · CR04-PQA-8ef3ca12-6570-4866-bc67-adfa320486be

`contextPolicy=isolated_subagent`; reviewed Interaction@7, Visual@4, DesignCritique@4, PreviewQAReport@3 (generation header 2), Preview@2; `evidenceRebuilt=yes`; `deviceValidation=not_performed`; recommendation **block**.

| Map | Manifest | Rebuilt | Delta | Independently passing |
|---|---:|---:|---:|---:|
| States | 12 | 12 | 0 | 12 |
| Transitions | 19 | 19 | 0 | 16 |
| renderSpec elements | 53 | 53 | 0 | 52 |
| dataBindings | 49 | 49 | 0 | 0 |
| Variants + states + stacking | 133 | 133 | 0 | 36 |
| Responsive + Reduce Motion | 4 | 4 | 0 | 4 |

Active CR-05 patch targets: preserve every legal source state for panel/tracking/Stage-exit transitions; make `orb_glow` own visible geometry; replace generic binding declarations with source-specific target properties/conversions/values; render structurally distinct variants, component-specific disabled/loading/empty/error and special states, plus actual dominant stacking behavior; activate entry help/discard, photo frame/timer selection, and inspector parameter updates; disable clear while locked.

### CR-05 host browser evidence before independent rerun

- 44/44 variants have distinct structure signatures; 80/80 component states expose state-specific behavior/reason/progress/recovery; 9/9 stacking rows apply a combined dominant behavior. Total 133/133, errors 0.
- 49 bindings × normal/fallback/error = 147/147 checks expose source-specific target properties, conversions and converted values, errors 0.
- Multi-source transitions: panel toggle 3/3 sources, tracking loss/recovery 7/7 sources, Stage-exit cancellation 11/11 sources, errors 0.
- Entry help/discard, photo frame/timer selection, live brightness/radius/color, visible owned glow geometry, and locked-clear safety all passed; glow selector bounds measured 142.17×142.17 CSS px.
- This remains host evidence only; a fresh isolated reviewer must independently reverse-lookup all 270 rows.

### CR-05 independent rerun · CR05-PQA-fc4b7204-879c-46b5-9769-e0c67626fe04

Reviewed Interaction@7, Visual@4, DesignCritique@4, PreviewQAReport@5 (generation header 3), Preview@3; `contextPolicy=isolated_subagent`; `evidenceRebuilt=yes`; `deviceValidation=not_performed`; recommendation **block**.

Counts again reconcile at 12/19/53/49/133/4 with zero delta. Strictly passing: 12 states, 19 transitions, 53 render elements, 20 bindings, 127 variant/state/stacking items, 4 responsive/motion scenarios (235/270 total). CR-06 is limited to the 29 named binding rows whose target property/conversion does not yet match Visual@4 and six named stacking rows whose precedence is still generic. No state, transition, element, variant, action, geometry or responsive patch is authorized.

### CR-06 host browser evidence before independent rerun

- The 34-row union of the two independent binding finding lists now uses explicit per-row target properties, design-fact conversion, normal/fallback/error values; 102/102 mode checks passed with no generic conversion remaining.
- Photo panel/ray bindings explicitly remain `false` in photo mode; `PlacementPose.valid` maps to reticle/commit validity rather than Vector3 transform.
- Component-specific precedence is encoded from Visual@4 for all nine components. Twelve high-risk combined cases across palette, guard, inspector, orb, photo, recovery and placement produced the expected dominant state; errors 0.
- No other state, transition, element, variant, action, geometry or responsive implementation was changed.

### CR-06 independent pass · CR06-PQA-762850e2-88e5-4e97-98d6-03f5b238ce37

`contextPolicy=fresh_context / isolated_subagent`; reviewed Interaction@7, Visual@4, DesignCritique@4, PreviewQAReport@7 (Generation@4), Preview@4; `evidenceRebuilt=yes`; `deviceValidation=not_performed`; strict recommendation **pass**.

| Denominator | Manifest | QA rebuilt | Delta | Passing |
|---|---:|---:|---:|---:|
| States | 12 | 12 | 0 | 12 |
| Transitions | 19 | 19 | 0 | 19 |
| renderSpec elements | 53 | 53 | 0 | 53 |
| dataBindings | 49 | 49 | 0 | 49 |
| Variants + component states + stacking | 133 | 133 | 0 | 133 |
| Responsive + Reduce Motion | 4 | 4 | 0 | 4 |
| **Total** | **270** | **270** | **0** | **270** |

Independent focus checks: explicit union 34/34; union mode checks 102/102; full binding modes 147/147; stacking 9/9 including 34 collision checks; previously passing rows 235/235; missing row IDs none; browser console warnings/errors 0.

## 10. Minimum Completeness Gate

| Requirement | Evidence | Verdict |
|---|---|---|
| Input readiness | §1 all design-system/source revisions ready | pass |
| Coverage Manifest itemized | §§2.1–2.5 lists all 12/19/53/49/133/4 facts | pass |
| Five implementation maps | §§3.1–3.5 plus §3A carry source, selector, trigger, expected, actual and verdict for 270 rows | pass |
| Independent denominator rebuild | CR06-PQA invocation above: 270/270, delta 0 | pass |
| Device boundary | `deviceValidation.status=not_performed` | pass |

`minimumCompletenessGate = pass`; `Preview Implementation Fidelity = pass`. This Web gate does not claim PICO runtime/device validation.
