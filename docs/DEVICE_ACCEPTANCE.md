# Cyber Protocol — Physical Android Acceptance

This checklist is the final manual acceptance gate for the current First Contact rebuild. CI success is necessary but does not replace installation and gameplay checks on a real Android device.

## Test build identity

- [ ] Record the exact Git commit used to build the APK.
- [ ] Record the GitHub Actions run and artifact name.
- [ ] Confirm the APK is labeled **DEVELOPMENT — NOT FINAL**.
- [ ] Keep the existing app installation and save data until the update test is complete.
- [ ] Do not use a permanently signed Final APK workflow until the release candidate is explicitly approved.

## Installation and launch

- [ ] Clean install on a device without an existing Cyber Protocol save.
- [ ] Confirm the app opens without crash, blank screen, or repeated restart.
- [ ] Confirm a fresh installation defaults to English even when Android uses Indonesian.
- [ ] Update an existing installation without uninstalling it.
- [ ] Confirm the existing campaign save, evidence, XP, rank, clearance, drafts, and unlocks survive the update.
- [ ] Force-stop and relaunch; confirm the saved state is restored.

## Main menu and accessibility

- [ ] Confirm the A.E.G.I.S. headquarters backdrop scales in portrait orientation and menu labels remain readable.
- [ ] Open every main-menu destination: Continue, New Campaign, Application Hub, Profile, Archives, Achievements, Settings, and Language.
- [ ] Toggle Reduce Motion; confirm the opening animation respects the setting and the preference persists after relaunch.
- [ ] Open and dismiss New Campaign confirmation without changing any data.

## Recruitment and story continuity

- [ ] Start a new campaign and confirm the official A.E.G.I.S. recruitment notification appears.
- [ ] Open the notification and verify the invitation is authored by A.E.G.I.S., not an unknown sender.
- [ ] Confirm Freya's separate introduction appears at the correct story checkpoint.
- [ ] Confirm NEXUS remains unavailable until the authored Messenger link is opened.
- [ ] Confirm the fourteen First Contact story beats progress in the approved order.
- [ ] Confirm the story never invents the recruiter identity or attributes an unapproved motive.

## Messenger, Browser, and Terminal

- [ ] Verify the contact list keeps Freya and NEXUS conversations separate.
- [ ] Send predefined replies; navigate away and back; confirm history persists.
- [ ] Open Freya's intranet link and verify the Browser returns to the correct context.
- [ ] Sign in using the fictional recruitment credentials and confirm the offline Browser works without public internet.
- [ ] Download the mission attachment from Messenger and the matching asset from the File Repository; confirm both represent the same source content.
- [ ] In Terminal, run `pwd`, `ls`, `help`, and `cat` on an authorized file.
- [ ] Confirm the Terminal never opens the Android/Linux host shell.
- [ ] Confirm read-only mission assets cannot be overwritten or deleted.
- [ ] Confirm mission workspace paths cannot be reached from the personal Terminal.

## Code Editor and learning progression

- [ ] Confirm the two mandatory Python onboarding tasks are understandable in English and Indonesian.
- [ ] Confirm the starter code does not contain a bundled executable answer.
- [ ] Download the authorized input file, inspect it with `cat`, and use its exact content in the exercise.
- [ ] RUN executes code but does not complete the mission or award XP.
- [ ] SUBMIT remains disabled until the exact current source completes a successful RUN.
- [ ] Editing code after RUN invalidates the prior run result.
- [ ] Confirm failed submissions provide useful feedback and allow retry.
- [ ] Confirm successful completion awards XP once and creates mission-provenanced evidence.
- [ ] Confirm the six retired Python exercises are optional practice and never block the main campaign.

## RX-14 investigation

- [ ] Open both required receipt records and confirm the timestamps are 09:41:12 and 09:41:16.
- [ ] Confirm the source contents remain read-only and provenance is visible.
- [ ] Record each required source review and confirm the case phase advances only when its requirements are met.
- [ ] Record an analysis conclusion supported by the two sources.
- [ ] Confirm the interface does not describe the discrepancy as proof of tampering or identify a culprit.
- [ ] Test a defensible **insufficient evidence** decision.
- [ ] Confirm alternative decisions request additional logs without claiming an unproven cause.
- [ ] Open the final debrief and confirm it records the selected conclusion, decision, rationale, unresolved discrepancy, and XP reward.

## Profile, Archives, and Achievements

- [ ] Confirm Profile shows current rank, XP progress, clearance, competency XP, mission statistics, and case progress.
- [ ] Confirm Archives can search and open completed mission files.
- [ ] Confirm Archives can search and open evidence records, including provenance and review history.
- [ ] Confirm the RX-14 case report is available in Archives.
- [ ] Confirm Achievements update from saved mission, evidence, decision, debrief, practice, and competency progress.
- [ ] Confirm optional-practice achievements do not block First Contact completion.

## Language switching and data integrity

- [ ] Switch from English to Indonesian during an active campaign.
- [ ] Confirm story text, mission metadata, app navigation, Terminal guidance, evaluator feedback, evidence metadata, Profile, Archives, and Achievements use the selected language.
- [ ] Switch back to English and confirm the same content remains available.
- [ ] Confirm language switching does not change campaign progress, mission IDs, evidence contents, source files, code drafts, rank, clearance, or unlocks.
- [ ] Force-stop and relaunch; confirm the selected language persists.

## New Campaign reset semantics

Use a disposable save or back up the current save before testing this section.

- [ ] Create a file in Personal Workspace.
- [ ] Create or download a file in a mission workspace.
- [ ] Add Messenger replies and complete at least one mission.
- [ ] Start New Campaign and confirm the confirmation text explains what will reset.
- [ ] Confirm campaign progress, mission workspaces, and Messenger reply history reset.
- [ ] Confirm Personal Workspace files remain.
- [ ] Confirm canonical shared evidence assets remain available for the new campaign.
- [ ] Confirm language, Reduce Motion preference, and Browser authentication remain.
- [ ] Confirm the new campaign starts at the recruitment flow with clean editor and Terminal state.

## Layout, keyboard, and navigation

- [ ] Test portrait mode on the target Android device.
- [ ] Confirm long screens scroll without clipping the bottom navigation or Android system bars.
- [ ] Open the keyboard in the code editor, Messenger, search fields, and decision-rationale field.
- [ ] Confirm input fields remain visible and scroll correctly while the keyboard is open.
- [ ] Test Android Back from every main screen and every App Hub tool.
- [ ] Confirm Browser, Messenger, and Terminal return to their originating screen without losing operation context.
- [ ] Confirm no screen becomes trapped or requires force-closing the app.

## Acceptance decision

- [ ] All critical gameplay and persistence checks pass.
- [ ] No known crash, progression blocker, data loss, or permission bypass remains.
- [ ] Any failed or skipped check is recorded with device model, Android version, APK commit, and reproduction steps.
- [ ] Only after all applicable checks pass may the rebuild be considered ready for a release-candidate review.

**Status:** Pending physical-device execution. This document is a test plan, not a claim that the checks have passed.
