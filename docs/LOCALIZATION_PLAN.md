# Cyber Protocol — Localization Plan

**Approved product direction:** English-first, Indonesian-supported  
**Canonical policy:** GAME_CONCEPT.md §3.4  
**Current implementation state:** L1 is implemented; L2, L3, and L4 have substantial but incomplete coverage. The language preference persists independently, all 14 story beats and authored Messenger dialogue have English and Indonesian variants, and the campaign, case, training, and several A.E.G.I.S. tool surfaces now read the selected language. L5 parity and manual review are not complete.

## 1. Product behavior

- Fresh installations start in **English**, independently of Android's configured language.
- Players can select **English** or **Bahasa Indonesia** from the in-game Language screen.
- The selected language is persisted locally and restored on every launch.
- Language changes never reset or mutate campaign saves, mission progress, evidence, code drafts, rank, clearance, or unlocks.
- English is the authoring/source language. Indonesian translations follow the approved English source.
- Both language variants must preserve the same mission IDs, evaluator behavior, evidence IDs, progression rules, and available choices.

## 2. Localization boundaries

### Localizable content
- Main menu, navigation, settings, accessibility, dialogs, and notifications.
- Character dialogue, story beats, briefings, objectives, hints, and debriefs.
- Browser/intranet pages, database and dossier labels, evidence descriptions, analysis and operations UI.
- Empty states, validation feedback, errors intended for players, achievements, and profile text.

### Never translate
- Python syntax, keywords, identifiers, and code supplied to or written by the player.
- Standard terminal command names, command arguments, virtual paths, mission IDs, evidence IDs, evaluator IDs, and protocol-like identifiers.
- Literal output values that a mission evaluator expects to match.
- Fictional organization/person names and other canonical proper nouns unless a specific localized display form is approved.

## 3. Technical direction

1. Establish a single language identifier model (en, id) and a small localization boundary independent of campaign persistence.
2. Use stable message keys and separate Android string resources for reusable interface text. Avoid inline language conditionals inside individual composables.
3. Move authored story and mission copy into localized content data keyed by stable IDs. Do not duplicate mission logic or evaluator fixtures per language.
4. Keep the selected language in a dedicated local settings preference. Do not store it in the campaign save schema.
5. Resolve localized text at presentation boundaries; domain rules, IDs, file contents, Python execution, and evaluators remain language-neutral.
6. Add automated checks for missing keys and required content variants. A missing translation must fail validation rather than silently show the wrong language.
7. Verify that switching language while a campaign is in progress preserves all player state.

## 4. Rollout sequence

- **L0 — Contract:** approve English-first behavior, terminology boundaries, persistence rules, and parity requirements. (Approved)
- **L1 — Foundation:** language identifier, English fresh-install default independent of Android locale, local preference persistence, and a functional Language screen. (Implemented)
- **L2 — Shell:** localize main menu, navigation, settings, shared components, and system dialogs. (Main menu, campaign timeline, Language screen, settings, App Hub, campaign reset dialog, and recruitment/mentor notifications now use the selected language; other shell and profile surfaces remain)
- **L3 — Campaign:** localize all First Contact story beats, Messenger dialogue, briefings, objectives, hints, mission feedback, and debriefs. (All 14 story beats, authored Freya/NEXUS dialogue, the two mandatory onboarding missions, six optional legacy exercises, RX-14 case briefing/conclusions/decisions, and mission debrief labels have selected-language variants; evaluator-generated feedback and several campaign messages still need parity review)
- **L4 — Operational apps:** localize Browser, Terminal explanatory UI, Database, Dossier, Analysis, Operations, Profile, Archives, and Achievements.
- **L5 — Quality gate:** automated key/content parity checks, language-switch persistence tests, regression tests for mission IDs/evaluators, and manual review of both languages on Android.

Do not expose English as a completed playable language until L2–L5 cover every player-facing route. During rollout, the Language page must accurately state which language coverage is complete; it must not imply that a partial translation is complete.

## 5. Acceptance criteria

- A clean install opens in English even when Android is configured in Indonesian.
- Selecting Bahasa Indonesia changes all supported player-facing content and survives force-stop/relaunch.
- Selecting English restores English and survives force-stop/relaunch.
- Switching language does not change campaign save data or progress.
- Both variants use identical mission/evidence identifiers and evaluator behavior.
- No evaluator-sensitive Python code, terminal command, path, or literal output is translated.
- Automated tests detect missing keys or missing authored content variants.
- No major player-facing screen silently falls back to another language.


## Implementation checkpoint — 2026-11-12

The current localization batch extends bilingual interface coverage in the RX-14 Operations screen and the optional Training Simulator. Phase/status labels, decision-rationale guidance and legacy-save fallbacks, training navigation, workspace labels, and RUN/SUBMIT controls now use the selected game language. Practice-screen feedback for draft edits and saves is also localized.

This is **interface-copy coverage only**, not full L3/L4 parity. Authored case titles, briefings, analysis/decision option content, optional mission titles/objectives/briefings/hints, and some player-facing output elsewhere still need reviewed English variants. The Language screen must continue to describe localization as partial.


## Implementation checkpoint — 2026-11-12 (expanded First Contact coverage)

The selected language is now passed into the campaign timeline, story mission workspace, mission debrief, App Hub, main menu, settings, language preference screen, recruitment/Freya notifications, Operative Profile, Archives, Achievements, and Terminal. The RX-14 case title and briefing, analysis option titles/descriptions/feedback, operational decision titles/descriptions, and mission source presentation are resolved at the UI boundary without changing canonical IDs or evaluator contracts.

The shared First Contact content resolver now supplies English variants for all eight catalog missions: titles, briefings, next clues, objectives, objective descriptions, graduated hints, and RX-14 evidence title/summary/source metadata. The Story, optional Training Simulator, Browser Mission Control, Intelligence Database, Personnel Dossier, and Archives use these localized mission values where those details are shown.

A new unit-test suite checks the RX-14 translations, all eight mission-content mappings, and stable language behavior. The Python evaluator now receives the selected language for story and optional-practice submissions and returns Indonesian translations for authored guidance, mismatch summaries, and pass feedback; English remains the default for compatibility. The virtual Terminal now localizes its command guide and identity output while preserving command names and file contents. Rank labels and RX-14 evidence metadata are localized at the presentation boundary without mutating persisted values. The implementation still uses a transitional Kotlin presentation resolver with inline language pairs rather than the final stable resource/message-key system described in the technical direction. Do not mark L2–L5 complete until all remaining player-facing routes and evaluator feedback are covered, a missing-key/content parity check exists, and both language journeys receive manual Android review.


## Implementation checkpoint — 2026-11-12 (A.E.G.I.S. profile and evaluator feedback)

The selected language now also controls the Operative Profile, Archives, Achievements, Terminal guidance labels, and the displayed mission-workspace label. Archived mission titles use the same stable-ID content resolver as Story and Browser Mission Control. Evidence status labels are translated only at the presentation boundary; enum values and persisted records remain unchanged.

The Python evaluator now accepts an optional language tag. English remains the default for compatibility with existing callers and test fixtures; the Android service passes the selected in-game language for story and optional practice submissions. Evaluator-authored concept guidance, pass feedback, and test mismatch summaries have Indonesian variants. Python syntax errors, runtime errors, identifiers, and expected literal output are intentionally left technical and unchanged.

The Python test suite now includes a language-parity check for evaluator feedback. The latest CI run before this checkpoint exposed a malformed newline in that new test fixture; the fixture has been corrected and a fresh CI run is required before this batch can be considered verified.

### Remaining localization gates

- Localize the Terminal's system-authored help and error messages without translating player files, command names, paths, code, or command output.
- Review evidence titles/summaries and story/mission feedback for both languages, including old-save fallbacks.
- Complete Profile/Archives/Achievements content review and ensure all rank, clearance, status, and checkpoint labels are understandable.
- Replace transitional inline bilingual strings with a stable message-key/resource strategy, or document and test the approved alternative if a full resource migration is not justified.
- Add an automated completeness/parity check covering every player-facing route and authored content ID.
- Perform manual English and Indonesian journeys on Android, including language switching mid-campaign, force-stop/relaunch, save migration, and end-to-end First Contact completion.


## Implementation checkpoint — 2026-11-12 (evidence, Terminal, and rank parity)

The virtual Terminal command guide and `whoami` response now respect the selected language. Command tokens, paths, player-created file contents, and Python output remain untouched. The profile and personnel dossier display localized rank names while the persisted rank identifiers remain canonical.

RX-14 evidence titles, the derived discrepancy summary, and source labels now have English presentation variants in Operations, Analysis, Intelligence Database, and Archives. Original receipt contents and stored EvidenceRecord fields remain unchanged. The evaluator's language parameter and localized feedback are covered by Python tests; Terminal language behavior, rank labels, and evidence metadata have Kotlin unit-test coverage.

This checkpoint is implementation progress, not full localization acceptance. Other VFS error messages, all remaining evidence/review metadata, the full message-key parity mechanism, and physical Android review remain open.


## Implementation checkpoint — 2026-11-12 (player feedback and evidence metadata parity)

The selected language now covers story and optional-practice editor feedback for save, RUN, SUBMIT, verification, execution failures, and output placeholders. Switching language clears transient evaluator feedback and re-renders the save-status label without changing code drafts or mission progress. The virtual Terminal now localizes its startup banner, clear-screen response, help, command errors, permission errors, empty directory state, and identity response while preserving command names, paths, file bytes, and Python output.

Evidence presentation now localizes mission-evaluator provenance and summaries, evidence source labels, review-history status transitions, evidence origins, and relationship types. The Intelligence Database and Personnel Dossier use the shared RX-14 and mission-content resolver for case titles, briefings, conclusions, decisions, and related mission details. The obsolete duplicate mission-database composable was removed so the routed Intelligence Database remains the single source of truth.

Regression coverage now checks bilingual story beats, all eight mission content mappings, RX-14 analysis/decision variants, evaluator feedback, mission evidence provenance, Terminal language behavior, evidence relation labels, and stable canonical IDs.

**Verified source CI:** [run 43237898965](https://github.com/MazBayID/firstGame/actions/runs/43237898965) — Python runner tests and Release-variant Android unit tests passed on source commit `0ceb4d5d5b41b8092d84877232f826cf0869f6b5`. This was a source-test run; it did not create an APK.

### Remaining localization acceptance gates

- Replace or formally approve the transitional inline bilingual resolver in place of the planned stable resource/message-key system. There is still no general missing-key scanner for every player-facing string.
- Review any remaining mixed-language technical labels and old-save fallback text.
- Run complete English and Indonesian journeys on physical Android devices, including fresh install, mid-campaign language switching, force-stop/relaunch, save migration, and end-to-end debrief.
- Keep runtime exceptions, source code, commands, paths, player-authored rationale, and literal evaluator output unchanged where translation would corrupt their meaning or verification.
