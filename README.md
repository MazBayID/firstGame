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

First Contact retains its approved recruitment story, 14 story beats, Freya/NEXUS roles, and beginner-onboarding purpose. Only the first two Python exercises remain required for beginner onboarding. The six later evaluated exercises have been removed from the mandatory story route; their definitions remain temporarily available for old-save/history compatibility, but a dedicated optional-practice interface has not yet been implemented. The case phases themselves no longer require the player to solve those legacy coding tasks.

The RX-14 case is now represented in Operations as a persisted five-phase case flow. The player inspects the original receipt sources, analyzes them separately, and chooses a proportionate operational response. The valid outcome **insufficient evidence** preserves the unresolved cause of the four-second discrepancy. This case flow is a playable foundation; story routing and cross-app evidence interactions are still being converted.

## Gameplay approach

Cyber Protocol uses a **Guided Intelligence RPG** structure. The campaign gives the player clear story context, objectives, and fair guidance while preserving freedom to choose a reasonable investigative approach.

The core loop is:

1. **Intelligence Briefing** — understand the situation, objective, and unknowns.
2. **Investigation** — inspect relevant sources and use the tools that fit the case.
3. **Intelligence Analysis** — compare evidence, test hypotheses, and distinguish facts from assumptions.
4. **Operational Decision** — submit a supported conclusion, choose an authorized next action, or request more verification.
5. **Debriefing & Progression** — receive deterministic feedback, record findings, and advance the campaign.

Missions use five primary archetypes: Narrative, Investigation, Technical, Analytical, and Operational. A mission may combine activities, but it has one dominant experience. The player is solving a problem, not completing a mandatory sequence of apps. Python and other technical skills are used when the case genuinely calls for them; not every mission requires code.

The **Evidence & Investigation Engine** connects immutable source assets, provenance-aware evidence records, case progression, and uncertainty-aware decisions. RX-14 is the first case-flow vertical slice. Cross-app source discovery, full analytical workspaces, and the remaining legacy mission migration are still in progress.

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

The current build is **not yet localized**: the Language menu is a placeholder and much of the UI and campaign content remains Indonesian-only. See [Localization Plan](docs/LOCALIZATION_PLAN.md) and the language rules in GAME_CONCEPT.md.

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
