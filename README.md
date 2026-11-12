# CYBER PROTOCOL

**Adventure RPG · Android-first · Cinematic cyber-intelligence adventure**

Cyber Protocol is a story-driven Android Adventure RPG about an ordinary person recruited into **A.E.G.I.S.**, a fictional independent intelligence organization headquartered in Manila. The player investigates fictional incidents, examines evidence, and completes authorized simulations while gradually learning programming and technical skills.

Programming is part of the world and the gameplay—not a detached course or a quiz with a story skin.

## Campaign 01 — THE RECRUITMENT

Chapter 001 — **FIRST CONTACT** opens with an official A.E.G.I.S. recruitment notification. The player must tap it, read the organization's invitation in Secure Messenger, and follow its in-game mission link.

When the first mission becomes active, a separate notification arrives from **Freya Knox**. She introduces herself as the player's mentor and guides the A.E.G.I.S. assignments. Freya is secretly the person who initiated the recruitment order, but neither the official invitation nor her initial greeting reveals that fact.

Freya later provides a link to **NEXUS**, an internal A.E.G.I.S. support system. NEXUS does not appear in the Messenger inbox until the player taps that link. Freya remains the primary human mentor.

The opening notifications and links are in-game simulations. The app does not access real phone messages, contacts, or Android notifications.

## Current playable foundation

First Contact retains its approved recruitment story, 14 story beats, Freya/NEXUS roles, and beginner-onboarding purpose. Only two Python exercises are mandatory: the signal and identity exercises. Six later coding exercises remain in the catalog for legacy-save/history compatibility, but are no longer mandatory and do not block the RX-14 case. A dedicated optional-practice interface has not yet been implemented.

RX-14 is a persisted, five-phase case connected to the campaign story and the shared A.E.G.I.S. tools. The same case progress is used by Messenger, Browser, Terminal, Operations, Analysis, Intelligence Database, and Personnel Dossier. Original receipt assets remain in the game-owned virtual filesystem; source inspection, analysis, the supported conclusion, operational rationale, decision, debrief, and the 100 Intelligence XP chapter reward are persisted. The valid **insufficient evidence** outcome preserves the unresolved cause of the four-second discrepancy.

A source-level acceptance test now exercises a fresh campaign through both required training missions, the complete RX-14 case, reward delivery, and session reload. This is automated integration coverage—not a substitute for testing the experience on a physical Android device.

Remaining work includes broader player-facing localization, an optional-practice interface, future approved chapters, continued UI/session-state cleanup, and physical-device acceptance. No later case or culprit is being invented to fill the campaign.

## Gameplay approach

Cyber Protocol uses a **Guided Intelligence RPG** structure. The campaign gives the player clear story context, objectives, and fair guidance while preserving freedom to choose a reasonable investigative approach.

The core loop is:

1. **Intelligence Briefing** — understand the situation, objective, and unknowns.
2. **Investigation** — inspect relevant sources and use the tools that fit the case.
3. **Intelligence Analysis** — compare evidence, test hypotheses, and distinguish facts from assumptions.
4. **Operational Decision** — submit a supported conclusion, choose an authorized next action, or request more verification.
5. **Debriefing & Progression** — receive deterministic feedback, record findings, and advance the campaign.

Missions use five primary archetypes: Narrative, Investigation, Technical, Analytical, and Operational. A mission may combine activities, but it has one dominant experience. The player is solving a problem, not completing a mandatory sequence of apps. Python and other technical skills are used when the case genuinely calls for them; not every mission requires code.

The **Evidence & Investigation Engine** provides structured source provenance, evidence relationships, review history, controlled status transitions, and shared case progression. RX-14 is the first integrated case; deeper hypothesis/derived-finding tools and additional approved cases remain future work.

## Core systems

- **First-person player identity:** the player is the operative; no player-avatar portrait is required.
- **In-world learning:** programming concepts appear when the story gives the player a reason to use them.
- **Local Python execution:** CPython 3.13 through Chaquopy on Android.
- **Separate RUN and SUBMIT:** Code Editor RUN saves the source to the mission script and opens the dedicated Terminal to execute `python <script>.py`; the Code Editor exposes a dedicated Submit action, and Browser Mission Control remains an alternate entry point. Both submit the exact source from the latest successful RUN to the same authored mission evaluator.
- **Mission datasets:** deterministic, fictional, read-only inputs with multiple evaluator fixtures where appropriate.
- **Secure Messenger:** A.E.G.I.S., Freya, and NEXUS conversations unlock through the campaign sequence; Freya provides the intranet link, fictional credentials, and the first mission attachment.
- **A.E.G.I.S. Browser:** an offline intranet with local fictional login, Mission Control, File Repository, Intelligence Reports, and Agent Profile. Browser downloads and Messenger attachments resolve to the same mission file.
- **Virtual terminal and workspaces:** Linux-like commands operate within a game-managed virtual filesystem, never the Android host shell. The first signal fragment is downloaded into its mission workspace and read through the Terminal.
- **Evidence and progression:** mission checkpoints, provenance-aware evidence, XP, rank, clearance, archives, and achievements.
- **Narrative clues:** each mission's successful debrief surfaces a short authored clue that points toward the next story or technical step.
- **Offline-first foundation:** core story, mission execution, evaluation, and progression do not require an account, server, external AI, or API key.
- **Accessibility foundation:** reduced-motion preference and responsive Android layouts.

## Language

**Approved direction: English-first, Indonesian-supported.** English is the default for fresh installations; Bahasa Indonesia is an in-game alternative whose choice will persist locally. English is the source language for new story and UI content, and both languages must reach full player-facing content parity before bilingual support is called complete. Python syntax, identifiers, standard command names, file paths, and evaluator-sensitive output remain unchanged.

The Language screen is functional and persists the English/Bahasa Indonesia choice locally. English is the fresh-install default, the main menu has localized resources, and all 14 story beats have English and Indonesian variants. Operational screens, Messenger dialogue, and much of the remaining player-facing UI are still Indonesian-first, so full language parity is not complete. See [Localization Plan](docs/LOCALIZATION_PLAN.md) and the language rules in GAME_CONCEPT.md.

## Safety and technical limitations

All principal cases, organizations, people, and datasets are fictional. Technical operations are controlled simulations.

The current Python runner applies AST restrictions and resource limits, but executes in-process. It is **not a hardened sandbox for hostile code** and must not be treated as one.

## Canonical design references

- [GAME_CONCEPT.md](GAME_CONCEPT.md) — product direction, gameplay rules, and implementation constraints.
- [Story_Line.md](Story_Line.md) — narrative blueprint and continuity reference.
- [Visual Identity](docs/VISUAL_IDENTITY.md) — approved visual direction.
- [Asset Catalog](docs/ASSET_CATALOG.md) — semantic UI asset registry.

## Build and CI

Requirements for local validation: JDK 17, Android SDK Platform 36, Build Tools 36.0.0, Gradle 8.13, and Python 3.13.

Run the source test suites locally:

```bash
python -m unittest discover -s tests -v
gradle --no-daemon testReleaseUnitTest
```

- **Pushes to `main`:** run Python and Release-variant Android/Kotlin unit tests only. Do not build or publish an APK for ordinary development commits.
- **Pull requests targeting `main`:** source-level tests only. No installable APK and no release-signing secrets.
- **Final APK:** a manual release dispatch is reserved for an explicitly approved release candidate after the complete campaign and gameplay acceptance gates have passed. It is not part of ordinary feature batches.
- Emulator smoke testing and physical-device acceptance remain final release gates, not substitutes for complete gameplay integration.

The permanent keystore is restored only inside the ephemeral Final Release runner and removed at the end of the job. The project is an evolving game foundation; the full v1.0 campaign is not yet complete.
