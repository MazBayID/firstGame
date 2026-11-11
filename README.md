# CYBER PROTOCOL

**Cinematic Cyber-Intelligence RPG · Android-first · Educational Programming Game**

Cyber Protocol is a story-driven Android RPG in which an ordinary person is unexpectedly recruited into A.E.G.I.S., a fictional independent intelligence organization headquartered in Manila. The player learns programming and technical skills by using them to investigate fictional cases, examine evidence, and complete authorized simulated operations.

Programming is part of the world and the gameplay—not a detached course or a quiz with a story skin.

## Story

A device you rarely use receives an encrypted message from an unknown sender. The sender claims to represent A.E.G.I.S. and appears to know something about you. The organization is investigating anomalies in records, financial trails, and disconnected systems. Before the player can understand the larger mystery, one question must be answered:

**Why were you contacted?**

The player begins Chapter 001 — FIRST CONTACT with a guided introduction to A.E.G.I.S., a classified recruitment file, and two progressive Python decryption exercises. After recruitment, the player is assigned **Freya Knox**, a highly capable mentor whose outrageous confidence and playful teaching style gradually become part of the team's dynamic.

## Core design

- **Story-driven campaign:** cinematic narrative beats and progressively unlocked operations.
- **First-person player identity:** the player is the operative; no player-avatar portrait is required.
- **In-world learning:** Python concepts are introduced when the story gives the player a reason to use them.
- **Real local Python execution:** CPython 3.13 through Chaquopy on Android.
- **Separate Run and Submit:** RUN executes code; SUBMIT evaluates the current objective.
- **In-game terminal:** recognizable Linux-like commands operate inside a game-managed virtual filesystem, never the Android host shell.
- **Two workspaces:** persistent Personal Workspace and mission-scoped working areas.
- **A.E.G.I.S. application hub:** Secure Messenger, Terminal, Intelligence Database, Dossier, Analysis, and Operations unlock progressively.
- **Offline-first foundation:** the opening campaign flow and core training do not require a server or external AI API.
- **Safety boundary:** fictional cases and controlled training data only. The current Python runner is a restricted teaching prototype, not a hardened sandbox for hostile code.

## Language\n\nBahasa Indonesia is the default language for the opening experience, including campaign story, menu navigation, mentor dialogue, app labels, and the in-game terminal guidance. Python syntax and terminal commands intentionally retain their standard technical spelling.\n\n## Current playable foundation

The current reconstruction establishes the Android app shell and an initial vertical slice:

- Classic mobile-game-inspired main menu with the provisional dark-cyan headquarters identity.
- Campaign 01 — THE RECRUITMENT and Chapter 001 — FIRST CONTACT.
- Persistent story checkpoints and two guided Python tasks using variables and `print()`.
- A secure-message interface introducing Freya Knox.
- Six-app A.E.G.I.S. hub with progressive access states.
- Local virtual filesystem with basic Linux-like file and directory commands.
- Python script execution from files saved in the virtual workspace.
- Existing UI artwork and emblem retained in the Android resource folders.

Several systems remain foundational prototypes. The full campaign, complete operational simulations, granular filesystem permissions, Python-to-virtual-filesystem APIs, robust execution isolation, and final cinematic art are not yet complete. See [Rebuild Status](docs/REBUILD_STATUS.md).

## Canonical design references

- [GAME_CONCEPT.md](GAME_CONCEPT.md) — canonical product and game-design bible.
- [Story_Line.md](Story_Line.md) — narrative blueprint and continuity reference.
- [Visual Identity](docs/VISUAL_IDENTITY.md) — approved visual direction.
- [Asset Catalog](docs/ASSET_CATALOG.md) — semantic UI asset registry.

## Build

Requirements: JDK 17, Android SDK Platform 36, Build Tools 36.0.0, Gradle 8.13, and Python 3.13 for the standalone runner tests.

```bash
python -m unittest discover -s tests -v
gradle --no-daemon assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

The project is an evolving game foundation, not a claim that the complete v1.0 campaign is already implemented.
