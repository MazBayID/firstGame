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

The baseline repository was a playable vertical slice, not a blank project. The rebuild has since extended several foundations:

- Kotlin + Jetpack Compose Android application.
- Chaquopy Python 3.13 integration.
- A game-managed virtual filesystem with local persistence.
- A Python execution adapter and AST-restricted teaching runner.
- A code editor with basic Python syntax highlighting and editing helpers.
- A campaign/story-stage flow, basic code RUN/SUBMIT actions, and a six-app hub.
- UI icon assets, launcher artwork, and 240 WebP loading frames.
- GitHub Actions for Python unit tests and Android debug APK assembly.

These foundations are not automatically production-ready. Python file access is now routed through mission-scoped virtual workspaces and the general terminal is confined to the personal workspace, but execution still occurs in-process and is not a hardened hostile-code sandbox. The game shell still contains substantial UI state, some App Hub interactions remain read-only, and the approved campaign currently covers the expanded First Contact investigation; a later investigative case remains unapproved.

## Production rebuild workstreams

### 1. Engineering foundation
- Establish clear domain, data, execution, presentation, and platform boundaries.
- Move campaign state and navigation decisions out of the monolithic UI composable.
- Define versioned local persistence, migrations, checkpoint recovery, previous-snapshot recovery, and corruption handling.
- Add deterministic tests for domain rules and storage behavior.

### 2. Runtime and terminal
- Replace prototype-only assumptions with an explicit execution contract and documented capability limits; Python currently receives mission-scoped text-file capabilities, but remains in-process and is not a hardened hostile-code sandbox.
- Connect Python file I/O only through a game-owned, mission-scoped, permission-checked virtual filesystem interface.
- Enforce source/output/resource limits, bounded file writes, cancellation where feasible, and safe error reporting.
- Implement authentic standard command conventions in an allowlisted virtual terminal; never invoke the Android/Linux host shell.
- Test path traversal, malformed input, resource exhaustion, and permission boundaries.

### 3. Complete player experience
- Complete the approved main menu, campaign selection/progress, fourteen-beat First Contact story, contact-list messenger, and App Hub interactions.
- Continue expanding Analysis and Operations into deeper review/operational workflows. Dossier and Intelligence Database now have dedicated data-backed screens; remaining work is broader cross-linking, archive navigation, and audited evidence review.
- Integrate the entry loading animation efficiently; do not decode all frames into memory at once.
- Provide coherent Indonesian-first localization foundations and keep code syntax/standard command names authentic.

### 4. Mission and learning systems
- Build mission definitions, objectives, evidence provenance, workspace permissions, RUN/SUBMIT separation, hybrid evaluation, graduated hints, and debriefs.
- Complete Campaign 01 — THE RECRUITMENT / Chapter 001 — FIRST CONTACT as a cohesive playable narrative; the current authored scope has fourteen beats and eight evaluated missions.
- Ensure each mission provides a fair, level-appropriate code/syntax clue for the next mission, including integer conversion, signed arithmetic, and absolute difference.
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
**Phase 1 — Architecture and persistence foundation:** in progress; versioned snapshots, previous-save recovery, mission contracts, authored story data, an extracted destination model, and a ViewModel-owned campaign state/progression layer exist; broader UI state/navigation extraction and storage recovery tests remain.  
**Phase 2 — Runtime and terminal:** in progress; mission-scoped Python text-file I/O, pure read/write permission tests, personal-workspace terminal confinement, path-policy tests, and source/output/resource guardrails exist; process isolation and stronger hostile-code containment remain.  
**Phase 3 — Complete campaign and operational systems:** in progress; First Contact now has fourteen authored story beats and eight evaluated Python missions, graduated hints, debriefs, contact-list messaging, and data-backed App Hub surfaces; a later case remains unapproved and is not being silently invented.  
**Phase 4 — Progression, archives, and release quality gates:** in progress; XP/rank calculation, story-gated app unlocks, evidence provenance, profile, archives, achievements, and save recovery foundations exist; clearance progression and full release gates remain.

No feature is marked production-complete solely because a prior prototype screen or partial test exists.

## Implementation ledger — current branch

Branch: `rebuild/full-game`  
Draft PR: [#1 — Cyber Protocol full rebuild](https://github.com/MazBayID/firstGame/pull/1)

### Completed in this implementation increment

- Added a data-driven mission definition contract with objective, graduated-hint, workspace, evaluator, reward, and clearance metadata.
- Authored eight evaluated First Contact coding missions as catalog entries; the two timestamp-analysis exercises extend the same unresolved case, while later case concepts remain excluded until approved.
- Integrated the First Contact mission catalog into the opening story UI: authored briefings/objectives and progressive hint reveal now come from mission data rather than duplicated screen text.
- Successful First Contact submissions now use the mission completion use case and award their configured XP exactly once.
- Successful First Contact submissions now create mission-provenanced evidence records; the recruitment ending adds a separate unresolved transmission-timestamp discrepancy as UNVERIFIED evidence.
- Added an evidence lifecycle use case for idempotent recording and explicit status updates.
- Added deterministic rank thresholds for the approved rank ladder and story-gated App Hub unlock rules; legacy story-stage migration now restores the matching app access.
- Added a previous-good-snapshot backup and corrupt-snapshot preservation path for local campaign save recovery.
- Replaced the App Hub's initial placeholders with data-backed Database, Dossier, Analysis, and Operations views; added Profile, Archives, Achievements, and a persistent reduced-motion setting to the main menu.
- Implemented a contact-list Messenger with separate Freya and NEXUS conversations and locally persisted reply history.
- Redesigned Secure Messenger toward a familiar smartphone chat experience with searchable conversations, contact previews, distinct incoming/outgoing bubbles, and predefined quick replies.
- Added the first-entry Campaign notification sequence: black screen, ordinary-looking in-game Pesan Aman notification from A.E.G.I.S. / Recruitment Office, and explicit tap-through to the official organization's invitation conversation.
- Added persisted recruitment, Freya-introduction, and NEXUS-unlock checkpoints in the versioned GameState; the current schema is 5. The notification taps are idempotent, and the schema-4 completed-chapter migration reopens the two newly added timestamp missions without replaying the opening.
- Removed the Unknown Sender prologue from the playable First Contact story sequence. The official invitation is authored by A.E.G.I.S.; its interactive mission link opens the orientation sequence. Freya's separate notification appears when the first mission becomes active, and NEXUS remains hidden until the player taps Freya's in-chat link.
- Rewrote the Story_Line opening sequence to match the approved notification → Secure Messenger → recruitment invitation flow.
- Reworked the Campaign overview into a chapter-focused hero panel with a fourteen-beat progression timeline.
- Added distinct cinematic gradient treatments and scene identifiers to First Contact story cards, with different visual palettes for organization, dossier, training, and recruitment scenes.
- Added a mission-success debrief screen before the story advances, with authored forward clues for each of the eight missions.
- Added two final First Contact technical missions: timestamp-string conversion with `int()` and signed subtraction, followed by `abs()`-based magnitude validation across multiple fixtures. The final report now follows these missions and still does not infer a cause for the four-second discrepancy.
- Extended the chapter's achievement archive with dedicated completion awards for timestamp normalization and absolute-delta validation.
- Corrected mentor introduction continuity: the official A.E.G.I.S. invitation is separate from Freya's notification; Freya is attributed as mentor only after her notification is opened, and NEXUS remains gated behind her in-chat link.

- Extracted First Contact narrative content and game destination definitions from the main Compose shell.
- Added a `GameSessionViewModel` to own campaign state refreshes, mission completion, code checkpoints, recruitment clearance, and progression/evidence mutations outside the main Compose screen.
- Added end-to-end domain coverage for First Contact progression and ViewModel-level checkpoint tests.
- Versioned evidence provenance timestamps and added a save-schema upgrade path for earlier snapshots.
- Added pure mission-workspace permission tests and aligned the code editor's source limit with the Python runner.
- Added a pure virtual path policy and regression tests for traversal, root access, and mission-directory denial from the personal terminal.
- Added Python static limits for literal text, integer size, and string/list repetition to reduce accidental memory exhaustion. These remain teaching-runtime guardrails, not security isolation.
- Added mission workspace handles with per-mission file allowlists, explicit writable-file sets, bounded text writes, and seeded read-only briefings.
- Connected Python RUN to mission workspaces through bounded `read_text()` / `write_text()` capabilities; changed files return as structured data and persist only through Kotlin permission checks.
- Confined the general-purpose in-game terminal to the personal virtual workspace so path navigation cannot browse mission directories or the virtual root.
- Added Python regression cases for allowlisted reads, denied reads, writable-file enforcement, and file-size limits.
- Added `GameState`, evidence models, and a repository interface independent of Android storage APIs.
- Added versioned local campaign-state persistence, including migration of the previous story-stage preference.
- Added checkpoint and idempotent mission-completion use cases with clearance and XP rules.
- Connected the opening campaign's fourteen story checkpoints and explicit code saves to the versioned state store.
- Routed all eight First Contact code submissions through the Python mission evaluator; RUN remains execution-only.
- Added multi-fixture mission evaluator coverage for all eight First Contact tasks, including reversed timestamp ordering and signed/absolute delta checks.
- Added Python runner limits for AST size, range iteration, integer literals, collection literals, and captured stdout. These are teaching-runtime guardrails, **not a hardened sandbox**.
- Added the entry-only WebP loading animation. It decodes one frame at a time and releases the previous bitmap instead of loading all 240 frames into memory.
- Added Python regression tests and Kotlin domain-use-case tests.
- Updated CI configuration to run Android unit tests before APK assembly.


- Tightened the Freya notification checkpoint: the introduction can only be acknowledged while the first mission is active; added regression coverage for premature access and the required mission gate.
- Expanded Freya's persistent Messenger guidance with the current active mission and verified-mission count, so her mentoring context reflects the player's actual campaign progress instead of repeating a static greeting.

- Added authored, choice-specific Freya and NEXUS responses. Freya's guidance reflects the active mission and verified-mission count; her response to the recruitment question preserves the hidden identity of the recruiter. NEXUS reports only local campaign facts and offline capability.

- Extracted Freya and NEXUS authored replies into a deterministic dialogue model with unit tests for recruitment-mystery continuity, beginner-support tone, live mission status, and offline-first messaging.

- Replaced the read-only Operations summary with a mission-control screen showing verified mission count, campaign XP/rank/clearance, each mission's current state, and a contextual resume action for the currently active mission.
- Replaced the static Analysis text dump with a searchable evidence workspace: status filters, case-insensitive search across title/summary/source/mission, newest-first ordering, and source/provenance cards. The view is intentionally read-only; evidence classification cannot be changed without an audited review workflow.
- Rebuilt the Intelligence Database into two linked indexes: authored mission files and persisted evidence records. Both are searchable; mission detail shows objectives/workspace metadata, while evidence detail preserves status, source, mission provenance, timestamp, and the related mission briefing.
- Replaced the text-only Personnel Dossier with a data-backed record showing recruitment/contact status, mentor and NEXUS access, campaign qualification, clearance, verified mission history, evidence count, and XP progress toward the next rank. The rank threshold calculation is now exposed by the progression domain rule and covered by tests.
- Added unit tests for evidence ordering, status isolation, combined search/filter behavior, and provenance-field search.
- Added one canonical mission-to-story-checkpoint mapping in the campaign catalog and regression coverage for all eight mission destinations. Operations resumes through the existing story gate rather than independently unlocking or starting missions.


### Verification observed so far
- CI run `43028166224` passed on code commit `83d983e`: Python runner tests, Kotlin unit tests, Android debug APK assembly, and artifact upload all succeeded. This verifies the persisted recruitment-opening state and removal of the old prologue.
- CI run `43028768493` passed on commit `73493ac`: the Secure Messenger inbox/chat redesign compiled, Kotlin unit tests passed, and a debug APK artifact was uploaded.
- CI run `43028842754` passed on commit `58ec024`: the Messenger return-navigation fix also compiled and passed Kotlin/Python tests.
- CI run `43029424840` passed on commit `01d49dd`: the new Campaign chapter overview and five-beat progress timeline compiled and passed the automated checks.
- CI run `43029676168` passed on commit `ac076ca`: Python runner tests, Kotlin unit tests, Android debug APK assembly, and artifact upload all succeeded, including the cinematic story-card styling.
- These CI results are automated build/test evidence, not a substitute for final physical-device acceptance.

### Remaining before this can be called a complete game
- Finish extracting navigation and remaining UI state from the main Compose shell; extend ViewModel ownership to terminal and application navigation state. Operations now has a dedicated mission-control surface, while broader hub and terminal state extraction remains.
- Extend mission workspace permission tests to the Android persistence adapter and integrate workspace-scoped terminal sessions for operations. The Operations control screen can resume the story-authorized active mission; full terminal-session integration remains.
- Harden the execution boundary beyond AST allowlisting; the current in-process Python runner must not be described as secure against hostile code.
- Expand the current data-backed Database and Dossier surfaces into fully interactive, story-linked workspaces. Analysis now supports evidence search/filter and Operations now supports active-mission resume; additional review workflows and data navigation remain.
- Complete the campaign's investigative operation only after its proposed case concept is approved; expand hybrid evaluation and evidence-backed reasoning beyond the current opening missions.
- Add automated tests for previous-snapshot recovery, save-schema migration, and archive retention; expand clearance progression.
- Finish ViewModel-driven state ownership and remaining App Hub interactions; implement complete Indonesian/English localization and the cinematic Campaign presentation overhaul; add audio/control settings when their runtime systems exist.
- Run the full unit, integration, build, and release checks; then perform final physical-device acceptance.

The branch is deliberately kept as a draft. Phases 1–5 are being developed as one integrated rebuild, but are not yet complete and must not be represented as release-ready.
