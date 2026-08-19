# Interaction / Spatial Design Spec · RoomLightDesigner

> Active revision: 7 | Completed stages: `task_model`, `concept_formation`, `spatial_structure`, `composition_synthesis`, `design_system` | Upstream: PM@4, UXR@3, Visual@3 | Change request: `CR-03 repaired`

## 1. Design principles

| ID | Assertion | Scope | Basis | Downstream checkpoint | Precedence |
|---|---|---|---|---|---|
| P1 | Real-room spatial judgment is the primary focus; controls must never obscure placement, radius, or a safe view of the room. | spatial/visual | R-VISUAL-01; UXR §6 finding 2 | container, attachment, layout, photo states | Below physical safety; above decorative richness |
| P2 | Every destructive or state-losing action is guarded, reversible before commit, or recoverable from the last valid layout. | interaction/data | R-DELETE-01, R-SAVE-01; UXR §6 finding 4 | state graph, repository contract | Overrides speed and gesture convenience |
| P3 | “Visual atmosphere preview” is explicit; no photometric or real-GI claim may appear. | product/data trust | PM assumption 1; DIALux boundary | copy, data semantics, effects | Overrides realism language |
| P4 | A state must be legible without color alone and must have controller/system-back fallback. | interaction/accessibility | PICO-COLOR-001, ACCESS-002/004 | component states, input spec | Overrides minimal ornamentation |
| P5 | Effects are bounded by comfort and performance; Reduce Motion disables spatial scan/pulse and lowers particle density. | motion/performance | UXR §7; PICO-MOTION-001/002/003 | motion table, runtime fallback | Overrides preset fidelity |
| P6 | Stage is entered only by explicit opt-in and always offers a stable return to Shared Space. | spatial/safety | R-ENTRY-01; PICO-SPACESTATE-002; PICO-STAGE-001 | architecture/state graph | Overrides shortest-launch path |

Conflict arbitration: physical safety and recoverability → platform legality/accessibility → task completion → visual fidelity → speed. Decorative particles or immediate Stage launch never override a safer fallback.

Negative list: no automatic camera movement; no infinite flashing; no color-only selection/lock/error; no true-GI claims; no hardware/scan prerequisite; no silent ninth-orb failure; no unconfirmed clear-all; no Stage entry without explicit action.

## 2. Task / decision model

| Task ID / task | Actor / scenario | Input evidence | Decision output | Error consequence | Frequency | Dependencies | Duration scale |
|---|---|---|---|---|---|---|---|
| T0 Enter workspace | User launches from Shared Space | Entry value, Passthrough explanation, stable-exit availability; R-ENTRY-01 | Enter Stage Mixed now or remain/exit | Surprise exclusivity or unsafe entry | once/session | none | glance, ≤2 s recognition hypothesis; no forced timeout |
| T1 Inspect restore | User sees a prior layout or empty room | Save timestamp/schema validity, orb count/positions, tracking validity; R-SAVE-01 | Accept restored layout, clear with confirmation, or continue empty | Stale/corrupt coordinates cause confusion or unsafe placement | once/session, after recovery | T0 | glance then optional fine review |
| T2 Choose light recipe | User wants a mood | Four preset semantics, custom color, current count/8, preview boundary; R-LIGHT-01/R-CAP-01 | Choose preset/custom value or cancel | Wrong mood; ninth addition blocked without explanation | each new orb | T0, capacity available | glance/short selection |
| T3 Aim and place | User targets a room location | Controller ray hit/pose, placement preview, safe bounds/tracking; R-INTERACT-01 | Commit world position or cancel | Floating/invalid/off-target placement | each new orb | T2, tracking valid, unlocked | direct manipulation |
| T4 Select and tune | User balances an orb | Orb identity, non-color selection marker, color, brightness, radius, room context; R-LIGHT-02 | Accept live parameter values or deselect | Wrong orb/value, hidden range, out-of-bounds value | repeated/high | existing orb, unlocked | 10–60 s hypothesis |
| T5 Move or remove | User corrects composition | Selected orb, trigger hold duration, drag delta, lock state; R-DELETE-01 | Commit move, delete after valid 2 s hold, or cancel | Accidental deletion/drag conflict | repeated/medium | T4, unlocked | direct; 2 s deletion threshold is fixed |
| T6 Protect layout | User finishes composition | Current count, selected state, save status; R-LOCK-01 | Lock or unlock | Unintended edits or inability to recover controls | few/session | T1–T5 | glance |
| T7 Manage panel | User needs unobstructed view | Panel visibility, photo mode, menu event; R-INTERACT-01 | Show/hide control panel | Controls cover target or disappear irrecoverably | frequent | T0 | immediate |
| T8 Prepare photo | User wants a clean capture | Lock recommendation, frame styles, 3/5/10 options, permission/capability; R-PHOTO-01/02 | Choose frame/countdown and start or cancel | Capture without readiness; hidden controls during failure | occasional | T0; layout exists; not already capturing | short selection |
| T9 Capture and recover | Countdown/capture runs | Countdown tick, compositor/media result, trigger exit; R-PHOTO-02 | Save success, recover from failure, or exit photo mode | Lost composition, no gallery image, stuck hidden UI | occasional | T8 | fixed 3/5/10 s + async result |
| T10 Exit/return | User ends Full Space or app | Pending save/capture, stable exit; R-ENTRY-01/R-SAVE-01 | Return to Shared Space after final save or remain | State loss or exclusive-space trap | once/session | any stable non-capture state | immediate after save acknowledgement |

Task dependencies: T0 gates all Stage tasks. T2→T3 is serial. T4 can branch to T5 or repeat tuning. T6 disables selection/move/delete but not T7/T8/T10. T8/T9 is mutually exclusive with editing. T10 is always reachable; if capture is pending, cancel/recover first and preserve the layout.

Key decisions: enter Full Space; accept restored coordinates; choose preset/custom light; commit placement; choose selected orb and parameters; distinguish drag vs deletion; lock layout; choose frame/countdown; accept capture result; exit safely.

Competitor coverage check: named scenes/manual tuning and save expectations are covered; room-relative position is embodied rather than handheld-scanned. Physical smart-light control, room reconstruction, photometric calculations/BIM, collaboration/remix, automations, and standards documentation are intentionally omitted by `USER-PRD-001#视觉与技术约束` and PM §4 originality/domain boundary.

## 3. Spatial value justification

| Task | Spatial value judgment | Rationale | 2D counterfactual | Benchmark evidence | Rating |
|---|---|---|---|---|---|
| T0 Enter | Space-state/safety; no intrinsic placement value | Entry should remain a single 2D Shared Space decision, not pseudo-spatial chrome. | A normal planar consent/entry panel is sufficient and preferred. | PICO Stage entry contract | low |
| T1 Inspect restore | Position/distance/depth/time | Validity is best judged by seeing restored orbs against the current real room and moving viewpoint. | A list/top-down map can show records but cannot prove alignment with present room context. | IKEA scan context; Hue rescan honesty | high |
| T2 Choose recipe | No necessary spatial dimension; fast recognition | Preset/custom selection is a compact 2D decision attached to the active workspace. | A palette/list fully handles the choice. | Hue scenes/custom controls | low |
| T3 Aim/place | Direction/distance/position/body/depth | Controller ray and head movement let the user judge an orb relative to walls, furniture and other orbs in situ. | A 2D floor plan requires room reconstruction and loses direct embodied parallax. | IKEA/Hue map positions but handheld; differentiation opportunity | high |
| T4 Select/tune | Position/scale/depth/time change | Live color/brightness/radius changes must be evaluated in the real room and against neighboring orbs. | Sliders can be 2D, but a 2D preview cannot show room-relative balance at true spatial scale. | Hue tuning + DIALux visualization boundary | high |
| T5 Move/remove | Direction/distance/position/body | Direct drag preserves the selected orb’s room context; deletion arbitration depends on controller motion over time. | Coordinates or a top-down drag are possible but require an accurate room model and reduce immediacy. | DIALux 3D placement; IKEA object arrangement | high |
| T6 Lock | State only | Lock has no inherent spatial value; it is a global guard surfaced in 2D and enforced in 3D interaction. | A toggle and text status are sufficient. | User PRD safety requirement | low |
| T7 Panel | Attention/occlusion | Visibility affects room sightline, but the decision is binary and should not add windows. | A menu-key toggle fully handles it. | Anti-dashboard differentiation | low |
| T8 Prepare photo | View composition/depth plus 2D option choice | Frame must be previewed in the actual MR view while frame/countdown controls remain compact. | A 2D mock image can choose style but not guarantee final in-room composition. | IKEA share image; user photography requirement | medium |
| T9 Capture/recover | Time change/view composition | Countdown and full MR compositor result are spatial presentation outcomes; failure handling remains 2D status. | A 2D camera preview cannot reproduce head-worn Passthrough composition. | Market gap/opportunity | high |
| T10 Exit | Safety/state only | Stable return is required but has no decorative spatial value. | A single explicit exit action is sufficient. | PICO stable-exit constraint | low |

Collaboration is deliberately absent. “Simulation” is limited to aesthetic glow/range visualization; no task is allowed to infer photometric truth. Stage is justified by T1/T3/T4/T5/T8/T9, while T0/T2/T6/T7/T10 remain planar controls.

## 4. Design hypotheses and concept selection

### 4.1 Substantially different hypotheses

| Hypothesis | Information model | Spatialization | Container structure | User path | Primary interaction | Risk / cost |
|---|---|---|---|---|---|---|
| A · Direct Spatial Atelier | Room is the canvas; orbs are world entities; one contextual control surface exposes creation/tuning/status. | High: embodied placement and live comparison in Stage Mixed. | Shared Space entry → Full Space Stage Mixed; one task panel, transient confirmation/countdown. | Enter → place/tune directly → lock → capture → exit. | Controller ray/trigger direct manipulation with panel controls. | Medium: ECS interactions, capture API and persistence coordinates; avoids room scanning. |
| B · Scan-and-Generate Studio | A scanned room model is the source of truth; user configures mood goals and the system proposes light positions. | Medium/high but mediated by reconstruction and recommendation. | Shared scan/onboarding windows → Stage preview with generated layout. | Scan → verify surfaces → generate → accept/edit → capture. | Guided scan plus recommendation cards and occasional spatial edits. | High: env mesh/anchors/scan reliability, longer onboarding, inferred intelligence; conflicts with no-AI/no-unnecessary-dependency intent. |
| C · Lighting Storyboard + Optional Preview | A 2D top-down storyboard and ordered light list are primary; Stage is a final preview. | Low/medium: most work occurs on a planar map, Stage only previews. | Planar main window → optional secondary Stage Mixed preview. | Draw room/map → add list items → enter preview → adjust numerically → capture. | 2D list/map editing, limited controller placement. | Medium: easier testability and accessibility, but requires a room model and weakens the key embodied moment. |

### 4.2 Selection matrix (1–5, higher is better)

| Hypothesis | Task efficiency | Spatial value | PICO comfort | Domain depth | Safety | Accessibility | Engineering feasibility | Distinctiveness | Total / 40 | Verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| A · Direct Spatial Atelier | 5 | 5 | 4 | 4 | 4 | 4 | 4 | 5 | 35 | selected |
| B · Scan-and-Generate Studio | 2 | 4 | 3 | 4 | 3 | 3 | 2 | 4 | 25 | rejected |
| C · Lighting Storyboard | 3 | 2 | 4 | 3 | 4 | 5 | 3 | 2 | 26 | rejected |

Comparative evidence for comfort, safety and accessibility (scores are design hypotheses, not measured outcomes):

| Hypothesis | PICO comfort rationale / evidence / confidence | Safety rationale / evidence / confidence | Accessibility rationale / evidence / confidence |
|---|---|---|---|
| A | Score 4: direct room-scale viewing avoids forced camera motion; bounded particles and one panel reduce load. UXR §7 flags effects/FOV as device-validation gaps, so confidence is medium. | Score 4: explicit entry/exit, layout lock, destructive guards and real-room visibility satisfy PM §4; tracking/capture remain device risks. Confidence medium-high. | Score 4: controller primary/fallback, non-color cues, panel toggle and Reduce Motion are specified; no measured target-user data exists. Confidence medium. |
| B | Score 3: guided scan and generated results reduce manual manipulation but add longer onboarding, rescan movement and more state transitions. Hue/IKEA scan evidence; comfort effect unmeasured. Confidence low-medium. | Score 3: scan validation may improve coordinate knowledge, but env-mesh/permission failure adds safety/recovery branches unsupported by the PRD. Confidence medium. | Score 3: guidance can help novices, but camera scanning and room walking raise mobility/vision barriers. No user sample. Confidence low. |
| C | Score 4: planar editing limits prolonged full-space exposure and makes motion minimal; actual FOV comfort remains unmeasured. Confidence medium. | Score 4: map-first editing reduces direct spatial errors, but stale/inaccurate room models can mislead. Confidence medium. | Score 5: persistent planar controls, text scaling and limited spatial manipulation provide the broadest fallback. This is a design inference, not tested behavior. Confidence medium. |

Sensitivity: treat every comfort/safety/accessibility score as ±1. A’s base total is 35 versus C’s 26 and B’s 25. In the adverse reasonable case A loses 3 points while C gains 3, A remains 32 versus C 29; even setting A engineering feasibility down by one leaves A at 31. The selection therefore remains A, while C’s accessibility strengths are mandatory fallbacks within A. Device testing can still trigger a later comfort patch without changing the current evidence-based concept.

Selected concept: **Direct Spatial Atelier** — a consent-gated Stage Mixed workspace where users compose the real room by placing and tuning lightweight virtual “mood lamps,” then lock and capture the view.

Selection evidence: A directly satisfies T3/T4/T5 and R-LIGHT/R-INTERACT without scan-first overhead. Its control decisions stay planar and its spatial value is concentrated in tasks that need direction/distance/position. Accessibility remains strong through controller fallback, non-color cues, panel visibility and Reduce Motion. Engineering risk is bounded because effects are fake glow/particles and no photometric engine, environment mesh or AI recommendation is introduced.

Market differentiation:

- **Positioning**: a hardware-free, head-worn workflow that links named atmosphere, direct in-room virtual-light placement, reversible layout control and MR capture without a mandatory room scan or photometric CAD process.
- **Rationale**: unlike Hue SpatialAware it requires no Bridge Pro or physical lights and supports direct virtual placement; unlike IKEA Kreativ it does not require a room scan/digital twin; unlike DIALux it does not perform standards calculations or require CAD workflow. It absorbs scenes, room-relative honesty, save/recovery, and clear parameters only at the needs level.
- **Evidence refs**: UXR §2 market evidence; UXR §3 competitor rows 1–3; UXR §3 “Our differentiation opportunity”; PM §4 originality contract.

Rejected rationale:

- B is rejected because scan/env-mesh/AI-like recommendation adds setup, permissions and engineering risk not supported by `USER-PRD-001`, while direct placement already supplies the required spatial value.
- C is rejected because the 2D map becomes the primary artifact, recreates room-model overhead, and weakens the screen-impossible key moment; its accessibility strengths are retained through panel/controller/Reduce Motion fallbacks in A.

## 5. Experience/container/attachment/sizing

### 5.1 Experience architecture

| Layer | Responsibility | Host | Entry | Exit / fallback | Why it exists |
|---|---|---|---|---|---|
| Glance · safe entry | Explain Passthrough, restore status and the exclusive Full Space transition; provide one explicit “进入房间布光” action. | `W-SHELL` Planar WindowContainer in Shared Space | App launch | Close app, or explicit entry opens `STAGE-ROOM`; if Stage creation fails remain here with retry | Prevents an implicit spatial takeover and gives a stable non-immersive recovery surface. |
| Explore · instrument panel | Choose preset/custom, tune selected orb, see `n/8`, lock/unlock, clear with confirmation, prepare photo settings, view save/tracking/capture status. | The same `W-SHELL` retained as a compact Planar control panel alongside Stage | Successful Stage open | Menu key hides/shows panel; closing Stage returns panel to Glance | These are 2D decisions; one panel avoids floating-window accumulation. |
| Immerse · room composition | Place, select, drag and compare world-space orbs; view fake glow/range/effects against Passthrough; compose and capture. | `STAGE-ROOM`, Stage Mixed in Full Space | Explicit `user.enterStage` from Glance | `user.closeStage`, system Stage close, or fatal tracking recovery returns to Glance | T1/T3/T4/T5/T8/T9 require room-relative direction, distance, overlap and embodied viewing. |

Immersion value boundary: `STAGE-ROOM` is used only for room-relative light entities, direct manipulation and final MR composition. Preset browsing, numbers, lock, destructive confirmation and recovery remain planar. No floating window is added for decoration.

### 5.2 Container architecture and legal space-state chain

| ID | Container | Space state | Form / immersion | Content and primary focus | Entry value/action | Stable exit | Legal-combination note |
|---|---|---|---|---|---|---|---|
| `W-SHELL` | WindowContainer | Shared on launch; retained in Full beside Stage | Planar; depth fixed 640dp; Dynamic worldScale | Shared entry gate, then one compact task-scoped control surface | Launch creates only this window | Always remains available after Stage close; normal close exits app | Planar is legal in Shared and Full Space. It is the single default root surface. |
| `STAGE-ROOM` | Stage | Full Space only | Mixed (`immersion=0`) | Passthrough room, up to eight `LightOrb` entities, placement reticle, range shells, photo frame/countdown | Value: judge location/radius/atmosphere in the real room. Action: explicit “进入房间布光” | Close Stage or fatal safety fallback; returns to `W-SHELL` Shared entry | One Stage plus the retained Planar window is legal in Full Space; Stage never exists in Shared Space. |
| `D-CLEAR` | Dialog attached to `W-SHELL` | Same state as host | Focused confirmation | “清空全部 n 个光球？” / cancel / clear | Explicit clear-all request | Cancel or completion returns to prior Explore state | Short destructive decision; not a new WindowContainer. |

No Volumetric WindowContainer is used: the subject entities exceed a bounded cuboid and belong in Stage; the control subject is familiar 2D workflow. No environment mesh or spatial anchor capability is requested for v1. Restored coordinates are local Stage coordinates with validity checks and an honest reset fallback, not claims of persistent real-world anchoring.

### 5.3 Window attachment decision matrix

| Need | Placement mode | Selected type | Host | Semantic role / persistence / frequency | Rationale | Rejected alternatives (including InlineControl and None) | Validation plan |
|---|---|---|---|---|---|---|---|
| Preset, capacity, lock, photo entry | In-window | `InlineControl` | `W-SHELL` | Current-workspace controls; persistent while panel visible; high | Controls act on the current workspace and fit a single compact surface. | TabBar is page navigation, Toolbar would duplicate commands outside the panel, Subwindow creates needless width, SpatialPopup is too transient, None would make required actions inaccessible. InlineControl beats None because these are required decisions. | Verify all actions within central FOV and ≤2 controller steps from idle. |
| Selected-orb parameters | In-window | `InlineControl` | `W-SHELL` | Context inspector; persistent only while selected; high | Color/brightness/radius directly affect the selected entity and replace palette content in situ. | A full-height Subwindow and floating SpatialPopup increase occlusion; None cannot expose required tuning. | Select overlapping orbs, confirm named target and current values remain unambiguous. |
| Clear all | Docked/focused modal | `Dialog` | `W-SHELL` | Destructive confirmation; transient; low | Requires a focused, explicit second action. | InlineControl alone risks accidental completion; None violates the PRD; Sheet is excessive for two choices. | Test cancel, confirm, zero-orb disabled state, lock interaction and rapid repeat. |
| First-use trigger arbitration hint | In-window anchored | `Coachmark` | `W-SHELL` | Instruction; first run or help; low | Briefly teaches click/drag/2s hold without becoming permanent UI. | InlineControl would make instructions look actionable; None leaves a high-risk gesture undisclosed. | Confirm dismissibility, persistence and no reappearance after completion. |
| Orb relationship/status around panel | Wraparound candidate | `None` | `W-SHELL` | No persistent need | Count, selection and lock status already live in-window; world cues belong on entities. | Augment would duplicate world semantics; InlineControl already carries the necessary status. None prevents decorative spatial chrome. | Inspect for any orphan status during prototype QA. |
| Page navigation / persistent side workspace | Docked candidate | `None` | `W-SHELL` | Not applicable | There is one task flow and one primary focus. | TabBar, Toolbar and Subwindow would duplicate or fragment the same actions; InlineControl is sufficient. | Confirm no task requires side-by-side pages at default/min size. |

Photo frame and countdown are Stage render layers, not window attachments: they are part of the captured composition and temporary feedback. Before entry, frame/countdown settings are `InlineControl`; once `PhotoSession.active=true`, editing UI and controller ray are hidden.

### 5.4 `W-SHELL` sizing derivation

| Field | Decision | Source / rationale |
|---|---|---|
| Content type / tier | 2D workflow; auxiliary/HUD while Stage is open, compact entry surface in Shared | PICO sizing methodology + Constellation Atelier room-first hierarchy |
| Form / unit / depth | Planar in dp; depth fixed 640dp | PICO Planar constraint |
| Viewing conditions | Standing or seated-small-movement; default ~1.75m; Dynamic worldScale; repeated 5–20 minute duration remains an unvalidated assumption | PICO baseline + UXR §5/§7 |
| Clear-FOV target | Default panel occupies a lower-central subset of 65°H × 40°V; no content requires head scanning; large size remains below secondary 85°H × 55°V | PICO FOV constraints; device check required because dp-to-angular mapping is system-controlled |
| Framework overhead | 96dp title/system shell budget; no TabBar/Toolbar/Subwindow; content inset 16dp | PICO framework baseline; attachment matrix |
| Hit/readability floor | ≥56×56dp targets; ≥12dp text; CJK body ≤50 chars/line | PICO numeric baseline |
| Aspect ratio | Adaptive, not fixed; default 4:3-ish, min stacks vertically, max permits two columns | Content reflow rather than global scale |
| Resize semantics | `ContentMinSize`; user may enlarge within bounds; internal content reflows/scrolls | Prevents unusable shrink while retaining user resize freedom |

Candidate calibration:

| Candidate | Logical size | Content capacity / FOV / risk | Verdict |
|---|---:|---|---|
| Compact launch | 560×420dp | One column after 96dp shell; usable for entry and a short palette but inspector requires vertical scroll; low occlusion | Reject as default; use as minimum/Compact. |
| Room-first default | 720×540dp | 688×412dp content area after 96dp shell and 16dp insets; fits one 68dp guard row plus a 328dp primary region with 16dp gap and 56dp targets | Select default; compact enough for MR while keeping one focus. |
| Expanded compare | 920×640dp | Two-column palette/inspector and more whitespace; increases room occlusion without a required simultaneous comparison task | Reject as default; use as maximum/Large. |

Final window contract:

| Tier | Width × height | Reflow |
|---|---:|---|
| Minimum / Constrained | 520×380dp | Single column; selected inspector replaces palette; status compresses to one row; secondary help scrolls; Dialog remains modal. |
| Default / Compact | 720×540dp | Two-column preset grid; inspector is one column with three controls; global actions remain a bottom row. |
| Maximum / Large | 920×640dp | Two-column content where palette and selected summary may coexist, but only one has primary emphasis; no font/hit-target scaling. |

All sizes fall within the official 320×180–2700×1800 Planar range. Exact angular occupancy, passthrough occlusion and controller reach are runtime validation items; failure triggers a size/reflow patch, not an unsupported “officially comfortable” claim.

## 6. State graph and end-to-end flow

### 6.1 States

| State ID | Container / primary focus | Main task + decision output | Layout/components/data | Entry | Exit / continue | Exception recovery / return |
|---|---|---|---|---|---|---|
| `S0_SHARED_ENTRY` | `W-SHELL` / enter safely | T0: enter Full Space or leave | EntryGate; restore summary; capability status | app launch or Stage close | `user.enterStage` → S1; close app | Stage failure stays S0 with retry; corrupt snapshot offers discard while preserving app access |
| `S1_ROOM_IDLE` | Stage + visible `W-SHELL` / room composition | T1/T2: inspect restore and choose recipe | Orb field; palette; n/8; lock/photo actions | Stage opened and snapshot validated | recipe → S2; select orb → S3; lock → S5; photo prep → S7 | Tracking loss → S11; panel hidden remains S1 with entities unchanged |
| `S2_PLACEMENT_AIM` | Stage / reticle | T3: commit valid position or cancel | Ray, reticle, ghost orb, placement validity, chosen recipe | `user.chooseRecipe` while count<8 and unlocked | trigger commit → S1; cancel/menu → S1 | count reaches 8, tracking invalid, or bad pose disables commit and explains smallest recovery |
| `S3_ORB_SELECTED` | selected orb + inspector / tune | T4: accept live color/brightness/radius or choose move/delete | double-ring, range shell, inspector, autosave status | trigger click on orb while unlocked | parameter changes remain; trigger-hold motion → S4; 2s stationary hold → delete→S1; deselect → S1 | clamp invalid parameter; overlapping selection shows named target; save error keeps in-memory state |
| `S4_ORB_DRAGGING` | selected orb / position | T5: release at new valid position | tether, live orb pose, suppressed delete timer after movement threshold | selected + trigger hold + movement threshold | trigger release commits and returns S3 | tracking loss rolls back to last valid pose then S11; lock cannot activate mid-gesture until release |
| `S5_LAYOUT_LOCKED` | room + lock banner / protected layout | T6: keep protected or unlock | entities visible without target affordances; lock icon/text; panel/menu available | lock toggle on outside active gesture | unlock → S1; photo prep → S7; panel toggle stays S5 | selection/move/delete requests are consumed with “布局已锁定” feedback; no state mutation |
| `S6_CLEAR_CONFIRM` | `D-CLEAR` / destructive choice | T5: cancel or clear all | count, cancel, destructive clear | clear request when count>0 and unlocked | cancel → prior S1/S3; confirm→S1 empty | lock or lifecycle interruption cancels dialog; storage failure reports but preserves cleared in-memory truth for retry |
| `S7_PHOTO_PREP` | `W-SHELL` / capture settings | T8: choose frame and 3/5/10s, then enter clean view | three frame choices, countdown choice, permission status, start/cancel | photo entry from S1 or S5 | start → S8; cancel → prior lock state | permission denied remains S7 with system-settings guidance; layout unchanged |
| `S8_PHOTO_READY` | Stage / clean MR pre-roll | T8: stabilize one clean frame or exit | all editing UI/ray hidden; selected frame only; 150ms pre-roll; trigger exit taught before entry | valid photo settings + capture capability | automatic `system.photoPreRollReady` → S9; trigger → prior S1/S5 | capability lost → S10 failure then restore UI |
| `S9_PHOTO_COUNTDOWN` | Stage center / time | T9: hold framing until capture | frame + central 3/2/1 animation; no ray/editor UI | countdown start | tick zero invokes public capture/save → S10; trigger cancels and exits photo mode | lifecycle interruption cancels capture and restores UI; no partial gallery record claimed |
| `S10_CAPTURE_RESULT` | brief Stage result / recovery | T9: confirm save result | success check or failure message; layout/frame retained | capture callback | success auto-dismiss → S8 or trigger exits; failure restores S7 for retry | permission/storage/compositor failure gives specific reason and always restores controls |
| `S11_TRACKING_RECOVERY` | `W-SHELL` / safety | Stop manipulation and regain valid spatial state | solid-backed warning; last valid snapshot; retry/return entry | Stage tracking/space invalid | regain → prior safe S1/S3/S5; close Stage → S0 | invalid restored coordinates can be discarded/reset; no entity mutation during recovery |

Panel visibility is an orthogonal flag `panelVisible`; menu toggles it in S1/S3/S5 only. Layout lock is explicit S5 because it changes the legal transition set. Autosave is a debounced side effect after committed mutations and on lifecycle boundaries; active gesture states are never persisted.

### 6.2 Transition graph

| Transition ID | From → To | Trigger event | Executed action(s) before target | Explicit confirmation | Guard / fallback |
|---|---|---|---|---|---|
| `TR-ENTER` | S0→S1 | `user.enterStage` | `validateSnapshot`, `openStageMixed`, `restoreValidOrbs` | yes | on failure remain S0 and show retry |
| `TR-PLACE-AIM` | S1→S2 | `user.chooseRecipe` | `setPendingRecipe`, `showPlacementReticle` | no | unlocked and orbCount<8 |
| `TR-PLACE-COMMIT` | S2→S1 | `controller.triggerPressed` | `clampPose`, `createOrb`, `scheduleAutosave` | no | valid tracking/pose; otherwise remain S2 disabled |
| `TR-SELECT` | S1→S3 | `controller.triggerClickOrb` | `setSelectedOrb`, `showInspector` | no | unlocked and target exists |
| `TR-DRAG` | S3→S4 | `controller.triggerHoldMoved` | `cancelDeleteHold`, `beginDragFromLastValidPose` | no | movement threshold crossed before 2s |
| `TR-DRAG-END` | S4→S3 | `controller.triggerReleased` | `commitLastValidPose`, `scheduleAutosave` | no | invalid release rolls back |
| `TR-HOLD-DELETE` | S3→S1 | `controller.triggerHeldStationary2s` | `deleteSelected`, `scheduleAutosave`, `announceDeleted` | yes via 2s progressive hold cue | unlocked, stationary threshold, target still exists |
| `TR-LOCK` | S1/S3→S5 | `user.lockEnabled` | `finishOrCancelTransientSelection`, `persistLockState` | no | no active drag; otherwise defer until release |
| `TR-UNLOCK` | S5→S1 | `user.lockDisabled` | `persistLockState`, `restoreTargetAffordances` | no | always available from visible panel |
| `TR-CLEAR-OPEN` | S1/S3→S6 | `user.clearAllRequested` | `openClearDialog(count)` | no | count>0, unlocked |
| `TR-CLEAR-CONFIRM` | S6→S1 | `user.clearAllConfirmed` | `deleteAllOrbs`, `scheduleAutosave` | yes | second action required |
| `TR-PHOTO-PREP` | S1/S5→S7 | `user.photoRequested` | `rememberReturnLockState`, `checkCapturePermission` | no | no active gesture |
| `TR-PHOTO-READY` | S7→S8 | `user.startPhotoMode` | `hideEditorUi`, `hideControllerRay`, `applyFrame`, `startCleanPreRoll(150ms)` | yes | public capture path and permission available |
| `TR-COUNTDOWN` | S8→S9 | `system.photoPreRollReady` | `startCountdown(selectedSeconds)` | no | automatic after clean view is confirmed; selected value must be 3/5/10 |
| `TR-CAPTURE` | S9→S10 | `timer.reachedZero` | `captureMrCompositorPublicApi`, `saveViaMediaStore` | no | failure is explicit result, never silent |
| `TR-PHOTO-EXIT` | S8/S9/S10→S1/S5 | `controller.triggerPressed` | `cancelPendingCaptureIfSafe`, `removeFrame`, `restoreUiAndRay` | no | returns to remembered lock state |
| `TR-PANEL` | same state | `controller.menuPressed` | `togglePanelVisibility` | no | works in S1/S3/S5; ignored in photo mode where trigger exits |
| `TR-TRACKING-LOST` | S1–S5/S8–S9→S11 | `system.trackingInvalid` | `freezeInteractions`, `rollbackActiveDrag`, `restoreUiIfPhoto` | no | retain last valid committed layout |
| `TR-STAGE-EXIT` | S1–S11→S0 | `user.closeStage` or `system.stageClosed` | `flushAutosave`, `closeStage`, `showSharedEntry` | yes for user exit; system exit is reported | S0 always remains the stable return |

### 6.3 Core happy path

`S0 explicit entry → S1 choose 日落 → S2 place → S1 choose 月光 → S2 place → S3 tune color/brightness/radius → S5 lock and verify manipulation rejection → S7 choose frame/countdown and compose → Start → S8 150ms clean pre-roll → automatic S9 countdown → S10 save result → trigger exit to S5 → unlock/continue or close Stage to S0`.

The flow preserves the layout through photo failure, tracking loss and Stage return. Restoring invalid coordinates drops only invalid records with a visible summary; it never invents a spatial anchor guarantee.

## 7. Layout skeleton, eye-hand input, motion, and placement geometry

### 7.1 Composition derivation

| Layout ID / states | Derivation evidence | Single primary focus | Regions and ownership | Density limit | Default / Large / Constrained reflow | Rejected option |
|---|---|---|---|---|---|---|
| `L-ENTRY` / S0 | T0 is one consequential decision; restore/capability data is supporting; Stage requires explicit entry. | `EntryGate.primaryAction` | `R0_Title` app identity; `R1_Status` Passthrough/restore summary; `R2_Action` enter + exit/help | ≤1 primary action, ≤2 status rows, ≤1 paragraph | Default 1 column; Large caps body width at 560dp; Constrained scrolls body but pins action | Multi-page onboarding/TabBar adds steps and duplicates a single decision. |
| `L-PALETTE` / S1,S2,S5 | T2/T6/T7 are frequent; `orbCount`, lock and save status are always relevant; room must remain visible. | Preset/custom choice (or unlock when S5) | `R0_Header` n/8 + save/tracking; `R1_Context` 2×2 presets + custom; `R2_Global` lock/clear/photo; optional first-use coachmark | 4 preset cells + 1 custom action; 3 global actions; one status line; no world thumbnail | Default 2×2 grid; Large may show selected summary beside grid but only palette is emphasized; Constrained is 1-column/2-row horizontal chips with internal scroll and pinned global row | Persistent eight-slot rail makes inventory primary and occludes Passthrough. |
| `L-INSPECTOR` / S3,S4 | T4/T5 need current identity and exactly three live parameters; movement feedback remains in world. | Selected orb identity + current parameter being adjusted | `R0_Header` preset/name + deselect/delete hint; `R1_Color` wheel/swatches; `R2_Params` brightness/radius; `R3_Global` lock/photo/back | 1 color control, 2 sliders, 2 secondary actions; no duplicated preset catalog | Default vertical inspector; Large splits color vs sliders 1:1; Constrained uses swatch strip plus sliders and internal scroll; hit targets unchanged | Side Subwindow wastes full height and forces two simultaneous workspaces. |
| `L-PHOTO-PREP` / S7 | T8 has two bounded choices and one start decision; permission is a guard. | “开始摄影” after settings | `R0_Header` capture/permission status; `R1_Frame` 3 frame choices; `R2_Timer` 3/5/10; `R3_Action` start/cancel | 3+3 options, 1 primary, 1 secondary; no editing controls | Default two rows; Large keeps max content width 720dp; Constrained stacks rows and pins start | Choosing settings inside clean capture mode would violate “hide UI” and complicate trigger-only exit. |
| `L-STAGE-WORKSPACE` / S1–S5,S11 | T1/T3/T4/T5 need real-room direction/distance/overlap; panel is supporting. | World orb/placement target | `WORLD_Orbs`; `WORLD_SelectionCue`; `WORLD_Reticle`; `PANEL_W-SHELL`; `WORLD_Notice` transient | ≤8 orbs; ≤1 selected shell; ≤1 reticle; particle budgets are per-preset and bounded; one panel | Stage entities do not scale with window resize. Panel follows §5.4 tiers; if hidden, world cues remain sufficient except commands unavailable until menu toggle | Multiple property panels attached to each orb create clutter and overlapping gaze targets. |
| `L-PHOTO` / S8–S10 | T8/T9 require clean composition, chosen frame and central time feedback; editor/ray must be absent. | MR composition; countdown becomes temporary primary focus | `WORLD_Frame`; `WORLD_Countdown`; `WORLD_Result`; underlying orbs/Passthrough | 1 frame, 1 countdown value, 1 brief result; zero editor components/rays | Head-relative frame preserves aspect; countdown center stays within clear FOV; Reduce Motion cross-fades rather than scales | Persistent camera toolbar conflicts with UI-free requirement. |
| `L-RECOVERY` / S6,S11 and permission/capture errors | Destructive or safety exceptions must preserve the layout and expose smallest recovery. | Confirm or retry action | `R0_Reason`; `R1_Impact`; `R2_Actions` cancel/retry/clear/return | ≤2 actions and ≤3 short text rows | Dialog minimum height ≥184dp; at Constrained it overlays and scrolls text without shrinking targets | Toast-only errors are too transient; full new windows break return context. |

### 7.2 Authoritative region geometry

`W-SHELL` content coordinates use top-left origin after the 96dp shell and 16dp content inset. Values below are content geometry, not a second window-size definition.

| Layout / tier | Region geometry `x,y,w,h` dp | Gap/alignment | Content owner |
|---|---|---|---|
| `L-ENTRY` default | R0 `0,0,688,56`; R1 `0,72,688,220`; R2 `0,308,688,104` | 16dp vertical; exactly 412dp total | EntryGate |
| `L-PALETTE` default | R0 `0,0,688,68`; R1 `0,84,688,328` | 16dp vertical; R1 holds 2×2 palette + custom and optional coachmark | WorkspaceGuardBar + LightPalette |
| `L-INSPECTOR` default | R0 `0,0,688,68`; R1 `0,84,688,328` | 16dp vertical; Inspector owns its completion control, Guard owns lock/clear/photo | WorkspaceGuardBar + OrbInspector |
| `L-PHOTO-PREP` default | R0 `0,0,688,48`; R1 `0,64,688,244`; R2 `0,324,688,88` | 16dp vertical; PhotoSetup owns all three contiguous regions | PhotoSetup |
| Constrained 488×252 content | R0 `0,0,488,56`; R1 `0,64,488,188` | 8dp; guard/action labels compact, current primary internally scrolls/pins its action | WorkspaceGuardBar + current primary; EntryGate uses its own 252dp grid |
| Large 888×512 content | R0 `0,0,888,68`; R1 `0,84,888,428` | 16dp; R1 may use two columns but only one primary emphasis | WorkspaceGuardBar + palette/inspector; EntryGate remains width-capped |

Stage/world coordinates use meters, right-handed Stage-local space, with the Stage center under the wearer. User-authored positions remain the source of truth after finite-value and tracking-validity checks.

| World element | Anchor / local geometry | Orientation / size | Z/depth relation and guard |
|---|---|---|---|
| `LightOrb` core | User placement pose `(x,y,z)` | Billboard-independent sphere, visual core diameter 0.12m; effect-specific particle emitter bound to orb | World entity; position must be finite and valid in the current tracked Stage; invalid restore record is skipped with summary |
| Influence shell | Same orb origin | Radius exactly 0.5–5.0m; sparse wire/shell visualization shown for selected/hovered only | Behind selection outline; communicates aesthetic reach, not photometric truth |
| Selection cue | Same origin | Double ring sized core diameter +0.04m; always faces viewer for label readability | Slight visual offset toward viewer; outline + glyph + text, never color only |
| Placement reticle / ghost | Controller-ray hit pose or ray point supported by the public interaction surface | 0.10m reticle + 0.12m ghost orb | One reticle only; commit disabled when tracking/pose invalid |
| Drag tether | Last committed pose to current ray pose | 0.006m line, max one | Removed on release/rollback; motion threshold arbitrates delete |
| `W-SHELL` | System-managed Planar launch at ~1.75m, user movable; desired lower-central attention region | Always faces its window plane; 720×540dp default | Supporting near layer; may hide via menu; must not cover selected orb by automatic repositioning |
| Photo frame | Head-relative visual overlay matching compositor-safe bounds | White/film/instant variants; no editing chrome | Foreground captured layer; no ray; full bounds must be device-calibrated |
| Countdown/result | Head-relative center | numeral uses fixed center and opacity transition; result below center | Nearest temporary layer; Reduce Motion removes scale pulse |

Placement is not silently snapped to walls because v1 does not request plane detection. “任意位置” means a finite valid Stage pose along the ray, bounded only by current tracking and renderer visibility; the influence radius is not a collision or physical-light promise.

### 7.3 Eye-hand/controller interaction contract

| Input / gesture | Target/context | Mapping | Feedback | Arbitration / recovery |
|---|---|---|---|---|
| Gaze focus + pinch/tap | Every `W-SHELL` interactable | Activate built-in Button/ToggleableChip/Switch/Slider/AlertDialog behavior | SpatialUI built-in hover, press indication, controller haptic where applicable | Controller remains equivalent fallback; focus loss cancels uncommitted press |
| Controller ray + trigger click | Preset/custom/panel action | Select/activate current control | Built-in hover/press/haptic | Disabled states consume input and announce reason |
| Controller ray + trigger click | Empty Stage valid pose | Commit pending ghost orb | Reticle solidifies; spawn transition; n/8 updates | Only in S2, unlocked, count<8 and tracking valid |
| Controller ray + trigger click | Existing orb | Select orb | Double-ring + named chip + inspector | Blocked in S5 with lock glyph/text feedback |
| Trigger hold + ray displacement ≥0.03m before 2s | Selected orb | Begin/continue drag; release commits | Tether + live pose + haptic at pickup/release | Movement cancels delete progress; invalid pose rolls back to last valid position |
| Trigger stationary hold 2.0s (displacement <0.03m) | Selected orb | Delete at complete | Radial progress 0–100%, “松开取消”; completion haptic | Release before 2s cancels; lock/tracking loss cancels; no delete while dragging |
| Controller menu key | S1/S3/S5 | Toggle `panelVisible` | Brief panel fade and status sound | Always retained while locked; ignored in photo mode to keep the trigger-only exit model clear |
| Trigger in S8/S9/S10 | Photo mode | Exit/cancel photo mode and restore prior lock/UI/ray state | Frame/countdown cross-fade out | Countdown starts automatically after S8 pre-roll; trigger is never overloaded as capture. If a public capture call is already non-cancellable, wait for callback but restore UI and report result |
| System back | Dialog → selected → workspace → Stage | Close dialog; deselect; otherwise request Stage close and return S0 | Focus returns to prior safe element | Destructive work never occurs on back; active drag rolls back before exit |
| Color/brightness/radius control edit | S3 inspector | Clamp HSV to supported range, brightness 0–100%, radius 0.5–5.0m; update in current render frame | Numeric value + world cue update | Non-finite input rejected; autosave occurs after committed edit debounce |

All custom Compose interactables use modifier order `size/clip → spatialHoverEffect → clickable/toggleable → decoration → padding`, `LocalIndication.current`, and the same `MutableInteractionSource` for controller haptic. Built-in SpatialUI components do not receive an extra hover modifier. World entities use the SDK’s public pointer/interaction component path discovered from the scaffold; no private controller API is assumed.

### 7.4 Motion and effects

| Motion ID | Trigger / purpose | Duration / easing | Spatial range | Reduce Motion | Performance fallback |
|---|---|---|---|---|---|
| `M-HOVER` | Focus interactive UI/orb; target confirmation | Built-in SpatialUI or 150ms standard ease; scale ≤1.04 | UI local or orb outline only | Brightness/stroke change, no scale | Drop scale, keep outline/text |
| `M-SPAWN` | Orb placed; acknowledge creation | 220ms decelerate, opacity 0→1 and core 0.85→1 | ≤0.018m core delta | 120ms cross-fade only | Immediate appear if frame budget breached |
| `M-SELECT` | Orb selection changes | 160ms standard; double-ring opacity | ≤0.02m outline offset | Instant outline/text | Disable interpolation |
| `M-DRAG` | Live movement | Direct 1:1 pointer pose, no smoothing that causes lag; tether updates each frame | User-authored range | Same direct motion | Reduce particle emission to zero while dragging |
| `M-HOLD-DELETE` | Stationary trigger hold | Exactly 2000ms linear radial progress | 0.14m ring around orb | Same progress without pulse | Ring updates at 15Hz while semantic timer remains accurate |
| `M-SUNSET-FOG` | Sunset orb active | Slow non-flashing drift, 4–8s particle life | Within min(radius, 1.2m) effect envelope | Static sparse warm haze | ≤32 particles/orb then ≤12; disable at low tier |
| `M-MOON-DUST` | Moon orb active | Slow star-dust drift, no full-screen flicker | Within min(radius, 1.0m) | Static points | ≤24 then ≤8; no additive bursts |
| `M-NEON-SCAN` | Neon orb active | 1200ms continuous band traverse, no rapid flash | Orb core + ≤0.2m glow | Static magenta/cyan rim | 15fps effect or static rim |
| `M-STAGE-EDGE` | Stage preset active | Static sharp cone/edge; optional 240ms intensity settle | Within selected radius visualization only | Static | No particles; reduce edge samples |
| `M-LOCK` | Lock toggled | 180ms fade/stroke transition | UI/orb cues only | Instant semantic swap | Immediate state update |
| `M-FRAME` | Frame style changes before photo entry | 180ms cross-fade | Head-relative frame | Instant replace | Immediate replace |
| `M-COUNTDOWN` | Each 3/5/10s tick | 200ms opacity 0→1→0; fixed center | No camera/world motion | Fixed numeral with opacity only | Text swap at 1Hz |
| `M-CAPTURE` | Capture callback | 240ms small check/error ring, no full-screen flash | Central ≤8° | Static icon/text | Immediate result text |

Camera pose is never animated. No continuous full-screen flashing, forced head movement or large moving peripheral surfaces are permitted. Global effect budget is bounded for eight orbs; quality degrades by particle density/refresh, never by changing persisted brightness/radius semantics.

### 7.5 Accessibility contract

| Contract | Enabled behavior |
|---|---|
| `reduceMotion` | User setting plus system preference; disables pulses/scale/scan traversal/particle drift, keeps opacity, outline and fixed labels. |
| `controllerFallback` | Controller ray/trigger/menu covers every required task; gaze+pinch is supported on panel controls where the runtime provides it. |
| `colorIndependentSemantics` | Selection uses double-ring + name; locked uses lock glyph + text; error triangle + text; saved check + text; preset names/effect glyphs accompany color. |
| `textScaling` | SpatialUI typography roles; content reflows/scrolls at larger scale, never scales hit targets or the whole panel down. |
| `stableExit` | Trigger exits photo mode; system back unwinds transient states; closing Stage always returns to S0 Shared entry. |
| `handedness/reach` | No edge-only destructive target; ray interaction avoids direct-reach requirement; panel remains user movable. |
| `comfort` | Primary focus central; no auto camera movement; bounded particles; direct drag; device FOV/fatigue validation remains required. |

## 8. Minimum Completeness Gate

| Check | Evidence | Verdict |
|---|---|---|
| Principles and tasks | §§1–2; every task has input/output/error/frequency/dependency/duration | pass |
| Spatial value and concept | §§3–4 include per-task 2D counterfactual, three substantial hypotheses, matrix and rejection evidence | pass |
| Container/attachment/sizing | §5 declares legal Shared→Full chain, entry value/exit, attachment alternatives and a three-candidate PICO sizing derivation | pass |
| States/flow | §6 covers S0–S11, stable transitions, confirmations, photo/tracking failure recovery and happy path | pass |
| Implementation spec | §§7.2–7.5 define authoritative geometry, input arbitration, motion/effect fallbacks and accessibility | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |
