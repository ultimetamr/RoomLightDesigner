# Visual System Spec · RoomLightDesigner

> Active revision: 4 | Completed stages: `visual_direction`, `composition_synthesis`, `design_system` | Upstream: Interaction@7, PM@4, UXR@3 | SpatialUI design-style contract applied | `CR-03 repair round 2`

> Role: `visual_designer` (visual direction) and `spatial_design_system_designer` (component synthesis, data trust reconciliation) | Workflow stage(s): `visual_direction` → `composition_synthesis` → `design_system` (layout / component / visual / data-trust facts) | Upstream inputs: selected concept, experience architecture, research evidence, quality contract, state graph, approved visual reference | Downstream recipients: Prototype / Frontend Engineer, QA, Design Lead
>
> This document carries this role's **LLM reasoning information** and **direct description of outputs**. It is not bound to any JSON Schema or validator error codes; mandatory gates are expressed through this document's structured Markdown required tables, evidence anchors, and the `block` status.

## 0. Reasoning Guidance (how this role reasons)

- **Only make visual and design-system decisions**: visual hierarchy, typography, color semantics, materials, component anatomy, responsive behavior, motion fallback. Do not define task priorities or state flows, and do not substitute for human approval.
- **Visual direction comes first**: before freezing design-system facts, generate and compare 2–3 spatial visual directions; the selected direction becomes the "approved visual reference." Subsequent design-system sections record that direction and do not reinvent the aesthetic. A direction that only swaps color / theme / icons / copy is not substantially different.
- **The visual language is derived from project semantics**: brand personality, environment, mood, risk, content density, physical metaphor, domain symbols; different domains cannot simply swap colors, and at least two rejected visual directions must be recorded.
- **Visuals and components are the source of truth for the implementation handoff**: express them with structured data (dimensions, ratios, Grid, state tables), **not prose**. Prose like "magenta + square border = critical" cannot be consumed stably by engineering. Any design change must be reflected in the delivery facts (swapping a hex / family must show up in the structured fields).
- **Components are derived from tasks, data, and interactions**: domain knowledge only provides terminology and rules, not a catalog that must be reused. Each core component declares its source task, source data, purpose, anatomy (including layout and sizing), data bindings, variants, states, layout role, and accessibility.
- **Data is runtime fidelity**: display-only fields carry human-readable copy; status/enum fields are translated via the color-semantic `label` and never echo the machine enum.
- **Prohibitions**: presenting project-derived rules as official PICO rules; scoring by visual similarity to a reference case; disguising review metadata such as design theses / layer names / component classifications / skeleton region names as end-user copy.

## 1. Direct Description of Outputs

This role delivers: **visual direction candidates and the selection (approved visual reference) → visual language (tokens / typography / color semantics / materials) → component specs (structured anatomy) → data display and semantic contract**. Each section below is the structured description of these outputs.

## 2. Spatial Visual Direction Candidates (2–3)

> Each direction defines a spatial thesis, first-view composition, container relationships, depth plan, information hierarchy, interaction cues, spatial value, Dashboard risk, and preview/render instructions. Directions that only swap color are rejected.

| Direction | Spatial Thesis | First-View Composition | Container Relationships | Depth Plan | Information Hierarchy | Interaction Cues | Spatial Value | Dashboard Risk |
|---|---|---|---|---|---|---|---|---|
| A · Constellation Atelier | The real room is the canvas; each orb is a luminous landmark and the UI is a quiet instrument tray, never the main scene. | After entry, separated orb silhouettes or restore markers occupy the room; one compact control panel sits below the dominant gaze line; the selected orb has a thin double-ring and a small range shell. | One Shared Space entry surface leads to Stage Mixed. In Stage, one task panel follows a stable head-relative anchor while world-space orbs remain independent; destructive confirmation and photo countdown are transient overlays. | Room/orbs at environment depth; range shell around each orb; selected outline slightly in front; task panel at comfortable near depth; countdown/frame nearest only during photography. | 1 selected orb/result, 2 preset and tuning decision, 3 layout status/count, 4 recovery notices. World objects remain primary over controls. | Controller ray endpoint becomes a placement reticle; hovered orb gains outline + named chip; drag shows a short tether; long-press deletion uses a radial hold trace; locked state adds a lock glyph and disables target affordance. | Makes position, distance, overlap and atmosphere legible in situ while keeping parameter choices planar. | Low: the panel exposes only the current task and never becomes a multi-card dashboard. |
| B · Lighting Laboratory Rail | Treat the room as an experiment bench with a persistent horizontal sample rail of all eight slots and a fixed inspector. | A slot rail spans the lower field of view, with all presets, orb slots, live metrics and inspector visible simultaneously; placed orbs float above it. | Stage Mixed plus one wide persistent rail and inspector region; photo controls replace the rail. | Room/orbs behind; rail mid-near; inspector and alerts foreground. | 1 slot inventory, 2 inspector metrics, 3 world result. | Slot drag/drop, meter ticks, explicit selection tabs and numeric controls. | Good explicitness for capacity and comparison, but spatial creation competes with inventory management. | High: repeats flat control rows and dense results-monitor patterns adjacent to Hue/DIALux, increasing occlusion and CAD-like density. |
| C · Mood Story Arc | Treat each light as a beat in a visual story arranged along a curved sequence, optimized for switching between composed looks. | A curved ribbon of mood cards surrounds a central frame; orbs are grouped by scene step and previewed in sequence. | Stage Mixed with several world-attached story cards and a central scene controller; photography is the climax state. | Story arc at mid-depth; current mood card and frame near; orb clusters farther. | 1 active scene beat, 2 story order, 3 per-orb details. | Select steps on the arc, scrub between moods, reorder cards, then edit contained orbs. | Adds temporal/sequence value that could support future scene sets, but the requested product stores only one current layout. | Medium/high: multiple peripheral cards overload FOV and invent a sequence model absent from the requirements. |

Preview/render instructions:

- **A · Constellation Atelier**: render a realistic Passthrough room with three well-separated glowing spheres (warm amber, cool blue, magenta), restrained particles, translucent radius shells, a single selected double-ring, and one compact dark-backed SpatialUI instrument panel below center. Preserve real-room visibility; no ceiling grid, dashboard wall, or photometric heat map.
- **B · Lighting Laboratory Rail**: render the same room with an always-visible lower rail showing eight slots, meters and a side inspector; make the tradeoff in occlusion and information density obvious.
- **C · Mood Story Arc**: render a curved sequence of mood cards around the scene with step numbers and a central photo frame; emphasize peripheral attention cost and the unrequested timeline metaphor.

### 2.1 Structured design-effect review

| Criterion (1–5) | A · Constellation Atelier | B · Lighting Laboratory Rail | C · Mood Story Arc | Review basis |
|---|---:|---:|---:|---|
| Supports T3/T4/T5 embodied placement | 5 | 3 | 2 | Interaction §3–§4 selected concept |
| Keeps one primary focus and MR visibility | 5 | 2 | 2 | PM §4; UXR §4 anti-dashboard and safety contract |
| Distinct from adjacent flat/CAD visual patterns | 5 | 2 | 4 | UXR §3 visual-experience observations only; no composition/style copied |
| Requirement fit without invented workflow | 5 | 4 | 1 | `USER-PRD-001`; C invents multiple scene beats |
| Engineering/performance restraint | 4 | 4 | 2 | Fake glow/particles boundary; bounded panel/entity count |
| Accessibility/recovery clarity | 4 | 5 | 3 | Non-color outline/text, stable panel, explicit status |
| **Total / 30** | **28** | **20** | **14** | A wins without relying on color/theme differences |

- **Structured review verdict**: approve A. Its dominant artifact is the in-room lighting composition, its controls remain a single task-scoped instrument surface, and its visual hierarchy directly reflects the approved Direct Spatial Atelier concept. Approval is design-time only; compositor readability, FOV and particle comfort remain device-validation gaps.
- **Selected direction (approved visual reference)**: **A · Constellation Atelier**. All later layout, component, motion, preview and implementation facts must preserve: room-first composition, one compact instrument panel, restrained world cues, and a foreground photo overlay only while capturing.
- **Rejected visual directions (≥2)**: **B** is rejected because its persistent slot rail/inspector turns the room into a control dashboard and obscures Passthrough; **C** is rejected because it invents multi-scene sequencing and distributes controls across peripheral FOV. Their distinct information models—not colors or iconography—are the reasons for rejection.

## 3. Design Tokens (the single contract between design and code)

> tokens, typography, color semantics, and materials are the source of truth for styling that downstream implementers consume verbatim; values must be precise (colors in hex).

| Token | Value | Semantics / Usage |
|---|---|---|
| accent | `#B894FF` | Fixed decorative orb-selection accent; UI interaction routes through `PicoTheme.colorScheme.interaction` where a theme role exists |
| surface | `#10131A` | Stage solid-backing reference only; window root uses system `Material.Regular` glass |
| brandPrimary | `#8E7CFF` | RoomLightDesigner brand mark and non-semantic illustration only |
| labelOnDark | `#F7F7FB` | Fixed Stage-overlay text after Vibrant termination; Compose window text uses `labelPrimary*` roles |
| sunset | `#FF9A52` | Sunset orb core/fog |
| moon | `#88B8FF` | Moon orb core/stardust |
| neonA | `#FF65D5` | Neon orb primary glow |
| neonB | `#8B68FF` | Neon scan secondary glow |
| stageWhite | `#F8F7F2` | Stage spotlight core |
| radius | `s=12dp / m=20dp / l=32dp` | Inner controls/cards/window shell |
| spacing | `4dp base; 8dp rhythm` | All padding/gaps use §3.4 tiers |

### 3.1 Typography hierarchy

> Each level: `family (grotesk/sans/mono/serif) · size · line · weight`. Implementers infer the display/title/metric/body/caption roles by descending size + mono for domain neutrality, and do not rely on specific key names (domain-custom key names such as asset/decision can also be consumed).

| Role / Key | family | size | line | weight |
|---|---|---|---|---|
| display | sans | 34sp | 42sp | 600 |
| title | sans | 24sp | 32sp | 600 |
| metric | mono | 20sp | 28sp | 600 |
| body | sans | 16sp | 24sp | 500 |
| caption | sans | 13sp | 18sp | 500 |

### 3.2 Color semantics colorSemantics (dual-channel: color + shape)

> Each item: `color(#hex) · shape · label · desc · aliases[]`. `shape` takes `circle/square/triangle/dashed/diamond` (a color-independent redundant encoding, required for accessibility); `aliases[]` lists all aliases of that semantic in the data (including localized copy, such as "Out of Stock" or "Pending Lock") for machine matching; `label` is the human-readable copy shown in the runtime UI (such as "Critical"), which replaces the visible text when a data value is matched rather than echoing the machine enum.

| Semantic Key | color (#hex) | shape | label (human-readable copy) | desc | aliases[] (machine matching, including Chinese aliases) |
|---|---|---|---|---|---|
| selected | `#B894FF` | diamond | 已选中 | Current orb/choice; paired with double-ring or check | `["selected","active","已选中","当前"]` |
| ready | `#7DE3C3` | circle | 可放置 | Valid placement/capture-ready state | `["ready","valid","可放置","就绪"]` |
| locked | `#FFD36A` | square | 布局已锁定 | Layout protected; manipulation disabled | `["locked","layout_locked","布局已锁定","已锁定"]` |
| warning | `#FFB44A` | triangle | 需要注意 | Recoverable tracking/storage/capacity issue | `["warning","partial","capacity","需要注意","已达上限"]` |
| error | `#FF6B6B` | triangle | 操作失败 | Capture, permission, tracking or persistence failure | `["error","failed","permission_denied","操作失败","权限缺失"]` |
| disabled | `#AEB6C4` | dashed | 暂不可用 | Unavailable action with visible reason | `["disabled","unavailable","暂不可用","不可选择"]` |
| saved | `#7DE3C3` | circle | 已自动保存 | Latest committed layout persisted locally | `["saved","fresh","已自动保存","已保存"]` |

### 3.3 Materials

> Each item: `desc · treatment(matte/glass/opaque) · glassStyle(Thin/Regular/Thick/Thickest/none) · opacity`. The glass look is a **system capability of the PICO spatial platform**: the PICO Spatial SDK provides four glass background material tiers `Thin/Regular/Thick/Thickest` (increasing degree of blur behind the content, applied via `Modifier.backgroundMaterial(...)`; for a WindowContainer it is controlled by `enableMaterialBackground` and enabled by default). When `treatment=glass`, the `glassStyle` tier must be specified, and the implementer calls the system `Material.<tier>` directly at handoff. `matte`→ a solid card. The Web preview using `backdrop-filter: blur+semi-transparency` is only a preview approximation of the four-tier system glass, and is not equal to the real material on a PICO device.
>
> **Component-level backgrounds are optional, and a custom color and glass are mutually exclusive**: a component inside a window can have no background (none, falling directly onto the parent container), a custom color background (customColor, with the color set by the component, not limited to a solid color), **or** a glass background material (the four glassStyle tiers), but **the same component must not stack a custom color + glass at the same time**—pick one. The glass background material is **only available inside a WindowContainer**. Which one a component uses is declared in the "background" row of §5 "Anatomy · Internal Metrics".

| Material Name | desc | treatment | glassStyle | opacity |
|---|---|---|---|---|
| WindowRegular | System default window material; no application root background | glass | Regular | system |
| InnerFocusGlass | Selected inspector or modal focus within `W-SHELL`; never stacked with custom color | glass | Thick | system |
| InnerNeutral | Theme-routed business-card fill (`fillPrimary`/`fillSecondary`) | opaque | none | 1.0 |
| StageLabelBacking | Small solid neutral backing for world labels/countdown because Stage cannot rely on glass | matte | none | 0.88 |
| OrbGlow | Additive fake-glow material; decorative fixed preset color, not UI semantics | matte | none | preset-dependent |

### 3.4 Scale (spacing / corner radius / icons, unified baseline)

> Component metrics must reference a unified scale and must not each write their own set of magic numbers. Spacing is based on 4/8dp. All padding / gap / radius / iconSize in the §5 component metrics table and the §5.0 in-window layout should reference the tier names here or their dp values.

| Scale | Tier → Value (dp) | Usage |
|---|---|---|
| spacing | xs 4 / s 8 / m 16 / l 24 / xl 32 | Component padding, gap between components, margin |
| radius | s 12 / m 20 / l 32 | Buttons/chips, inner cards, window shell (32dp) |
| iconSize | s 20 / m 24 / l 32 | Status icons, action icons, effect glyphs |

## 4. Environment Adaptation Spec (hard spatial constraints)

- **No large blocks of high-saturation color in dark environments.**
- **Color does not carry semantics on its own**: color + shape/text dual-channel is mandatory.
- **Minimum font size and contrast at wearing distance**: 13sp caption is the project floor (above the 12dp platform floor); CJK body 16sp/500. Primary text targets 4.5:1 in the Web approximation and requires device inspection in bright/dark Passthrough.
- **Readability on glass / semi-transparent backgrounds**: `W-SHELL` keeps system `Material.Regular` root with no root paint; critical inspector/modal inner surfaces use exactly one `Material.Thick` or theme opaque fill, never both. Stage labels/countdown use `StageLabelBacking` because Stage glass is unavailable.
- **Vibrant Style**: app toggle remains on. Window title/body use theme roles equivalent to `Darkest`/`Dark`; inner neutral surfaces use `Neutral`; selected background uses `SemiLight`. Fixed orb colors and photo frame terminate Vibrant (`Vibrant.None/Termination`). Gradient/multicolor color-wheel and film texture have solid backings and never depend on Vibrant.
- **Spatial state and background controllability**: Shared and Stage Mixed backgrounds are uncontrollable Passthrough. `W-SHELL` therefore uses system glass + theme roles; world text uses solid backing. No Full Space opaque environment is introduced.
- **Environment adaptation**: bright room → strengthen outline/backing opacity and reduce additive bloom; dark room → reduce glow peak/particle density and avoid high-saturation panels; high-contrast clutter → thicken selection outline and keep labels backed; extreme outdoor brightness remains unsupported until device validation and shows a readability notice rather than false assurance.

## 5. Component Definition Spec (structured anatomy, no prose)

> Each core component declares: source task, source data, purpose, layout role, priority, anatomy (layout + sizing), data bindings, variants, states. The component description must contain explicit dimensions (ratio or fixed value) and internal structure.
>
> **Structure is incompressible**: the "Component" block below must be fully copied for each core component. Do not merge multiple base fields into one row, do not compress `anatomy.layout` / `sizing` / `metrics` into a field value, and do not rewrite `renderSpec` / `dataBindings` / `variants` / `states` as untitled path strings or state enums. The shared state table can only supplement, not replace, a component's dedicated state table. Stage / 3D components only swap Grid for world anchors, local coordinates, orientation, and metric ranges, but the 8-section structure must still be preserved.

### 5.0 Window structure and in-window layout (structure diagram + dashed boxes, required)

> Visual design must first make clear "what the window looks like and how things are arranged inside it," and only then drop down to individual components. This section carries the **window-level** structure diagram; the spatial placement geometry (anchor/x/y/w/h/z, attachment docking relationships) is authoritative in interaction spec §14, and this section only visualizes and measures the in-window 2D layout. Copy this block for each primary WindowContainer. Window dimensions must reference the PICO methodology result in interaction §9 (default / min / max, content area, reflow), and must not define a separate window size in the visual stage.

**Window shell**

| Field | Content |
|---|---|
| Window / container name | `W-SHELL` (Interaction §5.2) |
| form | Planar; depth fixed 640dp |
| Logical dimensions | Default 720×540dp (Interaction §5.4) |
| min / max | 520×380dp Constrained / 920×640dp Large |
| Content safe inset contentInset | top/right/bottom/left = m 16dp after 96dp title/system shell budget |
| Docked attachment | None; InlineControl only; `D-CLEAR` is transient AlertDialog |

**In-window layout structure diagram (ASCII; solid boxes = window/region boundaries, dashed boxes `┈`/`╌` = component placeholders)**

> The structure diagram must annotate: ① each region name and its embedded component names; ② the spacing tier between regions (referencing §3.4); ③ component placeholders are shown with dashed boxes, and solid boxes indicate window / region container boundaries.

```
┌──────────────────────────────────────────────┐ ← W-SHELL 720×540dp
│ Title/system shell 96dp · RoomLightDesigner │
│ contentInset m 16dp                          │
│ ┌──────────────────────────────────────────┐ │
│ │ R0 Guard 68dp (or Entry title 56dp)      │ │
│ │ ┌╌ WorkspaceGuardBar / EntryGate title ╌┐ │ │
│ │ └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘ │ │
│ └──────────────────────────────────────────┘ │
│                 ↕ gap m 16dp                 │
│ ┌──────────────────────────────────────────┐ │
│ │ R1 Main decision 328dp                   │ │
│ │ ┌╌ LightPalette / OrbInspector /       ╌┐ │ │
│ │ ┊   PhotoSetup (exactly one primary)     ┊ │ │
│ │ └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘ │ │
│ └──────────────────────────────────────────┘ │
└──────────────────────────────────────────────┘
```

- **Grid definition**: default workspace has two contiguous regions: R0 fixed 68dp and R1 328dp with one 16dp gap = 412dp safe height. `L-ENTRY` and `L-PHOTO-PREP` use their state-specific contiguous grids from Interaction §7.2 within the same 688×412dp area.
- **Region → component mapping**: workspace R0 → `WorkspaceGuardBar` (status + lock/clear/photo in one row); workspace R1 → exactly one of `LightPalette` or `OrbInspector`. EntryGate owns its whole L-ENTRY grid. PhotoSetup owns its whole L-PHOTO-PREP grid. RecoverySurface is modal. `PlacementGuide`, `LightOrbEntity` and `PhotoExperience` live in Stage.
- **Region spacing**: workspace R0↔R1 = m 16dp; internal choice gaps = s 8dp; two-column gap = m 16dp.
- **Reflow**: Large uses 68+16+428dp; Default 68+16+328dp; Constrained 56+8+188dp. EntryGate uses its independent 56+16+220+16+104dp grid. Components scroll/reflow internally; text and 56dp targets never globally scale.

### Component: EntryGate

| Field | Content |
|---|---|
| Source task derivedFromTasks | T0, T1 |
| Source data derivedFromData | `LayoutSnapshot.savedAt/schemaVersion/orbs`, `CapabilityState.stage/passthrough` |
| Purpose | Make Full Space entry explicit and disclose restore/capability status. |
| Layout role layoutRole | critical_primary |
| Priority | primary |
| Runtime role runtimeRole | entryDecision |

**Anatomy · Layout (anatomy.layout, Grid mode)**

```
┌────────────────────────────────────┐
│ ┌╌ title + passthrough badge ╌╌╌╌┐ │ row1 56
│ └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘ │
│ ┌╌ restore summary / guidance ╌╌╌┐ │ row2 220
│ └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘ │
│ ┌╌ Enter 56 ╌┐  ┌╌ Exit/Help 56 ╌┐ │ row3 104
└────────────────────────────────────┘
```

- **Grid definition**: 3 rows × 2 columns; title/body span both columns; actions align bottom/end; gaps m 16dp.

**Anatomy · Sizing**

| Tier | Width × Height (ratio or fixed value) | Notes |
|---|---|---|
| Large | 688×412dp centered within max 888×512dp safe area | Width/body cap avoids an oversized entry surface. |
| Regular | 688×412dp | Default 720×540dp window safe area exactly. |
| Compact | 488×252dp | Minimum 520×380dp window; body scrolls, one restore line. |
| Constrained | 488×252dp | Same legal minimum; primary action pinned, help becomes Link; no growth. |

**Anatomy · Internal metrics (metrics, dp/sp, no prose, reference the §3.4 scale)**

> The icons, text, corner radii, padding, element spacing, and strokes inside a component must be given explicit values (referencing the §3.4 tiers or dp/sp directly), and cannot merely say "icon a bit large" or "leave some margin." Font sizes reference the §3.1 typography roles; icons / spacing / corner radii reference §3.4. All metrics must, together with the `sizing` above, satisfy the content-area constraints of the owning window's default / min / max; text and hit targets must not be scaled as a whole under Compact / Constrained.

| Metric | Value | Source / Notes |
|---|---|---|
| background | none | Falls onto system `Material.Regular` root. |
| Corner radius radius | l 32dp | Window shell |
| Padding | m 16dp all | §3.4 |
| Internal element gap | s 8dp inline / m 16dp rows | §3.4 |
| Stroke | 0dp | Root glass provides boundary. |
| Icon iconSize | m 24dp | Badge/action icons |
| Primary text | title 24/32/600 | §3.1 |
| Value / secondary text | body 16/24/500; caption 13/18/500 | §3.1 |
| Minimum hit target hitTarget | 56×56dp | PICO floor |

**Render elements renderSpec.elements[] (ordered visible elements)**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `entry_title` | 房间灯光师 | `TitleBar/Text` | static | app identity |
| `entry_mode` | 混合现实 · 真实房间可见 | `Badge` | `capability.passthrough` | ready/error |
| `restore_summary` | 将恢复最近的布光方案 | `Text` | `snapshot.summary` | display |
| `enter_stage` | 进入房间布光 | `Button` | `capability.stageReady` | primary action |
| `entry_exit_help` | 退出 / 使用说明 | `Link/Button` | static/helpState | secondary action |
| `discard_invalid` | 忽略无效记录 | `Link` | `snapshot.invalidCount` | recovery |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `CapabilityState.stageReady` | `enter_stage.enabled`, `entry_mode` | disabled + “空间能力暂不可用” | semantic |
| `LayoutSnapshot.orbs.size` | `restore_summary.text` | “暂无已保存方案” | display-only |
| `LayoutSnapshot.invalidCount` | `discard_invalid.visible` | hidden when zero | semantic |
| `LayoutSnapshot.schemaVersion` | validation status / compatibility warning | incompatible → “方案版本不兼容，可忽略后继续” | semantic |
| `LayoutSnapshot.savedAt` | restore timestamp | “保存时间未知” | display-only |

**Variants**: `firstRun` shows concise safety guidance; `restoreAvailable` shows count/time; `restorePartial` adds invalid-record recovery.

**States** (rebuilt around gaze)

| State | Trigger | Visual params (fill/stroke/opacity/blur/material) | Size change | Motion continuity (duration ms + easing, aligned with the interaction doc) | Accessibility alternative |
|---|---|---|---|---|---|
| default | stage ready | system glass, primary button | none | none | explicit mode text |
| hover / focused | gaze on action | built-in hover/highlight | ≤1.04x built-in | M-HOVER | focus outline + label |
| pressed / selected | pinch/trigger | built-in pressed layer | none | built-in | haptic/audio |
| disabled | stage unavailable | disabled alpha + dashed icon + reason | none | none | “空间能力暂不可用” |
| loading | snapshot validation | progress + “正在检查方案” | none | indeterminate built-in | live text |
| empty | no snapshot | “从第一颗光球开始” | none | none | same text |
| error | corrupt/stage failure | error triangle + retry | none | none | error text |
| overflow | large text | body internal scroll | none | none | preserves 56dp target |

- **State stacking precedence**: error > loading > disabled > focused; focused never animates disabled action.

### Component: LightPalette

| Field | Content |
|---|---|
| Source task derivedFromTasks | T2, T3 |
| Source data derivedFromData | `PresetDefinition[]`, `InteractionState.pendingRecipe`, `orbCount` |
| Purpose | Choose one of four named recipes or a custom color, then start placement. |
| Layout role layoutRole | primary_explore |
| Priority | primary |
| Runtime role runtimeRole | decisionList |

**Anatomy · Layout (anatomy.layout, Grid mode)**

```
┌────────────────────────────────────┐
│ ┌╌ 日落 ╌┐ ┌╌ 月光 ╌┐             │ row1
│ ┌╌ 霓虹 ╌┐ ┌╌ 舞台 ╌┐             │ row2
│ ┌╌ 自定义颜色 / 色盘 ╌╌╌╌╌╌╌╌╌╌┐ │ row3
└────────────────────────────────────┘
```

- **Grid definition**: 3 rows × 2 equal columns; custom spans both; cell gap s 8dp; each recipe has effect glyph + name.

**Anatomy · Sizing**

| Tier | Width × Height | Notes |
|---|---|---|
| Large | 688×264dp centered within max R1 888×428dp | Choice grid stays width-capped; remaining space is layout whitespace/summary. |
| Regular | 688×264dp | Default R1 688×328dp; 64dp remains for guide/coachmark. |
| Compact | 488×124dp | Minimum R1 488×188dp; horizontal chips. |
| Constrained | 488×124dp | Same minimum R1; stable-ID scroll; no target shrink or growth. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | none | Root glass visible between options. |
| Corner radius radius | m 20dp | Option/ToggleableChip shell |
| Padding | s 8dp outer; m 16dp option | §3.4 |
| Internal element gap | s 8dp | glyph↔name |
| Stroke | 2dp selected semantic; 1dp default | dual-channel |
| Icon iconSize | l 32dp | effect glyph |
| Primary text | body 16/24/500 | recipe name |
| Value / secondary text | caption 13/18/500 | effect descriptor |
| Minimum hit target hitTarget | 56×56dp; regular cell ≥160×96dp | PICO floor |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `preset_sunset` | 日落 · 暖雾 | `Option/ToggleableChip` | `presets.sunset` | choice |
| `preset_moon` | 月光 · 星尘 | `Option/ToggleableChip` | `presets.moon` | choice |
| `preset_neon` | 霓虹 · 扫描 | `Option/ToggleableChip` | `presets.neon` | choice |
| `preset_stage` | 舞台 · 聚光 | `Option/ToggleableChip` | `presets.stage` | choice |
| `preset_custom` | 自定义颜色 | custom `ColorChoiceChip` | `pendingRecipe.color` | choice/action |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `PresetDefinition.id/name/effect` | option id/label/glyph | hide malformed preset and log | display-only |
| `InteractionState.pendingRecipe` | option.selected | none selected → idle | semantic |
| `orbCount` | all options.enabled | count≥8 → disabled + capacity reason | semantic |
| `layoutLocked` | all options.enabled | locked → disabled + lock reason | semantic |
| `customColor` | custom swatch | default brandPrimary | display-only |

**Variants**: `regularGrid`, `compactRail`, `customPickerExpanded` (solid-backed HSV wheel + confirm/cancel; not a new window).

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | no choice | theme neutral option | none | none | name + effect glyph |
| hover / focused | gaze | built-in Option/Chip hover | ≤1.04x built-in | M-HOVER | outline |
| pressed / selected | choose recipe | selected fill + 2dp diamond/check | none | 160ms | “已选中：日落”等 |
| disabled | locked/count=8 | disabled alpha + dashed capacity/lock icon | none | none | explicit reason |
| loading | presets initialize | 5 skeleton option shapes | none | progress | “正在准备预设” |
| empty | no valid presets | custom choice remains | none | none | custom path |
| error | custom picker invalid | error triangle + reset color | none | none | text error |
| overflow | large text/Compact | horizontal scroll | none | direct | scroll indicator |

- **State stacking precedence**: disabled > selected > focused; custom-picker error affects custom only; loading suppresses focus.

### Component: WorkspaceGuardBar

| Field | Content |
|---|---|
| Source task derivedFromTasks | T5, T6, T7, T8 |
| Source data derivedFromData | `orbCount`, `layoutLocked`, `PersistenceState`, `TrackingState`, `CaptureCapability` |
| Purpose | Show capacity/trust and expose lock, clear and photo actions without becoming a dashboard. |
| Layout role layoutRole | status |
| Priority | secondary |
| Runtime role runtimeRole | workspaceGuard |

**Anatomy · Layout (anatomy.layout, Grid mode)**

```
┌────────────────────────────────────┐
│ ┌╌ n/8+trust ╌┐ ┌╌ 锁定 ╌┐ ┌╌ 清空 ╌┐ ┌╌ 摄影 ╌┐ │ one 68dp row
└────────────────────────────────────┘
```

- **Grid definition**: one contiguous row; status group takes remaining width, then lock/clear/photo controls; s 8dp gap. Save/tracking compress to one highest-priority labeled badge.

**Anatomy · Sizing**

| Tier | Width × Height | Notes |
|---|---|---|
| Large | 888×68dp | Owns max-window R0 exactly. |
| Regular | 688×68dp | Owns default-window R0 exactly. |
| Compact | 488×56dp | Owns minimum-window R0 exactly; short status label. |
| Constrained | 488×56dp | Same fixed minimum; clear enters in-row overflow, lock/photo stay direct; no growth. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | none | System glass root |
| Corner radius radius | s 12dp controls | §3.4 |
| Padding | s 8dp all | §3.4 |
| Internal element gap | s 8dp | §3.4 |
| Stroke | 1dp dividerLine; 2dp semantic shape | dual-channel |
| Icon iconSize | m 24dp | lock/save/camera |
| Primary text | body 16/24/500 | actions |
| Value / secondary text | metric 20/28/600; caption 13/18/500 | n/8 + trust |
| Minimum hit target hitTarget | 56×56dp | Switch/buttons |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `orb_capacity` | 0/8 光球 | `NumberBadge/Text` | `orbCount` | capacity |
| `save_state` | 已自动保存 | `Badge` | `persistence.state` | saved/error |
| `tracking_state` | 空间稳定 | `Badge` | `tracking.state` | ready/warning |
| `layout_lock` | 锁定布局 | `Switch` | `layoutLocked` | protective toggle |
| `clear_all` | 清空 | `Button/IconButton` | `orbCount,layoutLocked` | destructive request |
| `photo_entry` | 摄影模式 | `Button` | `capture.capability` | secondary-flow action |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `orbCount` | capacity; clear/palette enabled | clamp visible 0–8; >8 error/recover | semantic |
| `layoutLocked` | switch + locked label | false if corrupt, announce recovery | semantic |
| `PersistenceState` | save badge | “仅保存在本次运行” on failure | semantic |
| `TrackingState` | tracking badge/actions | invalid → recovery state | semantic |
| `CaptureCapability` | photo button enabled | disabled + reason | semantic |

**Variants**: `workspace`, `locked` (lock status leads), `compact` (short labels), `photoPrepHeader` (only cancel/status).

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | fresh/unlocked | neutral status + direct actions | none | none | labels |
| hover / focused | gaze on action | built-in control hover | ≤1.04x built-in | M-HOVER | focus ring |
| pressed / selected | lock/action | pressed layer; lock square+text | none | M-LOCK | state audio/haptic |
| disabled | clear zero/locked; photo unavailable | disabled alpha + reason | none | none | visible reason |
| loading | save pending | progress + “正在保存” | none | built-in | text |
| empty | orbCount=0 | “0/8 · 添加第一颗” | none | none | guidance |
| error | save/tracking/capture | triangle + short recovery | none | none | exact error label |
| overflow | scaled text | status becomes semantic icon+short label; clear enters in-row overflow; actions do not shrink | none | none | full status announced; logical reading order |

- **State stacking precedence**: tracking error > save error > locked > focused; disabled destructive control never gains hover.

### Component: OrbInspector

| Field | Content |
|---|---|
| Source task derivedFromTasks | T4, T5 |
| Source data derivedFromData | `LightOrb[id,kind,color,brightness,radius,position]`, `InteractionState` |
| Purpose | Identify one selected orb and edit exactly color, brightness and radius in real time. |
| Layout role layoutRole | critical_primary |
| Priority | primary |
| Runtime role runtimeRole | detailPanel |

**Anatomy · Layout (anatomy.layout, Grid mode)**

```
┌────────────────────────────────────┐
│ ┌╌ name/kind ╌╌╌╌┐ ┌╌ delete hint ╌┐ │ row1
│ ┌╌ HSV wheel + swatch ╌┐ ┌╌ brightness slider ╌┐ │ row2
│ └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘ ├╌ radius slider ╌╌╌╌╌┤ │
│ ┌╌ 完成/返回调色盘 ╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┐ │ row3
└────────────────────────────────────┘
```

- **Grid definition**: 3 rows × 2 columns; header/actions span or split; color 240dp column, parameters remaining; m 16dp gap.

**Anatomy · Sizing**

| Tier | Width × Height | Notes |
|---|---|---|
| Large | 888×328dp within max R1 888×428dp | Two columns widen; content height stays capped to avoid sparse controls. |
| Regular | 688×328dp | Fits default workspace R1 exactly. |
| Compact | 488×188dp | Fits minimum R1 exactly; HSV becomes swatch strip; sliders scroll. |
| Constrained | 488×188dp | Same fixed minimum; full wheel replaces swatches inline; no growth. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | glass `Thick` | Inner focus surface only; no custom color stacked. |
| Corner radius radius | m 20dp | inner surface |
| Padding | m 16dp all | §3.4 |
| Internal element gap | s 8dp controls / m 16dp columns | §3.4 |
| Stroke | 2dp selected semantic around swatch; 1dp divider | dual-channel |
| Icon iconSize | m 24dp | back/delete/effect |
| Primary text | title 24/32/600 | selected name |
| Value / secondary text | metric 20/28/600; body 16/24/500 | values/labels |
| Minimum hit target hitTarget | 56×56dp | controls/thumbs |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `orb_identity` | 已选中 · 日落 | `Text/Badge` | `selected.kind` | selected identity |
| `color_control` | 颜色 | custom `HsvColorWheel` + swatch | `selected.color` | edit |
| `brightness_control` | 亮度 70% | `Slider` | `selected.brightness` | edit |
| `radius_control` | 范围 2.0m | `Slider` | `selected.radius` | edit |
| `hold_delete_hint` | 按住扳机 2 秒删除 | `Text/LinearProgressIndicator` | `deleteHold.progress` | destructive affordance |
| `deselect` | 完成 | `Button` | `selected.id` | action |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `selected.id/kind` | identity, stable key | missing → leave S3 to S1 | semantic |
| `selected.color` | wheel/swatches/world core | invalid → preset default + warning | display-only |
| `selected.brightness` | slider/value/world intensity | clamp 0–100% | display-only |
| `selected.radius` | slider/value/world shell | clamp 0.5–5.0m | display-only |
| `InteractionState.mode` | edit/drag/delete UI | invalid gesture → selected idle | semantic |
| `deleteHold.progress` | progress indicator | reset 0 on movement/release | semantic |

**Variants**: `preset`, `custom`, `compactSwatches`, `draggingReadOnly` (values live, controls temporarily disabled).

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | selected idle | Thick focus glass; values readable | none | none | name + numbers |
| hover / focused | gaze on control | built-in hover or custom `spatialHoverEffect` wheel | ≤1.04x | M-HOVER | focus outline |
| pressed / selected | edit control | pressed layer + selected diamond/check | none | direct | haptic + value text |
| editing | wheel/slider changes | active thumb/value + live world update | none | direct current-frame | numeric value |
| dragging | world drag begins | controls read-only; position not shown as editable number | none | M-DRAG world-only | “拖动中，松开完成” |
| delete-armed | stationary trigger hold | 0–100% hold progress + cancel text | none | M-HOLD-DELETE 2000ms | progress text |
| disabled | layout lock/dragging | disabled alpha + reason | none | none | “拖动中/布局已锁定” |
| loading | selected entity binding | small progress, retain identity | none | none | text |
| empty | selected id disappeared | auto-return to palette | component removed | 120ms fade | announce deselected |
| error | invalid value/save failure | error triangle; reset/retry | none | none | exact affected field |
| overflow | text scale | R1 internal scroll; controls stay 56dp | viewport fixed | none | logical focus order |

- **State stacking precedence**: entity missing > error > dragging disabled > focused; delete-hold progress overrides ordinary focused styling but not error.

### Component: LightOrbEntity

| Field | Content |
|---|---|
| Source task derivedFromTasks | T1, T3, T4, T5, T6 |
| Source data derivedFromData | `LightOrb`, `PresetDefinition`, `InteractionState`, `layoutLocked` |
| Purpose | Represent the portable mood light, fake glow/effect, reach shell and direct manipulation target in Stage. |
| Layout role layoutRole | primary_hero |
| Priority | primary |
| Runtime role runtimeRole | worldManipulable |

**Anatomy · Layout (anatomy.layout, world-space mode)**

```
Stage-local origin at LightOrb.position
        ┌╌ viewer-facing name chip ╌┐  y +0.10m
     (( ┌──────── core Ø0.12m ─────┐ )) selected double ring
        └ glow billboard / effect ─┘
     - - - influence shell radius 0.5–5.0m - - -
        └╌ drag tether to last pose ╌┘ (drag only)
```

- **Spatial region definition**: core at local (0,0,0); rings concentric; name chip viewer-facing above; shell visible only hover/selected; particle envelope capped by §7.4, not full radius.

**Anatomy · Sizing**

| Tier | Width × Height / metric | Notes |
|---|---|---|
| Regular | core Ø0.12m; target ≥0.18m; radius 0.5–5m | Normal Stage distance. |
| Compact | core still Ø0.12m; effect density low | Performance tier, semantic size unchanged. |
| Constrained | N/A for window; far targets may use ≥0.22m invisible collider | Improves ray acquisition without changing visible core. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | customColor preset glow material | Stage custom material; glass unavailable. |
| Corner radius radius | N/A · spherical entity | world geometry |
| Padding | name chip 8dp equivalent / 0.01m clearance | readability |
| Internal element gap | ring gap 0.02m; chip offset 0.10m | Interaction §7.2 |
| Stroke | selected 0.008m double ring; hover 0.005m single ring | dual-channel |
| Icon iconSize | effect glyph 24dp-equivalent in chip | world label |
| Primary text | body-equivalent 16/24/500 | name chip |
| Value / secondary text | caption-equivalent 13/18/500 | radius/hold status |
| Minimum hit target hitTarget | collider Ø≥0.18m | controller-ray target |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `orb_core` | — | sphere/mesh primitive | `orb.color,brightness` | decorative subject |
| `orb_glow` | — | billboard/additive shell | `orb.kind,color,brightness` | fake glow |
| `orb_particles` | — | bounded particle/effect primitive | `preset.effect,quality` | atmosphere |
| `orb_range` | 影响范围 2.0m | sparse shell/line | `orb.radius` | non-photometric reach |
| `orb_selection` | 已选中 · 日落 | double ring + solid-backed label | `orb.selected` | selected semantic |
| `orb_hold` | 松开取消 · 删除 | radial progress | `deleteHold.progress` | destructive progress |
| `orb_tether` | — | line primitive | `drag.current,lastValid` | drag relation |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `LightOrb.position` | entity transform | invalid restore skipped; invalid drag rollback | semantic |
| `LightOrb.color` | core/glow | preset default | display-only |
| `LightOrb.brightness` | emissive/fake-glow gain | clamp 0–1 | display-only |
| `LightOrb.radius` | range shell scale | clamp 0.5–5m | display-only |
| `LightOrb.kind` | effect/glyph/name | custom static glow | display-only |
| `InteractionState` | ring/tether/hold/target | idle on invalid transition | semantic |
| `layoutLocked` | targetability/lock cue | locked consumes selection | semantic |

**Variants**: `sunset` warm fog; `moon` cool stardust; `neon` dual-color scan; `stage` white sharp cone/edge; `custom` static colored halo with restrained neutral particles.

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | placed/unlocked | core + bounded effect; no shell | none | preset effect | named preset in panel |
| hover / focused | ray/gaze hit | single outline + name chip | visual ≤1.04x | M-HOVER | outline/text |
| pressed / selected | trigger click | double ring + range shell + inspector | none | M-SELECT | “已选中” label |
| dragging | trigger hold + ≥0.03m movement | tether + live pose; particles suppressed | none | M-DRAG 1:1 | “拖动中” chip |
| delete-armed | stationary hold <0.03m | radial 0–100% + “松开取消” | none | exactly 2000ms | textual percent/time |
| disabled | layout locked | no hit target; lock square chip on attempted action | none | none | lock text |
| loading | restore/entity creation | ghost core + progress chip | none | M-SPAWN on ready | “正在恢复光球” |
| empty | entity removed | no render | removed | 120ms fade except Reduce Motion | deletion announcement |
| error | resource/effect failure | static colored sphere + warning chip | none | none | functionality retained |
| overflow | >8 or effect budget | reject 9th; reduce particles for existing 8 | semantic size unchanged | quality fallback | capacity text |

- **State stacking precedence**: error fallback keeps selected/locked semantics; locked suppresses hover/pressed; dragging suppresses delete progress; selected overrides hover ring but retains focus brightness.

### Component: PhotoSetup

| Field | Content |
|---|---|
| Source task derivedFromTasks | T8 |
| Source data derivedFromData | `PhotoSession.frameStyle/countdownSeconds`, `CaptureCapability` |
| Purpose | Choose frame and countdown before editing UI/ray are hidden. |
| Layout role layoutRole | critical_primary |
| Priority | primary |
| Runtime role runtimeRole | captureSetup |

**Anatomy · Layout (anatomy.layout, Grid mode)**

```
┌────────────────────────────────────┐
│ ┌╌ permission/status ╌╌╌╌╌╌╌╌╌╌╌┐ │ row1
│ ┌╌ 白边 ╌┐ ┌╌ 胶片 ╌┐ ┌╌ 拍立得 ╌┐ │ row2
│ ┌╌ 3秒 ╌┐  ┌╌ 5秒 ╌┐  ┌╌ 10秒 ╌┐ │ row3
│ ┌╌ 取消 ╌┐       ┌╌ 开始摄影 ╌╌╌┐ │ row4
└────────────────────────────────────┘
```

- **Grid definition**: 4 rows × 3 equal choice columns; status/actions span; s 8dp choice gap, m 16dp row gap.

**Anatomy · Sizing**

| Tier | Width × Height | Notes |
|---|---|---|
| Large | 688×364dp centered within max 888×512dp safe area | Width capped so choices remain grouped; additional space stays clear. |
| Regular | 688×364dp | Fits default 688×412dp safe area with 48dp reserve. |
| Compact | 488×252dp | Fits minimum safe area exactly; choices use chips/previews become icons. |
| Constrained | 488×252dp | Same minimum; internal scroll if text scales; start pinned; no growth. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | glass `Thick` | Focus surface; no custom fill. |
| Corner radius radius | m 20dp | setup surface/options |
| Padding | m 16dp | §3.4 |
| Internal element gap | s 8dp / m 16dp rows | §3.4 |
| Stroke | 2dp selected diamond/check; 1dp default | dual channel |
| Icon iconSize | l 32dp frame glyph; m 24dp status | §3.4 |
| Primary text | title 24/32/600 | heading/start |
| Value / secondary text | body 16/24/500; caption 13/18/500 | choices/status |
| Minimum hit target hitTarget | 56×56dp | choices/actions |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `capture_status` | 可保存到系统相册 | `Badge/Text` | `capture.capability` | ready/error |
| `frame_white` | 简约白边 | `Option` | `frameStyle.WHITE` | choice |
| `frame_film` | 胶片黑边 | `Option` | `frameStyle.FILM` | choice |
| `frame_instant` | 拍立得质感 | `Option` | `frameStyle.INSTANT` | choice |
| `timer_segments` | 3秒 / 5秒 / 10秒 | `SegmentControl` | `countdownSeconds` | choice |
| `photo_start` | 开始摄影 | `Button` | `capture.capability` | primary action |
| `photo_cancel` | 取消 | `Button/Link` | static | secondary action |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `PhotoSession.frameStyle` | selected frame option | WHITE | semantic |
| `PhotoSession.countdownSeconds` | selected segment | 3; reject other values | semantic |
| `CaptureCapability.permission` | status/start enabled | permission guidance | semantic |
| `CaptureCapability.publicPathAvailable` | start enabled | disabled + honest limitation | semantic |

**Variants**: `ready`, `permissionRequired`, `emulatorLimited`; structure unchanged so failure cannot erase choices.

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | capability ready | Thick glass + one selected frame/timer | none | none | labels + glyphs |
| hover / focused | gaze | built-in Option/Segment/Button hover | ≤1.04x | M-HOVER | outline |
| pressed / selected | choose | check/diamond + selected fill | none | M-FRAME preview crossfade | selected label |
| disabled | capture unavailable | start disabled + reason | none | none | exact limitation |
| loading | permission check | progress + keep choices | none | built-in | text |
| empty | no capability data | start disabled; “正在检查” | none | none | same |
| error | permission/storage | triangle + retry/settings | none | none | text/action |
| overflow | text scale | internal scroll, action pinned | none | none | logical order |

- **State stacking precedence**: error/permission > loading > selected > focused; selected settings remain visible under capability errors.

### Component: PhotoExperience

| Field | Content |
|---|---|
| Source task derivedFromTasks | T8, T9, T10 |
| Source data derivedFromData | `PhotoSession`, `CaptureResult`, `panelVisible`, `controllerRayVisible` |
| Purpose | Render the clean MR frame, central countdown and capture result while guaranteeing trigger exit. |
| Layout role layoutRole | primary_hero |
| Priority | primary |
| Runtime role runtimeRole | captureOverlay |

**Anatomy · Layout (anatomy.layout, world/head-relative mode)**

```
┌╌ chosen frame inside compositor-safe bounds ╌┐
│                                              │
│            ┌╌ countdown 3/2/1 ╌┐             │ center
│            └╌ result check/error ╌┘           │
│            passthrough + orbs only            │
└╌ no editor UI · no controller ray ╌╌╌╌╌╌╌╌╌╌┘
```

- **Spatial region definition**: head-relative frame follows compositor-safe bounds; countdown centered in clear FOV; result below center; orbs remain world-relative; trigger input is invisible but taught before entry.

**Anatomy · Sizing**

| Tier | Width × Height / metric | Notes |
|---|---|---|
| Regular | frame = calibrated compositor bounds; countdown ≤8° visual angle | Device default. |
| Compact | N/A; not tied to W-SHELL resize | HMD/compositor controlled. |
| Constrained | Safe bounds inset increases 5%; frame texture reflows, never crops silently | Permission/device fallback. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | none | Stage root remains unpainted; Passthrough visible. |
| Corner radius radius | white 0; film 0; instant 12dp-equivalent | frame variants |
| Padding | safe inset device-calibrated; Web preview 32px equivalent | not a window metric |
| Internal element gap | result 16dp-equivalent below countdown | §3.4 |
| Stroke | white 0.012m-equivalent; film 0.020m; instant bottom 0.06m-equivalent | visual frame |
| Icon iconSize | l 32dp-equivalent result glyph | Stage solid-backed overlay |
| Primary text | display 34/42/600 | countdown |
| Value / secondary text | body 16/24/500 | result/error |
| Minimum hit target hitTarget | full trigger input; no visible target | controller contract |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `photo_frame` | — | head-relative frame primitive | `frameStyle` | captured decoration |
| `countdown_value` | 3 | solid-backed Text | `countdownTick` | temporal focus |
| `capture_success` | 已保存到系统相册 | icon + Text | `CaptureResult.uri` | saved |
| `capture_error` | 保存失败 · 返回设置重试 | triangle + Text | `CaptureResult.error` | error |
| `editor_visibility_guard` | — | render assertion | `panelVisible,rayVisible` | must both be false |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `PhotoSession.frameStyle` | frame primitive | WHITE | semantic |
| `PhotoSession.countdownTick` | numeral/visibility | hide outside countdown | display-only |
| `CaptureResult.status/uri/error` | result label/semantic | explicit unknown failure | semantic |
| `panelVisible` | editor guard | force false in photo state | semantic |
| `controllerRayVisible` | ray guard | force false in photo state | semantic |

**Variants**: `whiteBorder`, `filmBlackBorder`, `instant`; `ready`, `countdown`, `success`, `failure` share the same safe bounds.

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | S8 photo ready | frame only | none | M-FRAME | frame-name taught before entry |
| countdown | S9 tick active | fixed-center numeral; editor/ray guard false | none | M-COUNTDOWN at 1Hz | numeral announced once per tick |
| capturing | tick reaches zero | small progress/result reserve; frame stays | none | no flash | “正在保存” |
| success | public save result returned | check + “已保存到系统相册” | none | M-CAPTURE | success text/haptic |
| hover / focused | N/A clean view | no hover/ray | none | none | trigger exit instruction memorized |
| pressed / selected | trigger | exit photo; frame fades | none | 180ms or instant | haptic exit |
| disabled | capture pending non-cancellable | frame + “正在保存” result area | none | progress | trigger restores UI after callback |
| loading | capture call | small progress, no editor | none | built-in-like | text |
| empty | no frame resource | safe white border generated | none | none | no blank state |
| error | capture failure | solid-backed error + restore controls | none | M-CAPTURE | exact recovery text |
| overflow | long OS error | truncate to one line; full detail in restored panel | none | none | accessible detail after exit |

- **State stacking precedence**: error > loading > countdown > ready; trigger exit preempts all cancellable states; guard forces editor/ray hidden in every photo state.

### Component: RecoverySurface

| Field | Content |
|---|---|
| Source task derivedFromTasks | T0, T5, T9, T10 |
| Source data derivedFromData | `RecoveryIssue`, `orbCount`, `TrackingState`, `CaptureResult`, `PersistenceState` |
| Purpose | Confirm destructive clear or explain the smallest safe recovery without losing layout context. |
| Layout role layoutRole | critical_primary |
| Priority | primary |
| Runtime role runtimeRole | recoveryDialog |

**Anatomy · Layout (anatomy.layout, Grid mode)**

```
┌────────────────────────────────────┐
│ ┌╌ semantic icon + title ╌╌╌╌╌╌╌┐ │ row1
│ ┌╌ reason / impact / preserved data ╌┐ │ row2
│ ┌╌ cancel/return ╌┐ ┌╌ retry/clear ╌┐ │ row3
└────────────────────────────────────┘
```

- **Grid definition**: 3 rows × 2 action columns; title/body span; primary recovery on end; destructive action always second confirmation.

**Anatomy · Sizing**

| Tier | Width × Height | Notes |
|---|---|---|
| Large | 520×240dp centered within max 888×512dp safe area | Dialog width cap preserves focus. |
| Regular | 520×240dp | Fits default 688×412dp safe area. |
| Compact | 456×216dp | Fits minimum 488×252dp safe area; body max 3 lines. |
| Constrained | 456×184dp minimum | Fits same minimum with 16dp side and ≥34dp vertical margin; actions remain 56dp. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | glass `Thickest` | Focused modal; no custom fill. |
| Corner radius radius | l 32dp | AlertDialog shell |
| Padding | l 24dp | §3.4 |
| Internal element gap | s 8dp inline / m 16dp rows | §3.4 |
| Stroke | 2dp semantic shape in icon only | non-color cue |
| Icon iconSize | l 32dp | warning/error/destructive |
| Primary text | title 24/32/600 | title |
| Value / secondary text | body 16/24/500; caption 13/18/500 | explanation |
| Minimum hit target hitTarget | 56×56dp | actions |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `recovery_title` | 清空全部 4 个光球？ | `AlertDialog` title/Text | `issue.title` | warning/error |
| `recovery_icon` | 警告 / 操作失败 | semantic `Icon` | `issue.type` | triangle/square/circle non-color cue |
| `recovery_body` | 此操作无法撤销，已保存方案也会更新。 | Text | `issue.detail` | impact |
| `recovery_preserved` | 当前布局仍保留 | Badge/Text | `issue.preservedData` | trust |
| `recovery_cancel` | 取消 | Button | static | safe action |
| `recovery_primary` | 清空 / 重试 / 返回入口 | Button | `issue.action` | context action |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `RecoveryIssue.type` | title/icon/actions | generic error + return | semantic |
| `RecoveryIssue.detail` | body | “发生未知问题” | display-only |
| `RecoveryIssue.preservedData` | preserved label | never claim preserved if unknown | semantic |
| `orbCount` | clear title | clamp 0–8; zero closes dialog | display-only |
| `TrackingState/CaptureResult/PersistenceState` | issue mapping | source-specific recovery | semantic |

**Variants**: `clearConfirm`, `trackingLost`, `captureFailed`, `permissionDenied`, `saveFailed`, `invalidRestore` with action labels bound per issue.

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | recoverable issue | Thickest glass + semantic shape/text | none | 180ms appear | screen-reader focus title |
| hover / focused | gaze action | built-in Button hover | ≤1.04x | M-HOVER | focus ring |
| pressed / selected | action | pressed layer | none | built-in | haptic/audio |
| disabled | retry unavailable | disabled action + reason | none | none | reason text |
| loading | retry/save | progress; cancel rules explicit | none | built-in | status text |
| empty | issue resolved externally | close and return prior safe state | removed | 120ms | announcement |
| error | retry failed | update same dialog with exact source | none | none | no dialog stacking |
| overflow | long diagnostics | short summary + detail scroll/link | none | none | full accessible detail |

- **State stacking precedence**: new safety error replaces older issue; loading suppresses focus; cancel remains available unless platform call is non-cancellable; no stacked dialogs.

### Component: PlacementGuide

| Field | Content |
|---|---|
| Source task derivedFromTasks | T3 |
| Source data derivedFromData | `InteractionState.pendingRecipe/rayVisible/activeController`, `PlacementPose`, `TrackingState`, `orbCount`, `layoutLocked` |
| Purpose | Show exactly where the pending orb will appear and whether trigger commit is legal. |
| Layout role layoutRole | critical_primary |
| Priority | primary |
| Runtime role runtimeRole | placementPreview |

**Anatomy · Layout (anatomy.layout, world-space mode)**

```
controller ray ───────────────► PlacementPose
                              ┌╌ reticle Ø0.10m ╌┐
                              │ ghost orb Ø0.12m │
                              └╌ valid/blocked chip ╌┘ +0.10m
```

- **Spatial region definition**: one ray endpoint pose; reticle and ghost share origin; label viewer-facing above; only one PlacementGuide exists; no surface snap/plane claim.

**Anatomy · Sizing**

| Tier | Width × Height / metric | Notes |
|---|---|---|
| Regular | reticle Ø0.10m; ghost Ø0.12m; collider N/A | Normal Stage placement. |
| Compact | same semantic size; ghost effect density low | Performance tier. |
| Constrained | label may collapse to glyph + short text | Far/complex background; reticle stays 0.10m. |

**Anatomy · Internal metrics (metrics)**

| Metric | Value | Source / Notes |
|---|---|---|
| background | customColor ghost preset + matte 0.88 label backing | Stage; no glass. |
| Corner radius radius | label s 12dp-equivalent | §3.4 |
| Padding | label s 8dp-equivalent | §3.4 |
| Internal element gap | reticle↔label 0.10m | Interaction §7.2 |
| Stroke | ready 0.006m circle; blocked 0.006m dashed | color-independent shape |
| Icon iconSize | m 24dp-equivalent | ready/blocked glyph |
| Primary text | body-equivalent 16/24/500 | “扣动扳机放置” |
| Value / secondary text | caption-equivalent 13/18/500 | blocked reason |
| Minimum hit target hitTarget | N/A; endpoint follows controller ray | commit uses trigger |

**Render elements renderSpec.elements[]**

| id | Visible label | Element type | Binding bind | State / semantic role |
|---|---|---|---|---|
| `placement_ray` | — | public SDK controller-ray visual | `rayVisible,activeController,pose` | direction/input ownership |
| `placement_reticle` | — | circle/dashed reticle | `placement.valid` | ready/disabled |
| `placement_ghost` | — | translucent orb primitive | `pendingRecipe,pose` | preview |
| `placement_prompt` | 扣动扳机放置 | solid-backed Text/Icon | `placement.valid` | action instruction |
| `placement_block_reason` | 已达 8 个上限 / 空间跟踪不可用 / 布局已锁定 | Text | `placement.reason` | disabled explanation |

**Data bindings dataBindings[]**

| Source path | Target element / property | fallback behavior | display-only / semantic |
|---|---|---|---|
| `InteractionState.pendingRecipe` | ghost color/effect | missing → cancel S2 | display-only |
| `InteractionState.rayVisible/activeController` | placement ray visibility/source | S2 defaults visible with current controller; photo mode forces hidden | semantic |
| `PlacementPose.position` | ray endpoint + guide transform | non-finite/invalid → blocked; retain last visual endpoint only | semantic |
| `PlacementPose.valid` | reticle/prompt/commit gate | false | semantic |
| `TrackingState` | validity/reason | invalid → blocked + recovery | semantic |
| `orbCount` | validity/reason | ≥8 → blocked + capacity text | semantic |
| `layoutLocked` | validity/reason | true → blocked + lock text | semantic |

**Variants**: `sunset`, `moon`, `neon`, `stage`, `custom` ghost appearance; `valid`, `trackingBlocked`, `capacityBlocked`, `lockBlocked` behavior.

**States**

| State | Trigger | Visual params | Size change | Motion | Accessibility alternative |
|---|---|---|---|---|---|
| default | S2 valid pose | solid circle + translucent ghost + action text | none | direct endpoint | ready circle/text |
| hover / focused | N/A endpoint follows ray | no separate hover | none | direct | prompt |
| pressed / selected | trigger commit | ghost solidifies into LightOrbEntity | none | M-SPAWN | placement haptic |
| disabled | tracking/count/lock invalid | dashed reticle; ghost low opacity; reason | none | none | exact text/glyph |
| loading | recipe/resource prepare | reticle + progress; commit disabled | none | none | “正在准备光球” |
| empty | no pending recipe | component absent; return S1 | removed | none | selection retained |
| error | effect resource failure | static-color ghost still placeable + warning | none | none | fallback text |
| overflow | multiple pointers/events | accept active controller only; one guide | none | none | active hand/controller label if needed |

- **State stacking precedence**: trackingBlocked > capacityBlocked > lockBlocked > resource warning > valid; commit state atomically replaces guide with one LightOrbEntity.

### 5.1 Component structure completeness checklist (before coverage reconciliation)

> Verify the fixed structure component by component. If any column for any core component is "no" or the corresponding section anchor is missing, this stage's verdict can only be `block`; "the information already appears elsewhere," "shared states are already defined," or "limited space" must not be used as a reason to pass.

| Core Component | Base fields on separate rows | anatomy.layout | sizing | metrics | renderSpec | dataBindings | variants | states + stacking precedence | Verdict |
|---|---|---|---|---|---|---|---|---|---|
| EntryGate | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| LightPalette | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| WorkspaceGuardBar | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| OrbInspector | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| LightOrbEntity | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| PlacementGuide | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| PhotoSetup | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| PhotoExperience | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| RecoverySurface | yes | yes | yes | yes | yes | yes | yes | yes | pass |

### 5.2 Coverage reconciliation (performed after structure is complete)

#### Table A · Data entity → component binding

| Data entity / decision variable (referencing the UXR domain model) | Timeliness | Consuming component.dataBinding | Presentation / semantic method | Gap handling (add binding / intentionally not presented + rationale) |
|---|---|---|---|---|
| `LightOrb.id/kind/color/brightness/radius/position/selected` | current frame for edits; committed for save | LightOrbEntity + OrbInspector bindings | world transform/effect plus named inspector; selected semantic | fully bound |
| `LayoutSnapshot.orbs/savedAt/schemaVersion` | debounced after commit + lifecycle | EntryGate + WorkspaceGuardBar | restore summary, schema compatibility, saved/partial/error status | schemaVersion validates but is intentionally not shown raw; mismatch shows human compatibility warning; invalid coordinates omitted with visible count |
| `InteractionState` | event-driven, never persisted active | LightPalette + OrbInspector + LightOrbEntity | pending/selected/drag/hold/locked rendering | fully bound |
| `PresetDefinition/customColor` | static/local current | LightPalette + LightOrbEntity | named choice/effect glyph/fixed decorative color | fully bound |
| `orbCount` | current frame | LightPalette + WorkspaceGuardBar + RecoverySurface | n/8 and boundary-disabled state | fully bound |
| `layoutLocked/panelVisible` | current; lock persisted | WorkspaceGuardBar + LightOrbEntity + PhotoExperience guard | square+text/targetability and photo UI invariant | fully bound |
| `PhotoSession.frameStyle/countdownSeconds/tick` | current + 1Hz tick | PhotoSetup + PhotoExperience | choices/frame/countdown | fully bound |
| `CaptureCapability/CaptureResult` | permission/callback asynchronous | PhotoSetup + PhotoExperience + RecoverySurface | ready/permission/error/saved semantics | fully bound; URI intentionally not shown except success confirmation because path is not a user decision |
| `TrackingState` | event-driven | WorkspaceGuardBar + RecoverySurface + LightOrbEntity | ready/warning/error and interaction freeze | fully bound |
| `PersistenceState` | after each save attempt | WorkspaceGuardBar + RecoverySurface | saving/saved/error with honest fallback | fully bound |

#### Table B · Task decision output → component interaction

| Task ID · decision output | read-only / actionable | Consuming component + `renderSpec` element + interaction behavior | Gap handling |
|---|---|---|---|
| T0 · enter Stage or leave | actionable | EntryGate `enter_stage` Button invokes TR-ENTER; system close leaves | covered |
| T1 · accept/reset restored layout | actionable | EntryGate `restore_summary/discard_invalid`; LightOrbEntity restore states | covered |
| T2 · choose preset/custom | actionable | LightPalette option/custom elements select pending recipe | covered |
| T3 · commit/cancel placement | actionable | LightPalette initiates; PlacementGuide `placement_reticle/ghost/prompt` binds pose/validity and controller trigger commits | covered |
| T4 · edit color/brightness/radius | actionable | OrbInspector color/slider elements update LightOrbEntity in current frame | covered |
| T5 · move/delete/clear | actionable | LightOrbEntity drag/hold elements; WorkspaceGuardBar `clear_all`; RecoverySurface confirm | covered |
| T6 · lock/unlock | actionable | WorkspaceGuardBar `layout_lock` Switch changes legal transitions | covered |
| T7 · panel show/hide | actionable | controller menu transition; WorkspaceGuardBar reflects state | intentionally controller-mapped because panel is the hidden object; no unreachable in-panel toggle |
| T8 · frame/countdown/photo entry | actionable | PhotoSetup options/segments/start | covered |
| T9 · capture/save/retry | actionable | PhotoExperience countdown/result + RecoverySurface retry | covered |
| T10 · exit photo/Stage | actionable | trigger exits PhotoExperience; system back/Stage close returns EntryGate | covered |

#### Table C · Exhaustive sub-states of primary components

| Primary component → sub-component | Runtime sub-states (loading / buffering / dragging or editing / empty / error / boundary-disabled and project-specific states) | Corresponding render primitive | Data binding |
|---|---|---|---|
| EntryGate → enter action/status/body | loading, ready, disabled, no snapshot, partial restore, corrupt snapshot, Stage-open error, large-text overflow | Button/Badge/progress/Text/Link | capability + snapshot bindings |
| LightPalette → preset/custom choices | loading, no valid preset, focused, selected, locked, capacity=8, picker error, compact overflow | Option/ToggleableChip/custom wheel/scroll indicator | preset/pendingRecipe/orbCount/lock |
| OrbInspector → identity/wheel/sliders/hold | binding, selected, editing, dragging read-only, delete progress, missing entity, invalid value, overflow | Badge/wheel/Slider/progress/internal scroll | LightOrb + InteractionState |
| LightOrbEntity → core/effect/shell/target | restoring, default, focused, selected, dragging, delete-armed, locked, resource fallback, max-capacity | ECS primitives/rings/tether/progress/label | LightOrb + preset + interaction + lock |
| PlacementGuide → reticle/ghost/prompt | valid, commit, tracking-blocked, capacity-blocked, lock-blocked, loading, no recipe, resource fallback, multiple-pointer overflow | reticle/ghost/Text/Icon/progress | pendingRecipe + PlacementPose + tracking + count + lock |
| PhotoSetup → choices/start | checking, ready, selected, permission denied, public-path unavailable, error, overflow | Option/SegmentControl/Button/Badge/progress | PhotoSession + CaptureCapability |
| PhotoExperience → frame/countdown/result/guard | ready, countdown, capturing, non-cancellable, success, failure, missing frame, long-error overflow | frame/Text/progress/icon/guard assertion | PhotoSession + CaptureResult + UI/ray visibility |
| RecoverySurface → reason/actions | default, focused, disabled retry, loading, externally resolved, retry error, long detail | AlertDialog/Text/Button/progress/scroll | RecoveryIssue and source states |

## 6. Material and depth semantics

- **Material / glass tier / opacity per layer**: `W-SHELL` uses one system Regular root; inspector/setup use one Thick inner material; modal uses Thickest; world labels use 0.88 solid backing; Stage root none.
- **Depth cues**: room/orbs are environmental subject; selected rings/name are a small forward cue; `W-SHELL` is supporting near layer; modal/countdown is nearest temporary focus. Planar 2D remains at the back of its fixed 640dp box; no color-only depth encoding.
- **Mapping of system glass tiers to depth layers**: Regular = persistent window shell; Thick = current in-window inspector/setup; Thickest = focused confirmation/recovery. Stage never uses glass and uses bounded solid backings for text.

| Layer | Material treatment | glassStyle | opacity | Content carried | Meets contrast |
|---|---|---|---|---|---|
| Confirmation/recovery | glass | Thickest | system | destructive/safety decision | yes with theme labels + semantic shape/text |
| Inspector/photo setup | glass | Thick | system | forms/current decision | yes with theme labels; device-check pending |
| W-SHELL persistent | glass | Regular | system | entry/palette/status | yes via PicoTheme/Vibrant; device-check pending |
| Stage world labels/countdown | matte solid | none | 0.88 | orb name/radius/countdown/result | yes target; device-check pending |
| Orb glow/effects | custom fake-glow | none | bounded | atmosphere only | not used for text/semantics |
| Passthrough environment | none | none | system | real room | uncontrollable |

- **passthrough / MR readability adjudication**: Entry/palette/status stay on system Regular glass with theme labels. Forms use Thick. Dialog uses Thickest. World label/countdown use solid backing. Selection/lock/error pair color with ring/square/triangle and text. No TabBar is used, so its known glass issue is not applicable; if later introduced, fall back to a solid theme surface after disabling its material.
- **Vibrant Style application list**: Window content inherits PicoTheme adaptive roles. Fixed orb/frame colors terminate propagation. Color wheel/film texture sit on solid inner backing because Vibrant does not support gradients/images.

| Element / panel | Background controllability (Full/Shared/MR) | Vibrant tier (darkest→ultralight/none) | Propagation / termination | Fallback (solid backing / thicker glass) |
|---|---|---|---|---|
| W-SHELL title/body | Shared + MR uncontrollable | Darkest / Dark | inherit from PicoTheme | system Regular glass |
| Inner option surfaces | MR uncontrollable | Neutral; selected SemiLight | local override | theme opaque inner fill or Thick glass, never both |
| Inspector/setup labels | MR uncontrollable | Darkest/Dark | inherit within Thick inner material | Thick material |
| HSV wheel and frame previews | MR uncontrollable | none | terminate at container | solid neutral backing |
| Orb fixed colors/effects | Stage MR uncontrollable | none | fixed color / termination | no semantic text depends on them |
| World name/radius/countdown/result | Stage MR uncontrollable | none | terminated fixed label color | `StageLabelBacking` 0.88 |

## 7. Data display and semantic contract

> Declare how data is converted into user-visible UI.

- **Display-only paths displayOnlyPaths[]**: `orb.kindLabel` (“日落”), `orb.colorHex` only in diagnostics, `brightness` (“70%”), `radius` (“2.0m”), `snapshot.savedAt` (localized time), `countdownTick` (“3”), `capture.albumName` (“RoomLightDesigner”). Raw ids/URIs are never primary UI copy.
- **Semantic enum paths semanticEnumPaths[]**: `interaction.selected`→selected; `layoutLocked`→locked; `placement.valid`→ready/disabled; `persistence.state`→saved/warning/error; `tracking.state`→ready/warning/error; `capture.status`→ready/saved/error; `permission.state`→error label “权限缺失”. Values map through §3.2 aliases to human labels.
- **Data states**: `loading` during restore/permission/capture/save; `fresh` when current in-memory and last save agree; `aging` while debounced save pending; `partial` when some restored records are invalid; `conflicting` only if schema/duplicate IDs require dropping records; `permission_denied`; `error`. `offline` is intentionally not presented because the app has no network source. `stale` means saved coordinates came from a prior Stage session and are shown as “上次方案，位置需确认”, never as anchored truth.
- **Trust policy trustPolicy**: current-frame parameter/world feedback is authoritative for the session; persisted freshness is visible; failed save never claims success; old coordinates never claim real-world anchor persistence; capture success requires a returned public save result/URI; all alerts identify source (空间跟踪/相册权限/本地保存).

| Display format rules formattingRules | Input path | Output format | fallback | Applicable data states |
|---|---|---|---|---|
| Brightness percent | `LightOrb.brightness` | rounded integer `0–100%` | `—` | fresh/aging/error |
| Influence radius | `LightOrb.radius` | one decimal `0.5–5.0m` | clamp + warning | fresh/aging/error |
| Orb capacity | `orbCount` | `n/8 光球`; at 8 “已达上限” | `0/8` | all |
| Saved time | `LayoutSnapshot.savedAt` | locale short time + “已自动保存” | “尚未保存” | fresh/aging/stale/error |
| Restore quality | `invalidCount` | “已恢复 n 个，忽略 m 个无效位置” | “暂无方案” | partial/conflicting/stale |
| Countdown | `PhotoSession.countdownTick` | integer `3/2/1` | hidden | fresh |
| Capture result | `CaptureResult.status/error` | “已保存到系统相册” or source-specific failure | “保存结果未知” | loading/permission_denied/error/fresh |
| Semantic machine state | `*.state` | §3.2 human label, never raw enum | “状态未知” + dashed icon | all |

## 8. PICO platform numeric spec

> The target platform is fixed as PICO spatial; numbers are governed by the official PICO spec and the Design Tokens above.

- **Corner radius**: window/Dialog l = 32dp; inner m = 20dp; controls s = 12dp.
- **Minimum font size**: platform floor 12dp; project caption 13sp; CJK body 16sp/500.
- **Interaction hit target**: ≥56×56dp; world orb collider ≥0.18m subject to device validation.
- **Central field-of-view zones**: core 65°H / 40°V; secondary ≤85°H / 55°V; panel targets lower-central subset.

## 9. Asset Delivery

> Beyond sliced images/icons, there are also 3D models, materials, spatial audio, and environment assets. The core is "engineering can use it directly, and it does not blur or break at different distances."

### 9.1 2D bitmap / sliced image

| Item | Delivery spec |
|---|---|
| Format | PNG (with transparency) / WebP, avoid lossy compression that damages edges |
| Multiplier | Export by dp/dmm baseline |
| Naming | `component_state_multiplier`, uniformly lowercase with underscores, machine-parseable |
| Slice inset | Preserve a safe inset, annotate nine-patch stretch regions |

### 9.2 Iconography

| Item | Delivery spec |
|---|---|
| Format | Prefer SVG / vector to guarantee clarity at any wearing distance |
| Grid | Unified icon grid (such as a 24/28dp visual box), consistent line width |
| Naming classification | Group by semantics (action/status/nav), including filled/outline variants |
| Adaptation | Single-color tintable, following the Design Tokens semantic colors |

### 9.3 3D assets (specific to spatial apps)

| Item | Delivery spec |
|---|---|
| Polygon budget | Orb primitive ≤512 triangles each; selection/range procedural lines; total orb geometry ≤8k triangles. Particle quads ≤32/orb high, ≤12 medium, 0 low. |
| Material / PBR | Procedural/fixed-color unlit emissive-like fake glow; no environment lighting claim; optional noise texture ≤256×256; no 4K PBR assets. |
| Scale / anchor | Core Ø0.12m, origin center; collider Ø≥0.18m; all transforms Stage-local meters. |
| LOD | High/medium/low effect tiers reduce particles/scan refresh before geometry; far distance uses static halo; semantic radius/brightness unchanged. |

> The specific 3D file format and import flow are governed by the official PICO spatial-engine conventions; this design spec does not lock the engine implementation.

### 9.4 Spatial audio / motion / environment assets

| Item | Delivery spec |
|---|---|
| Spatial audio | Prefer SpatialUI system click/state sounds/haptics; optional orb placement/delete one-shots OGG 48kHz mono, localized at orb; no continuous ambience. |
| Motion assets | Procedural timelines from Interaction §7.4; optional 128×128 monochrome particle sprite atlas; Reduce Motion equivalents mandatory. |
| Environment assets | None; Passthrough is the environment. No skybox/panorama bundled. |

### 9.5 Delivery method and engineering handoff

- **Single source**: assets follow the Design Tokens; colors/sizes are not hard-coded into the sliced images, and can be tinted at runtime.
- **Asset list**:

| Asset | Format / budget | Usage | Owner |
|---|---|---|---|
| `orb_core` | SDK primitive ≤512 triangles | all core spheres | LightOrbEntity |
| `particle_soft` | vector/procedural or 128×128 alpha | sunset fog/moon dust/custom sparse effect | LightOrbEntity |
| `frame_white` | vector/procedural | white border | PhotoExperience |
| `frame_film` | vector/procedural + ≤512×128 subtle grain | black film border | PhotoExperience |
| `frame_instant` | vector/procedural + ≤512×128 paper grain | instant frame | PhotoExperience |
| `effect_glyphs` | tintable SVG 24/32dp | preset dual-channel labels | LightPalette/LightOrbEntity |
| `status_glyphs` | tintable SVG 24/32dp | selected/lock/warning/error/saved | shared components |

## 10. Minimum Completeness Gate

> This table is self-checked by the visual/design-system generating role and independently re-reviewed by `design_coherence_reviewer`.
> Giving only style adjectives, a component list, or a shared state table does not constitute structural completeness. If any core component is missing a fixed structure block,
> any key token is still a placeholder, or the window layout and components cannot be mapped one-to-one, it is `block`. When any row is
> `block`, this document's `minimumCompletenessGate=block` and the overall `designStatus=invalid`.

| Check Item | Minimum Pass Condition | Evidence Anchor | Verdict |
|---|---|---|---|
| Visual direction | 2–3 substantially different directions, selection basis, ≥2 rejected directions and approval evidence complete | §2 | pass |
| Visual language | tokens, typography, colorSemantics, materials, scale are all consumable precise values with no mutually exclusive conflicts | §3–§4 | pass |
| Window structure | Each primary WindowContainer has a shell, ASCII/Grid, region→component mapping, spacing, and reflow | §5.0 | pass |
| Component structure | Each core component's base fields, anatomy.layout, sizing, metrics, renderSpec, dataBindings, variants, states all exist independently | §5 | pass |
| Coverage reconciliation | The structure-completeness checklist and the three reconciliation tables (data entity/decision output/primary component sub-states) have no unhandled gaps | §5.1–§5.2 | pass |
| Semantics and trust | Materials/depth, data display, fallback, data states, and trust policy are implementable and traceable | §6–§8 | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |

## 11. Delivery and Recipients

- **Deliverables**: visual direction and approved reference, visual language tokens, component specs, data-display semantic contract, asset list (this document is their human-readable source of truth)
- **Recipients**: Prototype / Frontend Engineer, QA, Design Lead

---

> Format convention: Tokens are the single contract between design and code, and values must be precise (colors #hex); components use structured anatomy (layout Grid + sizing tiers), no prose; colors must use the color+shape dual-channel with a human-readable label; data does not echo the machine enum; PICO platform numbers must not be missing; any design change must be reflected in the delivery facts.
