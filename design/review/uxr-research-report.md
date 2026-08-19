# User Research Report · RoomLightDesigner

> Role: `research_analyst` | Active revision: 3 | Workflow stage: `research` | Change request: `CR-01`

## 1. Research goals and methods

- Validate whether direct in-room placement, fast atmosphere presets, safe manipulation, local recovery, and MR capture form a coherent workflow.
- Compare adjacent lighting-control, AR room-design, and professional lighting-simulation products without reusing their UI structures.
- Methods used in this stage: user-supplied PRD analysis, first-party competitor documentation review, local PICO official-rule knowledge, and explicit evidence-gap logging.
- No interviews or hands-on user sample were supplied. Persona and journey items below are provisional assumptions, not demographic facts.

## 2. Five categories of research evidence

| Category | Evidence / gap | Source | Type | Scope | Confidence | Observed | Validation plan |
|---|---|---|---|---|---|---|---|
| market | Consumer lighting apps establish named scenes and direct color/brightness controls; current Hue SpatialAware also maps real light positions through a phone/tablet AR scan and spatially distributes supported scenes, exclusively with Bridge Pro. | [Philips Hue app](https://www.philips-hue.com/en-us/explore-hue/apps/bridge); [Hue SpatialAware](https://www.philips-hue.com/content/hue/us/en.supportarticlepage.000017.html) | official | Consumer smart-light scene creation and first-party AR mapping | high | 2026-08-15 | Compare task coverage and hardware/scan constraints; do not copy UI structure. |
| market | Adjacent AR room tools support scanning a real room, adding/arranging objects, hiding existing items, saving and sharing designs. | [IKEA Kreativ scan workflow](https://www.ikea.com/us/en/customer-service/knowledge/articles/e86d70g6-3673-4306-b373-20f7cg7fd5ed.html) | official | Mobile/web room design | high | 2026-08-15 | Use as evidence for recovery, direct spatial context, and save expectations only. |
| market | Professional lighting software supports 3D room context, luminaire placement, lighting scenes, calculations, and realistic visualization. | [DIALux indoor lighting](https://www.dialux.com/en-GB/indoor-lighting) | official | Desktop professional lighting planning | high | 2026-08-15 | Keep RoomLightDesigner positioned as fast aesthetic MR preview, not standards-compliant simulation. |
| user | The requested workflow explicitly prioritizes controller placement, live tuning, layout lock, auto-restore, and UI-free photography. | User PRD | user_supplied | RoomLightDesigner target workflow | high | 2026-08-15 | Verify the exact end-to-end scenario in emulator/device. |
| user | Evidence gap: target-user XR skill, color-vision needs, handedness, room size, and preferred session duration are unknown. | none | assumption | Target population | low | 2026-08-15 | Recruit novice and experienced PICO users; include left/right-handed and color-vision variants. |
| domain | Domain decisions are aesthetic atmosphere decisions governed by color, intensity, radius, position, preset effect, and interaction among up to eight light orbs; they are not photometric compliance decisions. | User PRD + DIALux comparison | user_supplied | Product domain boundary | high | 2026-08-15 | Confirm copy clearly says “visual preview” and does not expose lux claims. |
| platform | Shared Space only supports Planar/Volumetric, while a Stage requires Full Space; Stage entry value and a stable return must be declared. | `pico-spatial-app-designer/knowledge/official-rules.json@2.2.0` `PICO-SPACESTATE-001` (official), `PICO-SPACESTATE-002` (official+comfort) | official | PICO space-state architecture | high | 2026-08-15 | Validate the Shared→Full transition and runtime legality before build. |
| platform | Explicit Stage entry/stable exit and attachment-decision completeness are project/comfort constraints in the versioned rule registry, not independently claimed as first-party official documentation. | `official-rules.json@2.2.0` `PICO-STAGE-001` (project+comfort), `PICO-ATTACHMENT-001/002` (official-capabilities+project) | assumption | Project design gate | medium | 2026-08-15 | Cross-check against current SDK docs and architecture review; keep exact provenance. |
| platform | Evidence gap: emulator/device support for complete MR compositor capture into the system gallery is not proven at design time. | none | assumption | Photography implementation | low | 2026-08-15 | Query public SDK surface, run permission/save tests, and disclose simulator compositor limits. |
| safety | The project’s safety/accessibility contract prohibits automatic camera movement and continuous full-screen flashing, requires motion fallbacks, Reduce Motion, controller fallback, stable exit, and non-color cues. These entries are sourced as `safety`/`project` in the registry rather than uniformly first-party official. | `official-rules.json@2.2.0` `PICO-MOTION-001/002/003`, `PICO-ACCESS-001/002/004`, `PICO-COLOR-001` | assumption | Project comfort/accessibility gate | medium | 2026-08-15 | Review all effects and cross-check public SDK/design guidance during implementation. |
| safety | The user explicitly requires layout lock and confirmation for clear-all; long-press delete creates an additional accidental-deletion risk requiring state guards. | User PRD | user_supplied | Destructive interaction | high | 2026-08-15 | Unit-test lock/delete/drag arbitration and verify on controller. |

Source conflict resolution: the PRD requests a Stage Mixed tool, while `PICO-STAGE-001` requires explicit Stage entry. The revised intent uses one Shared Space entry panel and opens Stage Mixed only after the user selects “进入房间布光”; Stage exit returns to the entry state.

## 3. Competitive benchmark

| # | Competitor / platform | Feature needs | Interaction experience | Visual experience | Spatial-capability usage | Source |
|---:|---|---|---|---|---|---|
| 1 | Philips Hue + SpatialAware / iOS & Android + Bridge Pro | Rooms/zones, scenes, color/brightness, saved scenes; SpatialAware scans a room with a phone/tablet camera, maps each physical light/light effect, and distributes supported scenes by room layout. Requires real Hue lights and Bridge Pro; moved/added lights require partial rescan. | Touch/camera-first: aim at a blinking light/effect, add/verify its mapped point, repeat; the manufacturer says a room of about 10 lights typically scans in under a minute, which is a first-party claim rather than independent usability evidence. | First-party pages show mobile room/scene controls and spatial mapping instructions; hierarchy/readability quality has not been independently tested. | Genuine AR position mapping and spatial scene distribution, but results are bound to scanned physical hardware and viewed/managed through a handheld device rather than direct head-worn placement of virtual light volumes. | [Hue app](https://www.philips-hue.com/en-us/explore-hue/apps/bridge), [SpatialAware](https://www.philips-hue.com/content/hue/us/en.supportarticlepage.000017.html), observed 2026-08-15 |
| 2 | IKEA Kreativ / mobile + web | Room scan, digital twin, erase/hide items, add/arrange furniture, save/share/remix; does not focus on live light effects. | Phone camera/LiDAR capture followed by touch/web editing; scanning has guided setup and device capability branches. | Photographic/3D room canvas with highlighted removable items and side tools; observe only. | Uses captured room scale and object placement, but manipulation is mediated through a phone/web view rather than head-worn in-room perception. | [Official scan workflow](https://www.ikea.com/us/en/customer-service/knowledge/articles/e86d70g6-3673-4306-b373-20f7cg7fd5ed.html), [platform differences](https://www.ikea.com/us/en/customer-service/knowledge/articles/a6fd8d36-b079-46bd-af63-e429217eff4d.html), observed 2026-08-15 |
| 3 | DIALux evo / Windows desktop | Standards-compliant calculations, 3D geometry, real luminaires, automatic/grid placement, scenes, false color, ray tracing, BIM/scan import and documentation. | Official material documents CAD views, drag/drop plans, catalogs and calculation/evaluation steps. “Higher expertise/setup cost” is an analyst inference (medium confidence) requiring expert workflow observation. | Official material shows 3D views, results monitor, isolines/false-color/value charts and renderings. Perceived density/readability is not independently measured. | Strong 3D scale and lighting simulation but desktop-based rather than embodied MR in the current room. | [Official indoor-lighting overview](https://www.dialux.com/en-GB/indoor-lighting), observed 2026-08-15 |

| # | Strengths worth absorbing at need/opportunity level | Weaknesses / anti-patterns to avoid |
|---:|---|---|
| 1 | Named scenes, direct color/brightness feedback, spatial distribution by actual room positions, and explicit rescan/recalibration rules. | Do not require proprietary lamps/Bridge Pro, do not force a scan of existing hardware, and do not reduce embodied decisions to a handheld remote-control flow. |
| 2 | Preserve real-room context, direct object arrangement, clear save/recovery, capability-aware fallbacks. | Avoid a long room-scan prerequisite and avoid hiding device limitations. |
| 3 | Explicit distinction between visual result and calculation, clear parameters, scene comparison and strong data semantics. | Avoid CAD density, photometric jargon, slow setup, or implying standards-compliant accuracy. |

**Our differentiation opportunity**: Hue SpatialAware verifies that a current commercial product maps room-relative light positions and uses them to distribute supported scenes; any improvement in perceived quality is a manufacturer efficacy claim that still needs comparative validation. RoomLightDesigner differentiates through head-worn, direct placement of virtual light volumes in the current Passthrough room, visible radius semantics, no smart-light hardware, reversible layout locking/recovery, and a UI-free capture flow. It absorbs scene immediacy, recalibration honesty, spatial context and parameter clarity while rejecting hardware dependence, mandatory scan overhead and professional simulation claims.

Sample note: no directly equivalent PICO MR room-lighting product was established from reliable first-party evidence, so the benchmark uses three adjacent products across smart-light AR mapping/control, mobile AR room design, and professional desktop lighting simulation. Platform differences are explicit. Claims about ease, learning cost and visual density are manufacturer statements or analyst inferences unless separately validated. Absorption is limited to requirements and opportunities; no competitor layout, state graph, component combination, or visual style is reused.

## 4. Domain model

- **Workflow**: enter Stage Mixed → inspect restored layout → choose preset/custom color → aim and place → select and tune → reposition or delete → repeat up to eight → lock → optionally hide panel → enter photo mode → choose frame/countdown → capture/save → exit photo mode → continue or leave with auto-save.
- **Decision variables**: preset identity; color hue/saturation/value; brightness 0–100%; radius 0.5–5 m; world position; relative balance among orbs; particle/motion intensity; current orb count; lock state; panel visibility; photo frame; countdown duration; permission and tracking state.
- **Data entities and timeliness**:
  - `LightOrb`: id, preset/custom kind, color, brightness, radius, 3D position, selected state; interaction feedback should update in the current frame.
  - `LayoutSnapshot`: up to eight orb records plus saved timestamp/schema version; save after mutation with debounce and at lifecycle boundaries.
  - `InteractionState`: idle/aiming/placing/selected/dragging/longPressArmed/locked; event-driven and never persisted as active gesture state.
  - `PhotoSession`: frame style, countdown seconds, countdown tick, capture permission/result; tick every second and finalize asynchronously.
- **Specialized risks**: mistaking glow radius for real illumination; overlapping orbs obscuring selection; long-press delete conflicting with drag; restored coordinates becoming invalid; particles affecting comfort/performance; capture permission or compositor failure; layout lock accidentally trapping the user; maximum-count feedback being invisible.
- **User mental model**: each orb is a portable “mood lamp” whose color, strength, and reach can be shaped directly; presets are recipes, the room itself is the canvas, lock protects the composition, and photo mode is a clean presentation state.
- **Mature patterns**: preset-first entry with later fine tuning; direct manipulation; immediate parameter feedback; autosave; explicit destructive confirmation; visible capability/permission status.
- **Anti-patterns**: phone-dashboard metaphor inside MR; invisible selection radius; color-only selected/disabled states; physics-simulation copy; camera motion; always-on particles at full density; destructive gestures without arbitration; silent capture failure.

## 5. Provisional personas and journey

### Persona A — “现场氛围创作者” (assumption, confidence: medium)

| Dimension | Content |
|---|---|
| Profile | Adult content creator, novice-to-intermediate XR user; exact demographics unknown. |
| Scenario/frequency | Configures one room before a photo/video session; 5–20 minute session duration is a low-confidence validation hypothesis, not observed behavior. |
| Goals | Reach a distinctive mood quickly, compare warm/cool looks, protect the final layout, capture without UI. |
| Pain points | Abstract 2D controls, losing spatial positions, accidental deletion, capture flows that reveal editor chrome. |
| Spatial habits | Standing with small room-scale movement; controller ray is primary input. |
| Accessibility | Color-only cues are insufficient; large hit targets and reduced particle motion are required. |
| Source note | Synthesized from the user PRD; not an interview quote. |

### Journey

| Stage | Entry | First hands-on | Core use | Locked/photo | Exit/return |
|---|---|---|---|---|---|
| Goal | See the real room safely | Place a useful first orb | Balance several orbs | Preserve and capture | Resume later without rebuilding |
| Behavior | Checks panel and restored state | Chooses preset and aims | Selects, tunes, drags, compares | Locks, hides UI, counts down | Exits photo mode or app |
| Touchpoint | Stage Mixed + panel | Ray + trigger + orb preview | Orb entity + parameter panel | Lock, frame, countdown | Auto-save/restore |
| Likely low point | Permission/tracking warning | Unclear placement target | Selection overlap or drag/delete conflict | Capture failure | Coordinate drift |
| Opportunity | Honest status and stable exit | Obvious placement feedback | Shape + outline + text feedback | Restore UI and preserve layout on failure | Validate coordinates and degrade safely |

Emotional low point: a photo capture fails after the user has hidden UI and prepared a layout. The design must restore controls, keep the layout intact, and explain the smallest recovery action.

## 6. Key findings

| # | Finding | Evidence | Confidence | Design implication |
|---:|---|---|---|---|
| 1 | Preset-first plus later tuning is a feature pattern shared by Hue and the user PRD; user familiarity is an analyst hypothesis, not established behavior. | Hue official scene/custom controls + `USER-PRD-001#1-光球系统` | medium | Keep both preset and manual paths visible; validate recognition and familiarity in usability testing. |
| 2 | Direct room context is the main spatial differentiator. | IKEA spatial-context workflow + user Passthrough requirement | high | Keep the room visible and make orb position/radius readable in world space. |
| 3 | Accuracy claims must stay bounded. | DIALux professional calculation scope vs user’s fake-glow constraint | high | Label output as visual atmosphere preview and avoid lux/photometric language. |
| 4 | Safety and reversibility are product features, not error polish. | User lock/confirm requirements + PICO motion/accessibility rules | high | Lock, confirmation, autosave, stable exit, and failure recovery must appear in the state graph. |

## 7. Posture, interaction, duration, and safety gaps

- Usual posture: standing/small movement is user-workflow evidence; actual distribution is unknown and must be validated.
- Comfortable reach/FOV: no measured RoomLightDesigner data exists. Keep primary panel in the central comfortable view; use official PICO window-sizing/FOV methodology when the panel size is derived.
- Gaze/pinch hit rate: not measured; controller ray is the explicit fallback/primary path. Require at least 56×56 dp panel targets and non-color feedback.
- Glance decision baseline: evidence gap; ≤2 s is a product acceptance hypothesis for recognizing selected preset/count/lock state, to be usability-tested.
- Fine-tuning dwell: evidence gap; 10–60 s per orb is a low-confidence hypothesis managed in PM §2, to be measured rather than treated as behavior evidence.
- Motion-sickness risks: moving particle fields, neon scan, large pulsing halos, and central countdown scale animation. No automatic camera movement; provide Reduce Motion and low-density performance fallback.
- Session guidance: no product-specific evidence; avoid claiming a safe duration. Observe fatigue during device validation and keep a stable exit available at all times.

## 8. Minimum Completeness Gate

| Check item | Evidence | Verdict |
|---|---|---|
| Five evidence categories | Section 2 includes market/user/domain/platform/safety, with explicit gaps | pass |
| Competitive benchmark | Section 3 includes three adjacent first-party products and all four dimensions | pass |
| Domain model | Section 4 covers workflow, decisions, entities/timeliness, risks, mental model, patterns | pass |
| User evidence | Section 5 clearly labels provisional persona/journey as assumptions sourced from the PRD | pass |
| Quantitative and safety | Section 7 provides sourced floors/constraints or explicit validation gaps | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |
