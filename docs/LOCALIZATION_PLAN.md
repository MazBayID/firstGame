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

The selected language is now passed into the campaign timeline, story mission workspace, mission debrief, App Hub, main menu, settings, language preference screen, and recruitment/Freya notifications. The RX-14 case title and briefing, analysis option titles/descriptions/feedback, operational decision titles/descriptions, and mission source presentation are resolved at the UI boundary without changing canonical IDs or evaluator contracts.

The shared First Contact content resolver now supplies English variants for all eight catalog missions: titles, briefings, next clues, objectives, objective descriptions, and graduated hints. The Story, optional Training Simulator, Browser Mission Control, Intelligence Database, and Personnel Dossier use these localized mission values where those details are shown.

A new unit-test suite checks the RX-14 translations, all eight mission-content mappings, and stable language behavior. The current implementation still uses a transitional Kotlin presentation resolver with inline language pairs rather than the final stable resource/message-key system described in the technical direction. Do not mark L2–L5 complete until all remaining player-facing routes and evaluator feedback are covered, a missing-key/content parity check exists, and both language journeys receive manual Android review.
