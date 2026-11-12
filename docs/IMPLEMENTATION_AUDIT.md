# Cyber Protocol — Implementation Audit

**Audit baseline:** `main` at `9589ca1e18431b3af0a433bd9b5e6378fce38d4c`  
**Audit date:** 2026-11-12  
**Scope:** canonical design, story continuity, Android/Kotlin source, Python runner, local persistence, VFS, app surfaces, tests, and CI workflow.  
**Purpose:** identify what is genuinely implemented, what is partial, and what must be built next. This is a source review; it is not a physical-device acceptance report.

## Executive result

Cyber Protocol is a substantial playable foundation, not a blank project. The current branch contains the approved First Contact opening, two required Python onboarding missions, six retired legacy coding missions retained for save/history compatibility, a local virtual filesystem, mission-scoped Python file access, campaign persistence, a seven-app A.E.G.I.S. hub, and a persisted RX-14 case-flow vertical slice.

However, the newly approved product direction is **not yet implemented end-to-end**. In particular:

1. Evidence records exist, but the Evidence & Investigation Engine is incomplete: evidence provenance is mostly a free-text source string, relationships and derived findings are absent, and review history/reasoned status transitions are not modeled.
2. The current evidence statuses (`UNVERIFIED`, `INFERRED`, `CONFIRMED`, `DISPUTED`) do not fully express the approved distinction between corroboration and verification. Successful Python mission evaluation currently creates `CONFIRMED` evidence automatically, which risks overstating what a technical pass proves.
3. Mission definitions are still primarily coding/tutorial contracts. They do not yet model the five approved mission archetypes or the flexible five-phase case experience.
4. The campaign remains First Contact. No new post-First-Contact case has been authored or approved; do not invent one during implementation.
5. English-first localization is documentation-only at this baseline. The Language destination is an informational placeholder; there is no language identifier/preference model or complete English content set.
6. Some app surfaces are data-backed but remain read-only or weakly cross-linked. A visible screen is not proof of a complete workflow.
7. The main Compose shell still owns a large amount of navigation, editor, Browser, Messenger, and Terminal state; the ViewModel owns campaign state/progression but not the whole session.

**Implementation recommendation:** preserve the playable First Contact chapter, establish the evidence schema and audited review contract first, then integrate that vertical slice across the apps. Build localization foundation and migrate player-facing content in explicit coverage stages. Add a later case only after its evidence chain and evaluation contract are specified.

## 1. Repository and architecture baseline

### Present

- Android-first Kotlin application using Jetpack Compose.
- Separate core models, data/repository code, domain use cases, Python execution adapter, UI screens, and tests.
- Chaquopy-hosted Python runner with AST/resource guardrails.
- Versioned local campaign state (`GameState`, current schema version 5), codec, local repository, previous-good-snapshot/corruption recovery foundations, and legacy migration tests.
- Game-owned persistent VFS; mission workspaces use file allowlists and explicit writable-file sets.
- Virtual terminal commands operate on the game VFS, not the Android/Linux host shell.
- Mission RUN executes the player's code; SUBMIT is a separate evaluator action and requires the exact source from the latest successful run.
- Messenger, Browser, Terminal, Database, Dossier, Analysis, and Operations screens exist.
- Messenger/Browser delivery paths can install the same underlying mission asset into the active workspace; VFS and workspace tests cover important permission behavior.

### Partial / remaining

- `CyberProtocolApp.kt` remains a large Compose shell with many session and navigation concerns.
- `GameSessionViewModel` owns campaign state, progression, evidence recording, and code checkpoints, but not all app/session state.
- The Python runner executes in-process. Existing limits are teaching-runtime guardrails, not a hardened hostile-code sandbox.
- Several screens are read-only or informational; their existence must not be counted as a complete cross-app workflow.
- Physical-device acceptance (clean install, in-place update, save preservation) has not been completed.

## 2. Evidence & Investigation Engine

### Existing foundation

- `EvidenceRecord` currently stores ID, title, summary, free-text source, status, mission ID, and recorded timestamp.
- `RecordEvidenceUseCase` prevents an evidence ID from being reassigned to another mission, avoids duplicate IDs, and preserves the original recorded timestamp on an update.
- It exposes `updateStatus`, but that operation currently changes status directly and only updates the generic checkpoint string.
- Evidence is serialized in the versioned `GameState` snapshot.
- Successful First Contact mission submissions create mission-provenanced records; the recruitment discrepancy is separately recorded as `UNVERIFIED`.
- Analysis supports search/filter/order and is intentionally read-only.
- Intelligence Database can inspect evidence fields and navigate to the related mission briefing.
- Archives show stored evidence and source text.

### Gaps against the approved contract

- Source provenance is not structured (source type/channel/asset ID/context/timestamps are not separate fields).
- Evidence does not have structured links to related evidence, hypotheses, reports, or derived findings.
- There is no separate immutable review/audit-history list recording reviewer/action/reason/previous status/new status/time.
- Status transitions are unrestricted; no authored transition policy or review action contract exists.
- `CONFIRMED` is currently assigned automatically after a Python evaluator succeeds. A successful technical task must not automatically certify the truth or reliability of an intelligence claim.
- Current enum lacks a distinct `CORROBORATED` state and uses `CONFIRMED` where the approved vocabulary distinguishes corroboration from explicit verification.
- Database, Dossier, Analysis, Operations, Messenger, Browser, and VFS do not yet share a complete evidence graph or case-level investigation workspace.
- There is no cross-source independence/corroboration logic. Agreement between records is not automatically proof.

### Required implementation contract

1. Define a versioned evidence model with stable IDs, source/provenance metadata, epistemic status, related-record references, and derived-finding support.
2. Define legal status transitions and require an explicit review action with a nonblank reason; append an immutable audit entry rather than silently overwriting history.
3. Preserve original source assets and make derived findings separate records.
4. Treat technical execution/evaluator success as evidence about that execution only. Do not promote intelligence evidence to verified solely because code ran or a mission passed.
5. Add backward-compatible migration from GameState schema 5 and regression tests for old saves, duplicate IDs, missing relations, invalid transitions, and audit-history round trips.
6. Integrate the same underlying records across app surfaces; avoid disconnected copies.

## 3. Guided Intelligence RPG and mission architecture

### Existing foundation

- First Contact has fourteen story beats and eight evaluated Python missions.
- Mission catalog entries contain objective, hints, workspace permissions, evaluator ID, reward XP, and clearance metadata.
- Story progression, app unlocks, mission completion, XP/rank, debriefs, and retry feedback have deterministic foundations.
- Existing mission code clues connect the beginner technical learning sequence.
- Current story preserves the approved official A.E.G.I.S. recruitment notification → Freya introduction → NEXUS unlock sequence.

### Gaps

- `MissionKind` currently distinguishes `STORY_CODE`, `INVESTIGATION`, and `COMBINED`; it does not encode the five approved primary archetypes: Narrative, Investigation, Technical, Analytical, and Operational.
- The existing catalog is still dominated by `STORY_CODE`; the current eight evaluated missions are technical Python learning tasks.
- There is no first-class case/problem model that owns the case objective, known/unknown facts, constraints, leads, evidence graph, decision options, and debrief.
- The five experience phases are not represented as a flexible case progression model. The current campaign is still mainly story-beat/mission driven.
- Evaluation is largely technical evaluator behavior; separate deterministic criteria for evidence discovery, analytical reasoning, uncertainty, and operational decisions are not yet implemented.
- Rank, XP, and clearance exist, but separate Technical Competency and Intelligence Competency progression is not implemented.
- A later investigative case is intentionally not approved. Do not create story canon or a new case merely to demonstrate the new archetypes.

### Required implementation contract

- Preserve First Contact story beats and all eight evaluated Python missions.
- Introduce archetype/case metadata without forcing all five phases or every app into every mission.
- Keep optional leads optional and ensure the primary objective is solvable from discoverable required information.
- Make failed attempts retryable without reducing rank, XP, clearance, relationships, access, or story progress.
- Keep technical correctness and investigative interpretation as separate evaluation dimensions.
- Define and approve the next case's evidence chain, primary archetype, optional leads, and deterministic completion criteria before authoring it.

## 4. Localization

### Existing state

- `docs/LOCALIZATION_PLAN.md` defines English-first, Indonesian-supported behavior and L0–L5 rollout.
- The Language destination currently displays a static message that English is unavailable.
- A review of the current UI and campaign source shows player-facing strings are largely hard-coded in Indonesian; the Android resource tree has no complete localization resource set.
- No language identifier, persistent language preference, localized story/mission content catalog, missing-key check, or language-switch regression suite exists.

### Required implementation contract

- Implement the approved English fresh-install default and a persistent in-game English/Bahasa Indonesia choice.
- Move reusable UI copy into Android string resources and authored campaign/dialogue content into localized data keyed by stable IDs.
- Do not translate Python syntax, identifiers, standard command names, virtual paths, mission/evidence IDs, or evaluator-sensitive output.
- Do not advertise bilingual parity until every player-facing route has both language variants.
- Language preference must remain independent from campaign save data; switching language must not mutate progress, evidence, drafts, rank, or unlocks.

## 5. App-surface integration

| Surface | Current source evidence | Main remaining work |
|---|---|---|
| Messenger | Separate A.E.G.I.S., Freya, and NEXUS conversations; local reply history; authored attachments | Case-aware evidence references, full localization, richer authored investigative interactions |
| Browser | Offline intranet, local fictional login, repository downloads, Mission Control submission | Case/lead workflow, evidence links, localization, broader archive navigation |
| Terminal | Virtual commands and mission workspace execution | Case-aware evidence inspection commands only if designed; do not expose host shell |
| Code Editor | Python editing, syntax highlighting, RUN integration, saved drafts/checkpoints | Preserve exact source semantics and localization boundaries |
| Intelligence Database | Searchable mission/evidence indexes and detail panels | Evidence relationships, provenance structure, review history, case graph navigation |
| Dossier | Campaign/personnel/qualification data | Clear separation of rank, clearance, Technical Competency, Intelligence Competency |
| Analysis | Search/filter of evidence; read-only | Structured comparisons, hypotheses/findings, uncertainty, explicit audited review workflow |
| Operations | Mission list, active mission resume, campaign state | Case objective, flexible phase, decision/submission, case-aware debrief |

## 6. Persistence and quality

- GameState schema 5 already serializes evidence and player code. Any new evidence or localization preference schema must be migrated safely.
- Keep language preference in a dedicated settings store, not the campaign save.
- Add tests at model/use-case/codec/repository/UI integration boundaries.
- Preserve existing regression coverage for First Contact, RUN/SUBMIT, mission workspaces, VFS permissions, and save migration.
- Do not claim runtime correctness based solely on source tests or APK assembly.

## 7. Ordered implementation batches

### Batch 01 — Audit and specification alignment
**This document records the baseline audit.** Keep it updated as implementation changes.

Acceptance:
- Every major subsystem has an explicit present/partial/missing status.
- Conflicting implementation notes in other docs are corrected.
- No new story canon is introduced.

### Batch 02 — Evidence Engine core
- Approve exact evidence/provenance/relation/review models and migration rules.
- Implement safe status vocabulary and audited review transitions.
- Add codec, migration, lifecycle, and transition tests.
- Correct automatic `CONFIRMED` assignment from technical mission success.

### Batch 03 — Evidence vertical slice
- Connect one existing First Contact discrepancy and its original sources through Messenger/Browser/VFS, Database, Dossier, Analysis, and Operations where the story already supports it.
- Add source comparison and derived findings without rewriting the original evidence.
- Keep source records read-only and preserve provenance.

### Batch 04 — Case and RPG architecture
- Add case and mission-archetype contracts.
- Model the five experience phases as flexible state/context, not mandatory screens.
- Separate technical and intelligence competency progression.
- Preserve First Contact and its current evaluators.

### Batch 05 — Localization L1–L5
- Build language preference and resource/data boundaries.
- Localize shell, First Contact content, and all operational app surfaces.
- Add key/content parity and language-switch persistence tests.
- Only then declare English and Indonesian player-facing parity complete.

### Batch 06 — App integration and quality
- Complete case-aware app navigation and cross-linking.
- Expand deterministic multi-dimension evaluation and retry behavior.
- Continue ViewModel/state extraction and test persistence/recovery.
- Run full CI, create development APKs for iterative device testing, and create a Final APK only through the exact-APK emulator smoke-test pipeline.
- Complete physical-device clean install/update/save-preservation acceptance before production readiness.

## 8. Definition of done

A batch is complete only when its behavior contract, code, tests, integration, persistence/migration (where applicable), and documentation agree. A screen alone, successful compilation alone, or development APK alone is not completion. The final release label is reserved for the exact permanently signed APK that passes the required emulator smoke test; physical-device acceptance remains a separate gate.


## 9. Refresh against current main

**Refresh commit:** `9589ca1e18431b3af0a433bd9b5e6378fce38d4c`  
**Refresh date:** 2026-11-12  
**Result:** the earlier gap assessment remains valid. This refresh rechecked the current source tree and confirmed the following concrete implementation facts:

- `Campaign01Catalog.missions` contains eight First Contact mission definitions: `signal`, `identity`, `audit`, `inventory`, `trace`, `compare`, `normalize`, and `validate_delta`. They are all currently authored as `MissionKind.STORY_CODE`; evaluator IDs are 8–15.
- `GameState` remains schema version 5. Evidence still has only a free-text `source`, four statuses (`UNVERIFIED`, `INFERRED`, `CONFIRMED`, `DISPUTED`), mission ID, and recorded timestamp. No structured provenance, evidence relation graph, or audit trail is present in the model or codec.
- `RecordEvidenceUseCase.updateStatus` still allows direct status replacement without reviewer, reason, or transition validation.
- `CyberProtocolApp.kt` still owns a large portion of application navigation and working-session state. The current file is approximately 92 KB; `GameSessionViewModel` owns campaign mutations but not the complete app session.
- The Language route remains an informational placeholder stating that English is unavailable. The current Android resource tree does not contain a complete `values/strings.xml` and `values-en/strings.xml` localization implementation.
- Analysis filters evidence and displays records; Database provides mission/evidence browsing; Operations is still a campaign mission-control view. These are useful foundations, not yet an integrated case workflow or audited evidence-review system.
- The Python runner's module documentation explicitly describes it as a restricted teaching runner, not a hardened sandbox. Its AST and resource limits must not be represented as process isolation.
- Existing source tests cover the Python evaluator, First Contact catalog/story, save codec/migration, VFS/workspace policies, app session state, and selected UI/domain behavior. They do not yet cover the missing evidence review model, localization parity, or case/archetype evaluation because those systems do not exist.

### Updated implementation status

| Area | Current status | Evidence / next gate |
|---|---|---|
| First Contact story and eight evaluated missions | Implemented foundation; regression-tested | Preserve all existing mission IDs/evaluator contracts |
| VFS, workspace permissions, RUN/SUBMIT | Implemented foundation; regression-tested | Continue security-boundary and exact-source regression tests |
| Local campaign persistence | Implemented, schema v5 | New evidence fields require backward-compatible migration |
| Evidence & Investigation Engine | Partial | Structured provenance, relations, review history, safe transitions |
| Guided Intelligence RPG case model | Not implemented end-to-end | Case model, archetypes, flexible phases, multi-dimension evaluation |
| English-first localization | Not implemented | L1 foundation through L5 parity gate |
| Cross-app case/evidence workflow | Partial | Shared evidence graph and audited review across app surfaces |
| App shell/session architecture | Partial | Extract navigation/session ownership incrementally |
| CI development/final APK distinction | Implemented in workflow/docs | Development APK may skip smoke test; final APK must smoke-test exact published binary |
| Physical-device acceptance | Not completed | Clean install, in-place update, and save-preservation checklist |

This refresh does not introduce story canon or change gameplay implementation. It updates the audit baseline only. The next code batch remains **Batch 02 — Evidence Engine core**, preceded by a small design contract for the evidence schema, provenance, status vocabulary, transitions, review audit entries, and schema-5 migration.


## Batch 02 — Evidence Engine core

**Implementation checkpoint:** schema 6 / Evidence Engine domain foundation.

- Added structured evidence provenance: origin surface, source label, optional shared-asset ID and workspace path, collection timestamp, and collector identity.
- Added typed evidence relationships (supports, contradicts, corroborates, derived-from, duplicates) with self-link and duplicate guards.
- Added corroborated and retracted assessment states while retaining all prior status values.
- Replaced unaudited direct status replacement with a review operation requiring reviewer identity and a non-empty reason. Accepted transitions append review history; retracted records cannot be reactivated.
- Re-recording an existing evidence ID preserves its mission assignment, original timestamp, provenance, current status, and review history.
- Added schema-5-compatible decoding. Legacy evidence is retained and assigned LEGACY provenance from its source and timestamp; the existing repository version-upgrade path persists the upgraded snapshot.
- Added regression coverage for serialization, legacy decoding, review audit history, transition restrictions, relation validation, idempotency, and provenance preservation.

**Scope boundary:** this checkpoint establishes the domain and persistence contract. It does not yet claim all app surfaces are connected to the evidence graph, VFS assets automatically create evidence, or a new investigative case is playable. A successful Python RUN or evaluator result is not itself an intelligence review and does not automatically classify arbitrary evidence as confirmed.


## Batch 04 — Guided Intelligence RPG architecture

- Added mission primary/secondary archetype metadata. Existing STORY_CODE missions default to the Technical archetype without changing their mission IDs, evaluator IDs, workspace contracts, objectives, or rewards.
- Added a reusable case contract with required and optional evidence IDs, one primary archetype, and deterministic decision options.
- Added persisted case progress for briefing acknowledgement, discovered evidence, analyzed evidence, selected decision, and debrief acknowledgement.
- Added a phase evaluator for Briefing → Investigation → Analysis → Operational Decision → Debriefing → Complete. Optional evidence enriches a case but does not block the standard case phase progression.
- Decision options may require explicitly listed optional evidence; those requirements are checked independently and deterministically.
- Bumped save schema to 7. Schema-6 and earlier saves retain existing campaign/evidence state and receive an empty case-progress map.
- No new post-First-Contact story case is authored in this checkpoint. A playable case remains gated on an approved source chain, primary archetype, optional leads, and decision/evaluation design.


## Batch 05 — Localization L1 foundation

- Added a dedicated language preference model for English (en) and Bahasa Indonesia (id).
- Fresh installations default to English regardless of Android system locale; the Compose resource configuration follows the saved in-game preference.
- Language preference is stored in a dedicated SharedPreferences file, separate from campaign progress and save schema.
- Replaced the Language placeholder with a functional selector and an explicit notice that localization coverage is still partial.
- Added separate default-English and Indonesian Android string resources for the main menu and Language screen.
- Migrated main-menu labels, descriptions, motto, and footer to stable resource keys.
- Full localization is not complete. Story dialogue, First Contact briefings/hints, and many operational screens still contain hard-coded Indonesian content; neither language should yet be advertised as fully localized.


## Batch 06 — Case progression and competency integration

- Added a deterministic case-progress use case for briefing acknowledgement, evidence discovery, analysis, operational decision, and debrief acknowledgement.
- Case evidence must belong to the authored case and exist in the shared evidence store before it can be discovered. Analysis requires prior discovery.
- Required evidence gates the normal case phases. Optional leads do not block the case, but a decision option may explicitly require an optional lead.
- Exposed case-progress mutations through GameSessionViewModel for future case screens without adding unapproved case story content.
- Added separate Technical Competency XP and Intelligence Competency XP. Mission archetype determines which track receives its reward; total campaign XP and rank remain independent.
- The Personnel Dossier now displays both competency tracks. Schema 8 persists them; older saves default both tracks to zero without changing campaign progress.
- Added regression tests for case phase gates, optional leads, competency awards/idempotency, and schema round trips.


## Batch 03 — First Contact evidence source vertical slice

- Added two immutable fictional source assets for the existing RX-14 timestamp discrepancy: the A.E.G.I.S. receipt at 09:41:12 and the archive receipt at 09:41:16.
- The assets are installed into the game-owned virtual filesystem when the story reaches the existing discrepancy checkpoint (story beat 10). They are not seeded into a fresh player's early workspace.
- Messenger attachments, Browser intelligence reports, the evidence database, and Terminal file access resolve the same stable asset IDs and VFS paths. The source preview reads the VFS content; it does not create a second copy.
- The two source records remain UNVERIFIED. The discrepancy record is a separate derived finding linked to both sources; the source pair is related as contradictory only on its recorded timestamp. No cause is asserted.
- Existing First Contact story beats and all eight evaluator contracts remain unchanged.
- Added VFS tests for gated installation, shared reads, and persistence across VFS reconstruction.

## Latest player decision — gameplay mechanics may be redesigned

**Decision date:** 2026-11-12

The previous preservation constraint for eight evaluated First Contact Python missions as a fixed linear sequence is superseded. The player explicitly authorized changing current gameplay mechanics whenever they conflict with the approved Guided Intelligence RPG.

The following remain canonical:
- First Contact's approved recruitment story, character roles, beginner-onboarding purpose, and RX-14 timestamp discrepancy.
- The original source records and the rule that their four-second difference does not prove tampering or its cause.
- Progressive Python teaching for beginners, while code is used in a case only when it naturally serves the problem.

The following are no longer immutable:
- The eight old mission boundaries and their mandatory linear progression.
- The old evaluator IDs as permanent gameplay gates.
- The requirement that every First Contact learning concept be passed as a separate mission.

Next implementation direction:
1. Rework First Contact into a coherent evidence-backed case using flexible case phases.
2. Keep essential Freya-led terminal/Python onboarding; move unrelated or repetitive exercises to optional practice/learning.
3. Retain only technically meaningful code work in the case; make investigation and analytical reasoning first-class gameplay.
4. Provide deterministic, fair decision evaluation, including a valid insufficient-evidence outcome.
5. Migrate old saves and drafts safely when catalog/evaluator contracts change.
6. Do not author an unapproved culprit, tampering explanation, or separate new case.
## RX-14 guided Operations vertical slice — 2026-11-12

Implemented on the current branch:
- Added a data-authored First Contact RX-14 case contract using the same two evidence IDs installed from the shared immutable VFS evidence assets.
- Replaced the Operations screen's fixed eight-mission list with a case-control experience: briefing, source inspection, analysis acknowledgement, operational decision, and debrief.
- Wired Operations actions to the persisted case-progress use cases and GameState.caseProgress.
- Added a valid 'insufficient evidence' decision alongside proportionate requests for clock-synchronization and processing-queue logs.
- Added a progression test that completes RX-14 without inventing a cause for the four-second discrepancy.
- Fixed the missing Campaign01StoryLocalization import found by CI.

This is a first playable case-flow vertical slice, not a complete campaign conversion. The story route now requires only the two beginner Python onboarding exercises; six later legacy missions are retired from mandatory progression and retained for save/history compatibility. Messenger/Browser/Database navigation is not yet automatically linked to case evidence discovery; Operations currently exposes the shared evidence records as the case's inspection surface. Verify UI behavior on an emulator before claiming the slice is fully playable.

## RX-14 Operations source-inspection interaction

The Operations investigation phase now opens the original receipt text from the shared VirtualFileSystem before the player can mark that source as inspected. The confirmation action records discovery only after the player has viewed the source dialog. The analysis phase remains a separate action. This is an incremental UI vertical slice; the broader redesign of the campaign and cross-app integration remains in progress. CI run #158 passed source tests and the development Debug APK build for this interaction. Final-release emulator smoke testing remains pending.


## Legacy mission route retirement — 2026-11-12

- Only `first_contact.signal` and `first_contact.identity` remain required Python onboarding missions in the First Contact story route.
- The six later Python exercises no longer map to story checkpoints. Their corresponding story beats now present source review, comparison, analysis, and operational decision context without requiring code.
- Existing completed mission IDs, earned XP, player drafts, and workspace files are preserved. Startup clears only a retired legacy mission's active pointer.
- The six old mission definitions/evaluator contracts remain temporarily for historical save and archive compatibility. A separate optional-practice interface has not yet been built.
- RX-14 progression is independent of completion of the six retired exercises.


## Editor submission control — direct mission submission

- The First Contact code editor exposes a dedicated **KIRIM / SUBMIT MISI** action alongside Save and Run.
- Save remains draft persistence only; Run remains execution only; Submit invokes the existing deterministic mission evaluator.
- Submit is enabled only when the exact current editor source matches the last successful run source. Editing after Run invalidates the eligible submission.
- Browser Mission Control remains a secondary submission entry point; both routes use the same submission function and policy.
- CI run #165 passed source tests and built the development Debug APK. Device-level interaction and final emulator smoke testing remain outstanding.

## RX-14 story completion gate and progression safety

- The story no longer advances to its final investigation report until the persisted RX-14 case reaches `CasePhase.COMPLETE`.
- The stage-14 story action opens Operations directly while retaining Story as the hub return destination. After the player completes the case and returns, the action changes to continue to the report.
- Repeated analysis of an already analyzed evidence record and repeated submission of the same operational decision are idempotent.
- `CaseProgress` enforces that analyzed evidence must be a subset of discovered evidence, preventing invalid persisted snapshots from representing impossible investigation state.
- Automated tests cover story-button parity, case progression, and safe repeated actions. CI and emulator verification are still required.


## Development priority reset — 2026-11-12

The player has explicitly directed that complete gameplay/content/UI integration takes priority over APK production and CI polish.

- Ordinary `main` pushes now run source tests only; automatic development APK packaging has been removed from the workflow.
- Unit tests remain a development safety net. They are not the primary deliverable of a feature batch.
- Do not start release packaging until First Contact can be played end-to-end from a fresh save through the chapter debrief and all integrated acceptance criteria in `docs/REBUILD_STATUS.md` pass.
- Feature completion requires a connected player journey: authored content, domain behavior, app navigation, UI feedback, persistence/recovery, and progression. A compile, isolated screen, button, or APK does not satisfy this contract.


## Cross-app RX-14 source inspection

- Messenger attachment previews and Browser Intelligence Reports read the original RX-14 receipt assets from the same VirtualFileSystem paths used by Operations.
- A deliberate confirmation from either Messenger or Browser can record the source as discovered in the shared case-progress record, subject to the active case phase.
- Operations reads the same persisted discovery state; it does not create duplicate evidence records.
- `CasePhaseEvaluator.canDiscoverEvidence` is the shared domain policy for source discovery, including the optional-lead analysis window.
- Domain tests cover phase-aware source discovery. Manual navigation and UI interaction still require integrated acceptance.


## RX-14 chapter reward integration

- The RX-14 reward is granted only after the persisted case reaches COMPLETE (including debrief acknowledgement).
- Reward application uses the existing completed-mission ledger, so repeated calls cannot duplicate XP.
- A startup repair path grants the missing reward to existing completed saves.
- Operations exposes the earned reward and total campaign XP.
- Automated regression coverage checks reward amount, Intelligence competency attribution, ledger entry, and idempotency. Source-test workflow verification is pending.


## RX-14 analytical conclusion and decision rationale

- The case now has deterministic analytical options. Only the conclusion that records the four-second discrepancy while leaving its cause unresolved is supported by the available sources.
- Unsupported causal conclusions remain retryable and do not advance the player into operational decision.
- The player must provide a non-empty rationale (up to 600 characters) with an operational decision; it is persisted in `CaseProgress` and displayed during debrief.
- Save schema 9 adds `analysisConclusionId` and `decisionRationale`; schema-8 case progress decodes with safe defaults and retains existing decision state.
- Tests cover supported/unsupported conclusions, progression gates, retry behavior, and codec migration. Full CI and interactive acceptance are still pending.


## Analysis workspace integration

- The Analysis app now contains the RX-14 case workspace in addition to general evidence review.
- It exposes analysis actions only for discovered records, and exposes analytical conclusions only after all required records have been analyzed.
- Unsupported conclusions remain retryable; supported conclusions unlock the operational decision phase through shared case state.
- Operations routes into Analysis and restores its own context on return through the A.E.G.I.S. hub.
- The Analysis app and Operations consume the same persisted `CaseProgress`; no parallel analysis state is introduced.
- Automated domain coverage exists for the gating rules. CI and manual navigation checks are pending.


## Operational decision draft persistence

- The player's decision rationale is now written into the case save while typing, not only when a decision option is selected.
- Returning to Operations restores the exact draft from CaseProgress; selecting a decision commits the same rationale into the debrief record.
- Draft edits are accepted only during OPERATIONAL_DECISION and remain capped at 600 characters. The case phase does not advance until a decision is explicitly selected.
- Regression coverage checks draft persistence, phase gating, and the storage limit.


## Database and Dossier case-state integration

- Intelligence Database exposes a read-only RX-14 case file view backed by the same GameState and CaseProgress used by the other A.E.G.I.S. apps.
- Personnel Dossier includes case completion, evidence counts, analytical conclusion, operational decision/rationale, debrief state, and reward history.
- No duplicate or screen-local case state is introduced. Source tests and manual navigation verification remain pending.


## Case evidence visibility and investigation integrity

- RX-14 source summaries remain concealed in Analysis, Database, and Browser reports until the corresponding original source is explicitly inspected and recorded.
- The Analysis workspace no longer exposes source content before discovery; its per-source analysis action remains gated by shared CaseProgress.
- Search does not reveal hidden case summaries through matching results. Source metadata remains visible so players can identify where to investigate.
- Browser source preview remains the deliberate discovery path and records inspection only after the player confirms it.
- Added filter regression coverage for concealed source summaries.


## Terminal source inspection integration — restored alongside source visibility guards

- The virtual Terminal participates in RX-14 source inspection. Only a successful `cat` of an approved canonical source path with exact catalog content records the source as inspected.
- The same shared case progression policy gates the discovery update. Failed reads, arbitrary output, and mission-sandbox reads cannot mark evidence discovered.
- The helper is covered by unit tests for valid reads, failed output, and unrelated commands.


## RX-14 narrative-to-workspace navigation

- Story stages 11–14 now route according to the shared case phase instead of always advancing the narrative page.
- Investigation prompts open Operations while required sources remain undiscovered; analysis prompts open the Analysis workspace only after discovery; the decision prompt returns to Operations until debrief completion.
- Once a phase is complete, the story CTA advances to the next authored beat. Completing RX-14 unlocks the final report beat.
- Navigation policy is isolated and unit-tested across investigation, analysis, decision, debrief, and completion states.
