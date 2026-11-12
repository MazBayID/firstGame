# Cyber Protocol — Localization Plan

**Approved product direction:** English-first, Indonesian-supported  
**Canonical policy:** GAME_CONCEPT.md §3.4  
**Current implementation state:** L1 foundation is implemented; the Language destination persists a preference and the main menu uses localized resources. Full player-facing localization remains incomplete.

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
- **L2 — Shell:** localize main menu, navigation, settings, shared components, and system dialogs. (Main menu resources started; remaining shell coverage in progress)
- **L3 — Campaign:** localize all First Contact story beats, Messenger dialogue, briefings, objectives, hints, mission feedback, and debriefs.
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
