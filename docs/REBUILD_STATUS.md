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
- A campaign/story-stage flow, code RUN/SUBMIT actions, and a seven-app hub including the offline A.E.G.I.S. Browser.
- UI icon assets, launcher artwork, and 240 WebP loading frames.
- GitHub Actions with source validation followed by a clearly labeled development Debug APK on pushes to `main` (no emulator smoke test). A manually approved Final Release on `main` builds one permanently signed Release APK, installs and smoke-tests that exact binary on an Android Emulator, then publishes the unchanged APK with an explicit FINAL — EMULATOR SMOKE-TESTED label.

These foundations are not automatically production-ready. Python file access is now routed through mission-scoped virtual workspaces and the general terminal is confined to the personal workspace, but execution still occurs in-process and is not a hardened hostile-code sandbox. The game shell still contains substantial UI state, some App Hub interactions remain read-only, and the approved campaign currently covers the expanded First Contact investigation; a later investigative case remains unapproved.

## Production rebuild workstreams

### 1. Engineering foundation
- Establish clear domain, data, execution, presentation, and platform boundaries.
- Move campaign state and navigation decisions out of the monolithic UI composable.
- Define versioned local persistence, migrations, checkpoint recovery, previous-snapshot recovery, and corruption handling.
- Add deterministic tests for domain rules and storage behavior.

### 2. Runtime and terminal
- Replace prototype-only assumptions with an explicit execution contract and documented capability limits; Python currently receives mission-scoped text-file capabilities and source/output/AST/loop/repetition/file-size limits plus a three-second wall-clock budget, but remains in-process and is not a hardened hostile-code sandbox.
- Connect Python file I/O only through a game-owned, mission-scoped, permission-checked virtual filesystem interface.
- Enforce source/output/resource limits, bounded file writes, cancellation where feasible, and safe error reporting.
- Implement authentic standard command conventions in an allowlisted virtual terminal; never invoke the Android/Linux host shell.
- Test path traversal, malformed input, resource exhaustion, and permission boundaries.

### 3. Complete player experience
- Keep the A.E.G.I.S. App Hub as a set of connected tools, not a mandatory checklist; the mission design decides which apps are relevant.
- Complete the approved main menu, campaign selection/progress, fourteen-beat First Contact story, contact-list Messenger, offline A.E.G.I.S. Browser, and remaining App Hub interactions.
- Continue expanding Analysis and Operations into deeper review/operational workflows. Dossier and Intelligence Database now have dedicated data-backed screens; remaining work is broader cross-linking, archive navigation, and audited evidence review.
- Integrate the entry loading animation efficiently; do not decode all frames into memory at once.
- Implement the approved English-first, Indonesian-supported localization system: English default on fresh installs, persistent in-game language choice, stable message keys, localized story/mission data, and full content parity. Keep code syntax, identifiers, standard command names, file paths, and evaluator-sensitive output authentic.

### 4. Mission and learning systems
- Use the approved **Guided Intelligence RPG** approach: a case/problem is the gameplay unit, not a mandatory sequence of application visits.
- Build the five-phase loop—Intelligence Briefing, Investigation, Intelligence Analysis, Operational Decision, and Debriefing & Progression—as flexible experience phases, not five compulsory screens.
- Author each mission with one primary archetype (Narrative, Investigation, Technical, Analytical, or Operational), optional secondary activities, clear objectives, discoverable information, and fair completion criteria.
- Develop the Evidence & Investigation Engine as the cross-app source of truth for evidence identity, content/reference, provenance, uncertainty/status, related findings, and audited review actions. Define the schema and allowed status transitions before broad implementation.
- Expand deterministic hybrid evaluation to distinguish technical execution, evidence discovery, analytical reasoning, and operational decisions. Preserve multiple defensible conclusions where the authored evidence supports them; never require hidden criteria.
- Preserve RUN/SUBMIT separation, mission workspace permissions, graduated hints, and learning-oriented retry behavior.
- Preserve Campaign 01 — THE RECRUITMENT / Chapter 001 — FIRST CONTACT as the guided beginner chapter and preserve its approved narrative, characters, fourteen story beats, and unresolved RX-14 evidence premise. The previous requirement to preserve eight evaluated Python missions as a fixed linear sequence is superseded: redesign, merge, make optional, or retire legacy mission mechanics wherever they conflict with the Guided Intelligence RPG. Keep beginner Python teaching progressive, but do not force code into non-technical case phases.
- Use code/syntax clues when they naturally foreshadow the next technical concept; do not force them into every mission archetype.
- Introduce SQL, networking, C++, and IoT progressively when campaign problems justify them; Python is the first technical track.

### 5. Progression and persistence
- Implement rank, XP, clearance, unlock rules, archives, personal workspace, mission workspaces, explicit code saving, and meaningful autosave checkpoints.
- Preserve evidence provenance and mission state across interruption and restart.
- Keep initial dialogue choices cosmetic as specified; do not invent relationship meters or branching consequences.

### 6. Quality and release readiness
- Unit-test domain logic, evaluators, command parsing, virtual filesystem, save migrations, and runtime boundaries.
- Add integration tests for campaign transitions, persistence, mission completion, and App Hub navigation.
- Run Python and Release-variant Android/Kotlin unit tests on pushes to `main` and pull requests. A push to `main` publishes a clearly labeled development Debug APK after tests pass; pull requests remain source-tests-only and receive no release-signing secrets. A manual dispatch with `final_release=false` also publishes a development APK without emulator smoke testing. Only an explicit `workflow_dispatch` on `main` with `final_release=true` may produce a permanently signed Final APK; validate package, ABI, monotonically increasing versionCode, and signature, then install and launch that exact APK on the emulator, verify its SHA-256 is unchanged, and publish it as **FINAL — EMULATOR SMOKE-TESTED**.
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

**Phase 0 — Audit and specification alignment:** repository-wide implementation audit recorded in `docs/IMPLEMENTATION_AUDIT.md` against the current `main` baseline (`3c89476`). The audit identifies existing partial foundations, specification gaps, and the ordered implementation batches; follow-up code changes must update this ledger.  
**Phase 1 — Architecture and persistence foundation:** in progress; versioned snapshots, previous-save recovery, mission contracts, authored story data, an extracted destination model, a ViewModel-owned campaign state/progression layer, and isolated snapshot serialization with codec regression tests exist; broader UI state/navigation extraction and Android storage recovery tests remain.  
**Phase 2 — Runtime and terminal:** in progress; mission-scoped Python text-file I/O, pure read/write permission tests, personal-workspace terminal confinement, path-policy tests, and source/output/resource guardrails exist; process isolation and stronger hostile-code containment remain.  
**Phase 3 — Complete campaign and operational systems:** in progress; First Contact now has fourteen authored story beats and eight evaluated Python missions, graduated hints, debriefs, contact-list messaging, an offline intranet Browser with file downloads and Mission Control submission, and data-backed App Hub surfaces; a later case remains unapproved and is not being silently invented.  
**Phase 4 — Progression, archives, and release quality gates:** in progress; XP/rank calculation, story-gated app unlocks, evidence provenance, profile, archives, achievements, and save recovery foundations exist; clearance progression and full release gates remain.

No feature is marked production-complete solely because a prior prototype screen or partial test exists. The implementation audit is a point-in-time assessment of the current source tree, not a claim that the listed workstreams are complete.

## Latest implementation audit

See [IMPLEMENTATION_AUDIT.md](IMPLEMENTATION_AUDIT.md) for the source-by-source gap analysis against the approved Guided Intelligence RPG and English-first localization direction. The audit baseline is `3c89476`. It confirms existing evidence/persistence foundations while identifying the missing structured provenance, immutable review history, case/archetype model, competency tracks, and complete localization.

The audit is a point-in-time source review. The implementation ledger below records previously completed work and must be updated as each new batch is implemented.

## Implementation ledger — current branch

Branch: `main`  
Integration PR: [#1 — Cyber Protocol full rebuild](https://github.com/MazBayID/firstGame/pull/1) — merged.

### Completed in this implementation increment

- Added a data-driven mission definition contract with objective, graduated-hint, workspace, evaluator, reward, and clearance metadata.
- The original First Contact catalog contains eight evaluated coding missions. These are legacy mechanics under redesign, not a mandatory linear gameplay contract. Their learning concepts may be retained as Freya-led training or optional practice; the RX-14 case should require code only where it genuinely serves the investigation.
- Integrated the First Contact mission catalog into the opening story UI: authored briefings/objectives and progressive hint reveal now come from mission data rather than duplicated screen text.
- Removed bundled executable answers from all eight First Contact mission starters; starter files now contain objective-only TODO comments, and exact legacy bundled solutions are cleared on load while custom player drafts remain intact.
- Added a mission practice gate: SUBMIT is blocked until the exact current editor source has completed RUN without errors; editing code invalidates that run result, and edits made during execution/verification cannot advance the campaign.
- Successful First Contact submissions now use the mission completion use case and award their configured XP exactly once.
- Successful First Contact submissions now create mission-provenanced evidence records; the recruitment ending adds a separate unresolved transmission-timestamp discrepancy as UNVERIFIED evidence.
- Added an evidence lifecycle use case for idempotent recording and explicit status updates.
- Added deterministic rank thresholds for the approved rank ladder and story-gated App Hub unlock rules; legacy story-stage migration now restores the matching app access.
- Added a previous-good-snapshot backup and corrupt-snapshot preservation path for local campaign save recovery.
- Extracted the versioned GameState JSON codec from Android storage and added regression tests for complete state/evidence round trips, legacy defaults, unknown evidence statuses, and unsupported future schemas.
- Replaced the App Hub's initial placeholders with data-backed Database, Dossier, Analysis, and Operations views; added Profile, Archives, Achievements, and a persistent reduced-motion setting to the main menu.
- Implemented a contact-list Messenger with separate Freya and NEXUS conversations and locally persisted reply history.
- Added the offline A.E.G.I.S. Browser with locally validated fictional credentials, persistent login/navigation state, Mission Control, File Repository, Intelligence Reports, Operations Archive, and Agent Profile.
- Connected Freya's Messenger intranet link and credentials to the Browser; added the first mission's shared `message_fragment.txt` attachment.
- Added authored read-only mission file assets and a permission-checked VFS download capability. The Messenger attachment and Browser File Repository install the same underlying asset into the active mission workspace.
- Corrected the first mission's missing-input defect: the `ACCESS GRANTED` fragment is now provided as `message_fragment.txt`, and the briefing/hints explain how to download and read it with `cat`.
- Moved mission SUBMIT to Browser Mission Control. The Browser sends the player's actual editor source to the evaluator and only enables submission after that exact source completes a successful RUN.
- Routed Code Editor RUN through the mission's writable script and the dedicated Terminal screen using the normal `python <script>.py` command path; terminal return navigation preserves the editor context.
- Added the Browser unlock at the first active mission and backfilled it into existing saves without removing previously unlocked apps.

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
- Established the single-APK Final Release policy: development pushes and pull requests run source tests only; a manually approved Final Release on `main` builds one permanently signed Release APK, tests that exact file on an Android Emulator, verifies its SHA-256 remains unchanged, and publishes that same file.


- Tightened the Freya notification checkpoint: the introduction can only be acknowledged while the first mission is active; added regression coverage for premature access and the required mission gate.
- Expanded Freya's persistent Messenger guidance with the current active mission and verified-mission count, so her mentoring context reflects the player's actual campaign progress instead of repeating a static greeting.

- Added authored, choice-specific Freya and NEXUS responses. Freya's guidance reflects the active mission and verified-mission count; her response to the recruitment question preserves the hidden identity of the recruiter. NEXUS reports only local campaign facts and offline capability.

- Extracted Freya and NEXUS authored replies into a deterministic dialogue model with unit tests for recruitment-mystery continuity, beginner-support tone, live mission status, and offline-first messaging.

- Replaced the read-only Operations summary with a mission-control screen showing verified mission count, campaign XP/rank/clearance, each mission's current state, and a contextual resume action for the currently active mission.
- Replaced the static Analysis text dump with a searchable evidence workspace: status filters, case-insensitive search across title/summary/source/mission, newest-first ordering, and source/provenance cards. The view is intentionally read-only; evidence classification cannot be changed without an audited review workflow.
- Rebuilt the Intelligence Database into two linked indexes: authored mission files and persisted evidence records. Both are searchable; mission detail shows objectives/workspace metadata, while evidence detail preserves status, source, mission provenance, timestamp, and the related mission briefing.
- Replaced the text-only Personnel Dossier with a data-backed record showing recruitment/contact status, mentor and NEXUS access, campaign qualification, clearance, verified mission history, evidence count, and XP progress toward the next rank. The rank threshold calculation is now exposed by the progression domain rule and covered by tests.
- Added unit tests for evidence ordering, status isolation, combined search/filter behavior, and provenance-field search.
- Extracted terminal command, current-directory, output-buffer, and execution-busy state into a saveable `TerminalSessionState`; terminal output remains bounded to 24,000 characters, and an interrupted in-flight command restores as idle. Added unit tests for output bounds, clear behavior, workspace-state preservation, and save/restore behavior.
- Grouped A.E.G.I.S. Browser authentication, current page, and return destination into a saveable `BrowserSessionState`; authentication remains persisted through the existing local preference store. Added regression tests for initial login/home routing, Messenger return context, and session restoration.
- Grouped Secure Messenger contact selection, return destination, and Freya/NEXUS reply histories into a saveable `MessengerSessionState`; preserved the existing SharedPreferences keys, separator, and 50-reply history limit. Added tests for contact separation, reply bounds, unsupported contacts, and Saver restoration.
- Added automatic scroll-to-latest behavior in Secure Messenger whenever a conversation is opened or a new reply is appended, and in Terminal whenever command output changes. Both use Compose scroll state and animate to the latest content after layout.
- Added one canonical mission-to-story-checkpoint mapping in the campaign catalog and regression coverage for all eight mission destinations. Operations resumes through the existing story gate rather than independently unlocking or starting missions.


- The run passed Python runner tests and Release-variant Android unit tests, restored and validated the permanent signing keystore, built one signed Release APK, and passed package/version/ABI/signature validation. It is now classified historically as an emulator-smoke-tested Final APK under the former artifact naming convention. The Android Emulator installed and launched that exact APK; the post-test SHA-256 comparison passed; the unchanged APK and checksum manifest were published; and temporary signing material was removed.
- This automated verification does **not** replace physical-device acceptance: clean install, in-place update, and save-data preservation still need testing on the user's Android device.

### Source Validation and Explicit Final Release pipeline

- Pushes to `main` run Python and Release-variant Android/Kotlin unit tests, then build and publish a Debug development APK. The artifact and its manifest must say **DEVELOPMENT — NOT FINAL — SMOKE TEST NOT RUN**.
- Pull-request checks remain source-only: no installable APK and no release-signing secrets.
- A manual dispatch with `final_release=false` runs source tests and publishes the same clearly labeled development APK without emulator smoke testing.
- Only a manual dispatch from `main` with `final_release=true` may create the permanently signed Final APK. The workflow reruns source tests, builds exactly one Release APK, validates package/version/ABI/signature, installs and launches that exact binary on the Android Emulator, verifies its SHA-256 is unchanged, and publishes the same file as **FINAL — EMULATOR SMOKE-TESTED**.
- A skipped, failed, cancelled, or incomplete final check blocks Final publication. Never rebuild or re-sign the APK after smoke testing.
- The permanent keystore is restored only inside the ephemeral Final Release runner, protected by GitHub Actions Secrets, and removed after the job.
- Emulator smoke testing does not replace physical-device acceptance. Clean installation, in-place update, and save-data preservation must still be confirmed on the user's Android device.

### Current pipeline verification

- Latest development workflow: [CI run #124](https://github.com/MazBayID/firstGame/actions/runs/43152125920), source commit `3c89476190461737c51ffdc1044a9b219109fe38`.
- Python runner tests and Release-variant Android/Kotlin unit tests passed. The Debug development APK build and artifact-upload steps also succeeded. At the last check, the job was completing post-action cleanup; the development artifact was already available.
- Artifact: `cyber-protocol-development-not-final-no-smoke-test`, ID `13914700984`, archive size 58,748,863 bytes, SHA-256 digest `c477e3a72dc5d1fea80411029b950547028cea25a027b5fa47590518538e0658`, expiry 2026-11-26.
- This is a development APK only. It has no emulator smoke-test result and must not be described as a Final APK.
- Latest historical emulator-smoke-tested APK before the new gameplay/localization direction remains CI run [#119](https://github.com/MazBayID/firstGame/actions/runs/43144903075), source commit `c3027dc0e54d0785f6ed80eae631274afee9f60b`, artifact ID `13912230989`. It predates the Guided Intelligence RPG and English-first documentation changes.
- Physical-device acceptance remains outstanding: clean install, in-place update, and save-data preservation on the user's Android device.

### Historical verification observed under the former CI design
The following older runs predate the current test-only / signed-Release-only workflow separation. Their Debug APK artifacts are historical and must not be used as device-test or distribution builds.
- CI run `43028166224` passed on code commit `83d983e`: Python runner tests, Kotlin unit tests, Android debug APK assembly, and artifact upload all succeeded. This verifies the persisted recruitment-opening state and removal of the old prologue.
- CI run `43028768493` passed on commit `73493ac`: the Secure Messenger inbox/chat redesign compiled, Kotlin unit tests passed, and a debug APK artifact was uploaded.
- CI run `43028842754` passed on commit `58ec024`: the Messenger return-navigation fix also compiled and passed Kotlin/Python tests.
- CI run `43029424840` passed on commit `01d49dd`: the new Campaign chapter overview and five-beat progress timeline compiled and passed the automated checks.
- CI run `43029676168` passed on commit `ac076ca`: Python runner tests, Kotlin unit tests, Android debug APK assembly, and artifact upload all succeeded, including the cinematic story-card styling.
- These CI results are automated build/test evidence, not a substitute for final physical-device acceptance.

### Remaining before this can be called a complete game
- Finish extracting navigation and remaining UI state from the main Compose shell; extend ViewModel ownership to terminal and application navigation state. Operations now has a dedicated mission-control surface, while broader hub and terminal state extraction remains.
- Mission workspace permissions now have Robolectric coverage for read-only asset downloads, persistence across VFS instances, Terminal cat access, write denial, and path-traversal denial. Complete Operations-specific terminal-session integration.
- Harden the execution boundary beyond AST allowlisting; the current in-process Python runner must not be described as secure against hostile code.
- Expand the current data-backed Database and Dossier surfaces into fully interactive, story-linked workspaces. Analysis now supports evidence search/filter and Operations now supports active-mission resume; additional review workflows and data navigation remain.
- Complete the Guided Intelligence RPG conversion beyond the RX-14 vertical slice: cross-app source inspection, richer analytical reasoning and decision rationale, optional-practice access for retired Python concepts, and the next story case only after separate approval. The case engine and first RX-14 case foundation are implemented; the full campaign conversion is not complete.
- Save-snapshot round trips, previous-good-snapshot recovery, corrupt-snapshot preservation, legacy story migration, and previous-schema migration now have Robolectric coverage. Add archive-retention tests and expand clearance progression.
- Finish ViewModel-driven state ownership and remaining App Hub interactions; implement complete Indonesian/English localization and the cinematic Campaign presentation overhaul; add audio/control settings when their runtime systems exist.
- Run the full unit, integration, build, and release checks; then perform final physical-device acceptance.

The repository's long-lived development and release branch is `main`. The game remains a development build: the integrated rebuild is not complete and must not be represented as final-game release-ready.

## Latest gameplay direction — supersedes the eight-mission preservation rule

**Player decision, 2026-11-12:** the current gameplay may be changed when it conflicts with the latest Guided Intelligence RPG plan. The previous constraint to preserve all eight evaluated Python missions as fixed, mandatory mechanics is removed.

Implementation requirements:
- Preserve First Contact's approved narrative and beginner-onboarding purpose, not obsolete mission mechanics.
- Make the RX-14 discrepancy a coherent evidence-backed case with flexible briefing, investigation, analysis, decision, and debrief phases.
- Treat the eight old Python concepts as curriculum material. Keep only the technical steps that naturally serve the case; move the rest to optional practice/learning or redesign them.
- Do not force visits to every app. The player chooses relevant tools to solve the problem.
- Allow a qualified 'insufficient evidence' outcome when the sources do not establish a cause.
- Retire or migrate old evaluator IDs and mission-save state deliberately; preserve drafts and earned progress where safely possible.
- No new culprit, tampering claim, or additional story case is authorized by this gameplay change.
## Guided gameplay implementation update — 2026-11-12

- **Implemented foundation:** Operations now renders the RX-14 case as a five-phase case flow instead of a fixed mission checklist.
- **Implemented foundation:** Case briefing, discovery of the two shared receipt evidence records, analysis acknowledgement, decision selection, and debrief completion persist through the existing case-progress use cases.
- **Implemented foundation:** Players may choose to state that evidence is insufficient; alternative decisions request clock-sync or processing-queue logs without asserting a culprit.
- **Test added:** an end-to-end domain progression test covers the complete RX-14 case and its uncertainty outcome.
- **Migration batch:** only the first two Python exercises remain mandatory beginner onboarding. The six later legacy exercises no longer map to story checkpoints; their definitions remain for history/save compatibility. Old active legacy missions are cleared from the active slot while completed IDs, XP, and saved drafts are preserved. An optional-practice interface is not yet exposed.
- **Still in progress:** direct cross-app case integration, richer analytical reasoning, decision rationale capture, competency rewards, and emulator smoke testing.
- **Build gate:** CI run [#158](https://github.com/MazBayID/firstGame/actions/runs/43161653497) passed on commit `940939e9e4f6a9ea1d0eb66e462a88ed9998e7ea`. Source tests and the development Debug APK build succeeded. The final-release job was skipped as expected; this is not a final or emulator-smoke-tested APK.

## RX-14 case interaction update

- Operations now requires the player to open the original VFS receipt content before recording a source as discovered. Evidence inspection and analysis remain separate actions. This is an incremental vertical slice, not completion of the overall First Contact gameplay redesign.
- CI run #158 passed source tests and built the development Debug APK. Final release was skipped; emulator smoke testing and physical-device acceptance remain outstanding.


### Editor submission visibility fix

- Added a dedicated Submit action directly beneath the First Contact code editor.
- Save persists a draft, Run executes code, and Submit invokes the deterministic mission evaluator as separate actions.
- Submit is enabled only when the exact current source matches the last successful Run. Editing after Run invalidates the eligible submission.
- Browser Mission Control remains a secondary entry point to the same evaluator path.
- CI run #165 passed source tests and built the development Debug APK. Device-level interaction and final emulator smoke testing remain outstanding.

### Case completion gate and repeat-action safety

- The final First Contact report is now gated by the RX-14 case reaching its persisted COMPLETE phase. The story action opens Operations and returns to the story after the case is completed.
- Repeating an already completed evidence-analysis action or submitting the same decision again is safe and does not corrupt case progress.
- Case snapshots now reject analyzed evidence IDs that were never discovered.
- CI verification is pending for this batch. This is still a development build; emulator and physical-device acceptance remain outstanding.


## Development priority reset — complete game before packaging

**Player direction, 2026-11-12:** do not spend development batches producing APKs or polishing CI while core gameplay, content, and UI are still incomplete. APK production is a release-candidate activity, not a measure of feature progress.

### New batch policy

- Every feature batch must advance a connected player-facing system or remove a real integration blocker.
- Keep fast, relevant unit/domain tests during implementation to catch regressions. Run broader source suites at coherent integration checkpoints; do not let CI maintenance become the batch's primary deliverable.
- Ordinary pushes to `main` run source tests only. The automatic development-APK job has been removed. Do not manually dispatch a release build until the complete-game acceptance gates are met and the player explicitly asks for release packaging.
- Do not count a screen, button, stub, isolated use case, successful compilation, or APK as a complete feature.
- A system is complete only when its authored content, domain rules, UI interactions, navigation, persistence/recovery, feedback, and progression work together in the actual player journey.

### Integrated game work order

1. **Campaign experience map:** map all fourteen First Contact beats to player actions, active objectives, required/optional information, relevant apps, state transitions, dialogue, and debrief outcomes. Remove dead ends and duplicate routes.
2. **Case and evidence engine:** finish structured provenance, immutable source assets, evidence relationships, derived findings, review history, status-transition rules, hypotheses, and uncertainty-aware reasoning.
3. **Mission contract and evaluation:** unify briefing objectives, discoverable facts, technical execution (only where justified), analytical conclusions, operational choices, deterministic evaluation, retry behavior, and rewards.
4. **Connected A.E.G.I.S. tools:** Messenger, Browser, Terminal, Code Editor, Dossier, Database, Analysis, and Operations must read/write the same underlying case state and return the player to the correct operation context.
5. **Full First Contact content:** complete authored dialogue, source documents, evidence, analysis prompts, decision choices, consequences, debriefs, clues, and progression for the whole chapter. Do not invent a new case or culprit.
6. **Player-facing UI and usability:** make every objective discoverable, every action explainable, every submission reachable from the relevant workspace, and every success/failure state visible. Resolve the current editor Submit defect as part of this integrated flow.
7. **Progression and save continuity:** validate old-save migration, interruption/resume, idempotent rewards, archives, rank/clearance, and case history across the whole campaign.
8. **Content parity and polish:** finish English-first/Indonesian-supported content coverage, responsive layouts, accessibility, and presentation after the connected gameplay contracts are stable.

### Acceptance definition

A complete First Contact experience must be playable from a fresh save to its chapter debrief without developer instructions or hidden navigation. Every required objective can be found and completed; optional leads remain optional; technical tasks are used only where story-relevant; evidence retains source/provenance; analytical and operational conclusions receive fair deterministic feedback; save/reload does not break progression; and the player can understand what happened and why.

Only after this journey is complete should we schedule a release-candidate batch for full regression, APK packaging, emulator smoke testing, and physical-device acceptance.


### Shared source inspection across A.E.G.I.S. apps

- Messenger attachments and Browser Intelligence Reports now expose the same original RX-14 source content from the shared VirtualFileSystem.
- After reading a source, the player can explicitly record it as inspected from either app while the case is in the correct phase.
- Operations consumes the same persisted case-progress state, so a source inspected in Messenger or Browser is not presented as undiscovered again.
- A shared domain policy controls when required and optional evidence can be discovered. Required sources are inspected during Investigation; optional leads may also be inspected during Analysis.
- Unit tests cover the discovery policy. CI and integrated navigation verification remain pending.


## RX-14 chapter completion reward — 2026-11-12

- Completing the persisted RX-14 debrief now awards 100 Intelligence XP through the shared completion ledger.
- The reward is idempotent and is repaired for existing saves that already reached COMPLETE without a corresponding reward entry.
- Operations displays the chapter reward and updated total XP after completion.
- A ViewModel regression test covers exactly-once award behavior. Source-test workflow verification is pending.
- This is a progression integration improvement, not a claim that the complete First Contact campaign is finished.


## RX-14 analytical conclusion and decision rationale

- After both required records are analyzed, the player must select an analytical conclusion before operational decisions unlock.
- The supported conclusion states only what the sources establish: RX-14 is recorded four seconds apart; the cause is not established.
- Unsupported causal claims receive deterministic feedback and keep the player in Analysis so they can revise the conclusion without losing progress.
- An operational decision now requires a non-empty player-authored rationale (maximum 600 characters); the rationale is persisted and shown in debrief.
- Save schema 9 persists the selected analysis conclusion and decision rationale. Schema-8 saves receive safe defaults without losing their existing decision/progress.
- Source and domain tests cover conclusion gating, retry behavior, rationale persistence, and schema migration. CI verification is pending.


## Analysis application connected to RX-14 case progression

- Operations no longer treats evidence analysis as a standalone checklist. Its Analysis phase opens the dedicated Analysis workspace.
- The Analysis app reads the same persisted RX-14 discovery/analyzed state, records analysis results, and presents the evidence-backed conclusion choices.
- The player can enter Analysis directly from the hub or through Operations; returning through the hub restores the correct Operations context.
- The same Analysis app continues to support general evidence status review and audit reasons.
- Domain tests already cover the phase/conclusion rules; UI compilation and integrated navigation verification are pending.
