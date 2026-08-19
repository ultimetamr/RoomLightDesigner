# Spatial App Requirement Spec · RoomLightDesigner

> Role: `product_strategist` | Active revision: 4 | Workflow stages: `intent`, `quality_contract` | Change request: `CR-01`

Source record: `USER-PRD-001`, exact original PRD text at `.scratch/user_prd_001.md`, captured 2026-08-15, SHA-256 `5d4db4e46b1f5df99b1f26963c038129b095215b7382b0574fbfdd836c5ed46b`. All `User PRD` references below point to stable headings in that immutable record.

## 1. Intent Definition

### 1.1 Background and problem

- **One-sentence requirement**: RoomLightDesigner 是一款面向 PICO OS 6 的 MR 空间布光工具，让用户在真实房间的透视画面上放置最多 8 个可调光球，以伪光晕、体积粒子和预设效果快速预演空间氛围，并直接进入无 UI 摄影模式保存完整 MR 画面。
- **Target users**: 普通房间氛围设计用户、室内内容创作者、摄影/短视频创作者，以及需要现场预演局部灯光效果的轻量空间设计使用者。
- **Use scenarios**: 室内站立或缓慢移动状态下，用户观察真实房间，通过手柄在墙面、桌面附近或空气中放置与调整虚拟光球；完成方案后锁定布局并拍摄 MR 合成画面。
- **Wearing posture**: 以站立和小范围移动为主，允许坐姿微调；不要求快速转身、奔跑或大范围行走。
- **Frequency and duration**: 按房间/拍摄任务间歇使用；单次会话与单光球精调时长尚无用户研究数据，作为假设在 §2 管理。
- **Preliminary spatial necessity**: 核心决策依赖真实房间中的方向、距离、遮挡和尺度关系；二维应用无法让用户直接在物理空间中判断光球位置、影响半径与多个光源的空间组合，因此 Stage Mixed + Passthrough 具有不可替代价值。

### 1.2 Key moment

- **The moment a screen cannot achieve**: 用户将暖橙“日落”和冷蓝“月光”分别放在房间两侧，移动头部同时看到真实家具、两个光球及其半径/粒子差异，并在原位拖拽调整到满意构图；这一判断需要共同的真实空间坐标系。
- **Immersion spectrum**: 光球放置、选择、拖拽、锁定和摄影属于 Full Space 中的 Stage Mixed；参数调节与预设选择是附着在 Stage 锚点上的平面控制面板；倒计时是视野中心的临时 HUD 状态。
- **Entry path**: 应用先在 Shared Space 显示单一入口面板，用户明确点击“进入房间布光”后才打开 Stage Mixed/Passthrough；Stage 内提供稳定退出动作返回入口面板。摄影模式仅在用户主动点击入口后隐藏 UI 与射线。

### 1.3 Frozen intent fields

- **Domain / sub-domain**: MR 空间创作工具 / 室内氛围布光与合成摄影。
- **Risk level**: 中等；主要风险来自佩戴头显在真实房间移动、误删布局、坐标恢复漂移、截图/媒体权限缺失，以及用户将伪光效误解为物理照度预测。
- **Default space**: Shared Space 入口；用户确认后切换到 Full Space 的 Stage Mixed，真实环境 Passthrough 可见。
- **Core scenarios**: 放置预设或自定义光球；选择并实时调节颜色/亮度/范围；拖拽移动或长按删除；锁定/解锁布局；自动保存与启动恢复；进入摄影模式、选择相框和倒计时、保存到系统相册；菜单键切换操作面板。
- **Data / AI / sensors / permissions**: 使用头部与手柄 6DoF 位姿、射线命中、扳机和菜单键事件、应用局部空间坐标；不使用 AI、不联网；本地持久化；拍摄保存需要系统支持的 MR 截图能力及媒体库写入权限，缺失时必须提示并保留可恢复状态。
- **Collaboration**: 无多用户、无账号、无云同步，单设备单用户。

## 2. Assumptions

| # | Assumption | Confidence | Impact | Validation plan |
|---:|---|---|---|---|
| 1 | “影响范围”以光晕/体积粒子半径表达，不承诺真实光照计算或物理照度。 | high | 决定视觉文案、范围指示与验收口径。 | 在首次引导与参数面板中标注“视觉预览”；设备验收确认无真实 GI 依赖。 |
| 2 | 自动恢复使用应用可获得的本地 Stage/Tracking 坐标；若空间原点变化则降级为安全默认位置或提示重新放置。 | medium | 影响跨启动坐标稳定性与空间丢失恢复。 | 模拟器重启、应用重启及追踪重定位测试；真实设备补验。 |
| 3 | 公开 API 能提供完整 MR 合成截图并写入系统相册；若模拟器只返回平面缓冲，摄影流程可验证而合成质量标为设备待验。 | medium | 影响摄影模式的最终证据与相册交付。 | 查验 SDK/API，执行权限与保存路径测试，真实设备补验合成图层。 |
| 4 | 长按扳机删除仅在已选中、未锁定且没有拖拽位移超过阈值时触发。 | high | 影响删除安全性与交互状态机。 | 单元测试 2 秒阈值、拖拽取消和锁定分支；手柄实测。 |
| 5 | 未提供品牌资产与视觉稿，视觉方向从“暗房灯光实验台 + 通透玻璃控制面板”语义独立推导。 | medium | 影响颜色、排版、面板密度和摄影相框质感。 | 通过结构化设计效果评审与 Web 预览门禁确认。 |
| 6 | 单次会话目标为 5–20 分钟、单光球精调目标为 10–60 秒。 | low | 影响流程密度、疲劳观察和效率门槛，但不是用户事实。 | 招募新手/熟练用户记录任务时长；将结果作为接受/调整目标的依据。 |

## 3. Product research anchors

| Dimension | Anchor | Source |
|---|---|---|
| Market baseline | Named lighting scenes, custom color and brightness are expected; save/reuse must be obvious. | UXR §3 Philips Hue benchmark |
| Spatial context baseline | Direct room context, placement, capability-aware fallback and recovery are valuable; room reconstruction must not become mandatory overhead. | UXR §3 IKEA Kreativ benchmark |
| Accuracy boundary | Professional tools explicitly calculate photometric results; this product must clearly remain a fast aesthetic preview. | UXR §3 DIALux benchmark |
| Platform/safety | Full Space/Stage legality, stable exit, Reduce Motion, controller fallback, non-color state cues, safe attachment decisions. | UXR §2 platform/safety; `official-rules.json` |
| Duration evidence | No observed user baseline exists for RoomLightDesigner; usability targets below are product acceptance targets requiring validation. | UXR §7 evidence gaps |

## 4. Quality contract

- **Required user outcomes**:
  1. Place any of four presets or a custom-color orb at an aimed room position and immediately recognize its preset/effect.
  2. Select an existing orb and adjust color, brightness 0–100%, and radius 0.5–5 m with clamped, visible, live feedback.
  3. Manage up to eight orbs; prevent a ninth, explain the limit, delete one safely, or clear all only after confirmation.
  4. Move a selected orb by held trigger and delete it only after an uninterrupted two-second long press that does not conflict with dragging.
  5. Lock the layout so no orb can be selected, moved, or deleted while panel visibility remains controllable.
  6. Restore the latest valid layout on launch, or degrade safely when stored data/coordinates are invalid.
  7. Enter photo mode, hide editor UI/rays, choose one of three frames and a 3/5/10 second countdown, capture through a public API, save to the gallery, and restore controls on exit/failure.
- **Success / efficiency criteria**:
  - A first preset orb can be selected and placed within 30 seconds of entering the workspace for a novice test participant.
  - Preset identity, selected orb, lock state, orb count/limit, and active photo countdown are recognizable within a two-second glance test.
  - Parameter changes and drag movement update no later than the next rendered state; persisted state is scheduled after each accepted mutation and committed at lifecycle boundaries.
  - Every destructive path is reversible before commit or explicitly confirmed; clear-all never happens from a single unconfirmed activation.
- **Risks and must-not-fail items**: never claim physical illumination accuracy; never delete during lock or a drag; clamp all parameters; guard against stale selection after removal; preserve layout after capture failure; restore panel/ray state on photo exit; report permission, tracking, persistence and capture failures without crashing; cap particle density and provide Reduce Motion/performance fallback.
- **Default visible primary-window orientation**: Shared Space 默认仅一块显式 Stage 入口面板；进入 Full Space 后只存在一个 Stage Mixed 工作区，入口面板不与 Stage 共同占用注意力。Stage 内最多显示一个任务控制面板；具体承载 API、锚点和组件由后续容器/附件阶段决定。
- **Domain-specialized component orientation**: 光球语义必须同时表达预设身份、非颜色选择标记、范围和效果状态；控制面必须覆盖创建、三参数、数量/上限、锁定、摄影入口和清空；相框必须能辨识三种质感。具体组件名称与代码类型由设计系统和实现阶段决定。
- **Real-time data trust orientation**: there is no network data. Layout persistence includes schema version and save timestamp; invalid/corrupt data is rejected with a recoverable empty/default state. Capture result exposes pending/success/permission-denied/unsupported/failure states.
- **PICO platform and spatial-design hard constraints**: the versioned project registry `pico-spatial-app-designer/knowledge/official-rules.json@2.2.0` records `PICO-SPACESTATE-001/002` for legal Shared/Full transitions, plus project/comfort/safety constraints `PICO-STAGE-001`, `PICO-ACCESS-001/002/004`, `PICO-COLOR-001`, and `PICO-MOTION-001/002/003`; their provenance is preserved rather than all being called first-party official. The downstream contract `pico-spatial-agentic-tools@0.4.1/skills/spatial-ui-design-style/SKILL.md` separately mandates `PicoTheme`, SpatialUI built-ins first, no Material/Material3, and correct system-glass ownership. Stage control attachment and ECS anchoring remain downstream hypotheses until architecture stages decide them.
- **Originality requirement**: combine the benchmark’s scene immediacy, real-room context, save/recovery and parameter clarity into an embodied “room as canvas” workflow. Do not copy the Hue dashboard, IKEA room-editor structure, or DIALux CAD/result-monitor model; reject hardware dependence, scan-first overhead and photometric-simulation claims.
- **Design / readability / downstream implementation acceptance plan**: pass all 17 designer receipts and independent review gates; make every state/transition/component binding traceable in a single-file Web logic preview; then pass the downstream legality, architecture, unit-test, SpatialUI verifier, Gradle build, install/launch, crash-log and screenshot checks. Emulator evidence is not treated as real-device comfort or compositor proof.

## 5. Requirements traceability

| Requirement ID | Source record | Acceptance-level capability | Validation method |
|---|---|---|---|
| R-ENTRY-01 | `USER-PRD-001#项目基础信息` + `PICO-STAGE-001` | Explicit Stage entry and stable exit | State-transition review; runtime entry/exit test |
| R-LIGHT-01 | `USER-PRD-001#1-光球系统` | Four named presets and custom color creation | Preset mapping tests; preview and runtime placement |
| R-LIGHT-02 | `USER-PRD-001#1-光球系统` | Color/brightness/radius live adjustment with bounds | Boundary tests for 0–100% and 0.5–5 m |
| R-CAP-01 | `USER-PRD-001#1-光球系统` | Maximum eight and visible ninth-item rejection | Eighth/ninth creation tests; disabled/action notice evidence |
| R-DELETE-01 | `USER-PRD-001#1-光球系统`; `#2-空间交互` | Single delete, 2 s hold arbitration, confirmed clear-all | State-machine, lock and confirmation tests |
| R-INTERACT-01 | `USER-PRD-001#2-空间交互` | Aim/place/select/drag/menu-panel controls | Interaction state tests; controller validation |
| R-LOCK-01 | `USER-PRD-001#3-布局管理` | Lock blocks select/move/delete but permits panel visibility | Guard tests and runtime observation |
| R-SAVE-01 | `USER-PRD-001#3-布局管理` | Auto-save and safe restore of one layout | Serialization/corruption/clamp/lifecycle tests |
| R-PHOTO-01 | `USER-PRD-001#4-摄影模式` | UI/ray hiding, three frames, 3/5/10 countdown, trigger exit | Transition tests; preview triggers; runtime observation |
| R-PHOTO-02 | `USER-PRD-001#4-摄影模式` | Public-API MR capture and gallery result states | Permission/unsupported/success tests; device compositor validation |
| R-VISUAL-01 | `USER-PRD-001#视觉与技术约束` | Passthrough with fake glow/particles and no physical GI claim | Design review, architecture scan, scene/performance review |
| R-ERROR-01 | `USER-PRD-001#异常处理`; `#视觉与技术约束` | Tracking/value/permission/persistence failure recovery | Unit tests and crash-log monitoring |

## 6. Minimum Completeness Gate

| Check item | Evidence | Verdict |
|---|---|---|
| Background and frozen intent | Sections 1.1–1.3 | pass |
| Assumption governance | Section 2, every row has confidence/impact/validation | pass |
| Quality contract | Section 4 contains all nine acceptance orientations | pass |
| Requirements traceability | Section 5 maps every mandatory requirement | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |
