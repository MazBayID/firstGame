# Cyber Protocol — Implementation Audit

**Audit baseline:** `main` at `3c89476190461737c51ffdc1044a9b219109fe38`  
**Audit date:** 2026-11-12  
**Scope:** canonical design, story continuity, Android/Kotlin source, Python runner, local persistence, VFS, app surfaces, tests, and CI workflow.  
**Purpose:** identify what is genuinely implemented, what is partial, and what must be built next. This is a source review; it is not a physical-device acceptance report.

## Executive result

Cyber Protocol is a substantial playable foundation, not a blank project. The current branch already contains the approved First Contact opening, eight evaluated Python missions, a local virtual filesystem, mission-scoped Python file access, campaign persistence, a seven-app A.E.G.I.S. hub, and several data-backed evidence views.

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
