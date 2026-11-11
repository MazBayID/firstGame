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

The approved First Contact chapter currently contains **12 story beats and six evaluated Python missions**:

1. Sinyal Pertama — variables and `print()`.
2. Verifikasi Identitas — named variables and output verification.
3. Audit Jejak Sistem — `for`, `if`, and counting log entries.
4. Inventaris Rekaman — `len()` and list size.
5. Penelusuran Referensi — `enumerate()`, filtering, and source indices.
6. Perbandingan Dua Sumber — list indexing and source comparison.

The closing report records a four-second difference between two fictional receipt timestamps. The difference is observable; its cause remains unverified. The chapter does not identify a culprit or claim tampering.

## Core systems

- **First-person player identity:** the player is the operative; no player-avatar portrait is required.
- **In-world learning:** programming concepts appear when the story gives the player a reason to use them.
- **Local Python execution:** CPython 3.13 through Chaquopy on Android.
- **Separate RUN and SUBMIT:** RUN executes code against the active mission data; SUBMIT evaluates the authored mission criteria.
- **Mission datasets:** deterministic, fictional, read-only inputs with multiple evaluator fixtures where appropriate.
- **Secure Messenger:** A.E.G.I.S., Freya, and NEXUS conversations unlock through the campaign sequence.
- **Virtual terminal and workspaces:** Linux-like commands operate within a game-managed virtual filesystem, never the Android host shell.
- **Evidence and progression:** mission checkpoints, provenance-aware evidence, XP, rank, clearance, archives, and achievements.
- **Narrative clues:** each mission's successful debrief surfaces a short authored clue that points toward the next story or technical step.
- **Offline-first foundation:** core story, mission execution, evaluation, and progression do not require an account, server, external AI, or API key.
- **Accessibility foundation:** reduced-motion preference and responsive Android layouts.

## Language

Bahasa Indonesia is the current implemented language. English selection and complete localization across all player-facing screens remain planned work; the design requirements are documented in `GAME_CONCEPT.md`. Python syntax and standard technical command names remain unchanged.

## Safety and technical limitations

All principal cases, organizations, people, and datasets are fictional. Technical operations are controlled simulations.

The current Python runner applies AST restrictions and resource limits, but executes in-process. It is **not a hardened sandbox for hostile code** and must not be treated as one.

## Canonical design references

- [GAME_CONCEPT.md](GAME_CONCEPT.md) — product direction, gameplay rules, and implementation constraints.
- [Story_Line.md](Story_Line.md) — narrative blueprint and continuity reference.
- [Visual Identity](docs/VISUAL_IDENTITY.md) — approved visual direction.
- [Asset Catalog](docs/ASSET_CATALOG.md) — semantic UI asset registry.

## Build

Requirements: JDK 17, Android SDK Platform 36, Build Tools 36.0.0, Gradle 8.13, and Python 3.13 for standalone runner tests.

```bash
python -m unittest discover -s tests -v
gradle --no-daemon testDebugUnitTest assembleDebug
```

Debug APK output: `app/build/outputs/apk/debug/app-debug.apk`.

The project is an evolving game foundation. This repository does not claim that the full v1.0 campaign is complete.
