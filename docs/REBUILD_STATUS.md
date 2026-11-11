# Cyber Protocol — Full Rebuild Status

**Development direction:** Full Rebuild (Option A)  
**Canonical product reference:** `GAME_CONCEPT.md`  
**Canonical narrative reference:** `Story_Line.md`  
**Product target:** Complete Android-first story RPG and controlled intelligence simulator—not a disposable prototype.

## Rebuild contract

- Treat `GAME_CONCEPT.md` as the default and authoritative product specification. Where implementation details are open, choose the simplest robust design that preserves its player experience.
- Preserve `GAME_CONCEPT.md`, `README.md`, `Story_Line.md`, and all user-supplied assets.
- Audit before replacement. Reuse existing components only after checking correctness, integration boundaries, and testability.
- Build coherent, integrated systems. A screen or menu item is not a completed feature unless its behavior, persistence, and connections to the game loop work.
- Keep the core campaign offline-first. Do not make network access, account sign-in, or an external AI API a prerequisite.
- Maintain strict separation between game-managed virtual data and the Android host environment.
- Keep RUN (execution) separate from SUBMIT (mission evaluation).
- Prefer a smaller complete and dependable first campaign over broad but nonfunctional feature coverage.
- Do not claim production readiness, perfect behavior, or device validation based solely on a successful CI build.

## Repository audit — baseline

The current repository is a playable vertical slice, not a blank project. It already contains reusable foundations:

- Kotlin + Jetpack Compose Android application.
- Chaquopy Python 3.13 integration.
- A game-managed virtual filesystem with local persistence.
- A Python execution adapter and AST-restricted teaching runner.
- A code editor with basic Python syntax highlighting and editing helpers.
- A campaign/story-stage flow, basic code RUN/SUBMIT actions, and a six-app hub.
- UI icon assets, launcher artwork, and 240 WebP loading frames.
- GitHub Actions for Python unit tests and Android debug APK assembly.

These foundations are not automatically production-ready. In particular, the current Python runner explicitly describes itself as a prototype guardrail, Python file access is not integrated with the virtual filesystem, the app state/navigation is concentrated in a large Compose file, several App Hub surfaces are placeholders, and the campaign is only an opening slice.

## Production rebuild workstreams

### 1. Engineering foundation
- Establish clear domain, data, execution, presentation, and platform boundaries.
- Move campaign state and navigation decisions out of the monolithic UI composable.
- Define versioned local persistence, migrations, checkpoint recovery, and corruption handling.
- Add deterministic tests for domain rules and storage behavior.

### 2. Runtime and terminal
- Replace prototype-only assumptions with an explicit execution contract and documented capability limits.
- Connect Python file I/O only through a game-owned, permission-checked virtual filesystem interface.
- Enforce source/output/resource limits, cancellation where feasible, and safe error reporting.
- Implement authentic standard command conventions in an allowlisted virtual terminal; never invoke the Android/Linux host shell.
- Test path traversal, malformed input, resource exhaustion, and permission boundaries.

### 3. Complete player experience
- Implement the approved main menu, campaign selection/progress, first-contact story, contact-list messenger, and App Hub.
- Implement real Dossier, Intelligence Database, Analysis, and Operations data flows rather than placeholder surfaces.
- Integrate the entry loading animation efficiently; do not decode all frames into memory at once.
- Provide coherent Indonesian-first localization foundations and keep code syntax/standard command names authentic.

### 4. Mission and learning systems
- Build mission definitions, objectives, evidence provenance, workspace permissions, RUN/SUBMIT separation, hybrid evaluation, graduated hints, and debriefs.
- Complete Campaign 01 — THE RECRUITMENT / Chapter 001 — FIRST CONTACT as a cohesive playable narrative.
- Ensure each mission provides a fair, level-appropriate code/syntax clue for the next mission.
- Introduce SQL, networking, C++, and IoT progressively; Python is the first complete technical track.

### 5. Progression and persistence
- Implement rank, XP, clearance, unlock rules, archives, personal workspace, mission workspaces, explicit code saving, and meaningful autosave checkpoints.
- Preserve evidence provenance and mission state across interruption and restart.
- Keep initial dialogue choices cosmetic as specified; do not invent relationship meters or branching consequences.

### 6. Quality and release readiness
- Unit-test domain logic, evaluators, command parsing, virtual filesystem, save migrations, and runtime boundaries.
- Add integration tests for campaign transitions, persistence, mission completion, and App Hub navigation.
- Build debug and release variants in CI; verify manifest, package, ABI, and dependency configuration.
- Run static checks and dependency/security review where supported.
- Validate layouts and keyboard behavior on representative Android screen sizes.
- Produce a release candidate only after the complete campaign path and core systems pass the defined quality gates.

## Acceptance gates

A workstream is not complete merely because its code compiles. It must have:
1. A documented behavior contract.
2. Automated tests for its critical logic and failure cases.
3. Integration with the relevant game flow.
4. Persistence and recovery behavior where applicable.
5. No known critical crash, data-loss, permission-bypass, or progression-blocking defect.
6. A successful CI build.

Physical-device acceptance remains a separate final validation step. A CI build cannot replace it.

## Current status

**Phase 0 — Audit and specification alignment:** baseline audit completed; implementation continues to refine the boundaries.  
**Phase 1 — Architecture and persistence foundation:** in progress; versioned state and domain mission contracts exist, while navigation/state extraction and recovery tests remain.  
**Phase 2 — Runtime and terminal:** in progress; virtual terminal and Python adapter exist, but mission-scoped I/O and stronger execution isolation remain.  
**Phase 3 — Complete campaign and operational systems:** in progress; First Contact has authored mission data and integrated objectives/hints/rewards; the remaining campaign and App Hub systems are unfinished.  
**Phase 4 — Progression, archives, and release quality gates:** in progress at the foundation level; XP idempotency exists, while rank/clearance/unlocks/archives and full release gates remain.

No feature is marked production-complete solely because a prior prototype screen or partial test exists.

## Implementation ledger — current branch

Branch: `rebuild/full-game`  
Draft PR: [#1 — Cyber Protocol full rebuild](https://github.com/MazBayID/firstGame/pull/1)

### Completed in this implementation increment

- Added a data-driven mission definition contract with objective, graduated-hint, workspace, evaluator, reward, and clearance metadata.
- Authored the two approved First Contact coding missions as catalog entries; later case concepts remain excluded until approved.
- Integrated the First Contact mission catalog into the opening story UI: authored briefings/objectives and progressive hint reveal now come from mission data rather than duplicated screen text.
- Successful First Contact submissions now use the mission completion use case and award their configured XP exactly once.
- Added `GameState`, evidence models, and a repository interface independent of Android storage APIs.
- Added versioned local campaign-state persistence, including migration of the previous story-stage preference.
- Added checkpoint and idempotent mission-completion use cases with clearance and XP rules.
- Connected the opening campaign's story checkpoints and explicit code saves to the versioned state store.
- Routed the two First Contact code submissions through the Python mission evaluator; RUN remains execution-only.
- Added mission evaluator fixtures for both First Contact code stages.
- Added Python runner limits for AST size, range iteration, integer literals, collection literals, and captured stdout. These are teaching-runtime guardrails, **not a hardened sandbox**.
- Added the entry-only WebP loading animation. It decodes one frame at a time and releases the previous bitmap instead of loading all 240 frames into memory.
- Added Python regression tests and Kotlin domain-use-case tests.
- Updated CI configuration to run Android unit tests before APK assembly.

### Verification observed so far
- Python runner suite passed on the latest observed run.
- Android debug APK assembly passed on a commit containing the entry loading integration.
- A prior failure was traced to a missing loading composable during an intermediate commit; the subsequent commit containing that composable built successfully.
- The latest state-store and UI cleanup commits still require their own completed CI result.
- Kotlin unit tests have been added; confirmation that CI executes them is pending.

### Remaining before this can be called a complete game
- Finish modularizing navigation, UI state, campaign/story content, and data ownership.
- Implement the mission-scoped permission model and a controlled Python-to-virtual-filesystem bridge.
- Harden the execution boundary beyond AST allowlisting; the current in-process Python runner must not be described as secure against hostile code.
- Replace the remaining placeholder Database, Dossier, Analysis, and Operations surfaces with working, story-linked systems.
- Implement complete campaign operations, evidence lifecycle/provenance, hybrid evaluation, hints, debriefs, and progression unlocks.
- Complete checkpoint recovery, save migration/recovery behavior, and archive retention.
- Complete the App Hub, messenger history, and the remaining player-facing systems.
- Run the full unit, integration, build, and release checks; then perform final physical-device acceptance.

The branch is deliberately kept as a draft. Phases 1–5 are being developed as one integrated rebuild, but are not yet complete and must not be represented as release-ready.
