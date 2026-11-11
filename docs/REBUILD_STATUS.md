# Cyber Protocol — Reconstruction Status

**Current foundation:** Android 0.6.0  
**Status:** Initial playable vertical slice under reconstruction  
**Canonical references:** `GAME_CONCEPT.md` and `Story_Line.md`

## Implemented in this reconstruction

- Classic mobile-game-inspired main menu using the provisional dark-cyan direction.
- Campaign 01 — THE RECRUITMENT and Chapter 001 — FIRST CONTACT story checkpoints.
- Local persistence of campaign story stage.
- Two guided Python tasks: variable assignment and `print()`, followed by separate Run and Submit actions.
- Code draft saving at explicit save action and successful task checkpoint.
- A.E.G.I.S. app hub with six applications and stage-based locked/unlocked presentation.
- Secure Messenger introduction to Freya Knox with predefined cosmetic response choices.
- Game-managed virtual filesystem persisted locally, with basic `pwd`, `ls`, `cd`, `cat`, `mkdir`, `touch`, `rm`, `echo ... > file`, `whoami`, `help`, and `clear` commands.
- Local Python script execution from a saved virtual-workspace file using `python <file.py>`.
- Existing raster UI assets, vector emblem, and launcher artwork retained.

## Deliberate limitations of this first slice

- The current campaign contains the recruitment introduction and two guided code checks, not the full Chapter 001 operation sequence or later campaigns.
- Intelligence Database, Dossier, Analysis, and Operations currently establish their app surfaces; their complete data and gameplay systems are future work.
- Virtual filesystem commands are a functional initial subset, not full Linux compatibility. File-level mission permissions, archive lifecycle, and robust command autocomplete remain to be implemented.
- The Python runner uses an AST allowlist and execution-event guard in the app process. This is a learning prototype, **not a hardened security sandbox**. Do not execute hostile or untrusted code.
- Python scripts can be launched from virtual files, but Python's own file APIs are not yet connected to the game-managed virtual filesystem.
- A full Code Runner isolation boundary, CPU/memory/wall-clock enforcement, cancellation, and adversarial testing remain required.
- The final A.E.G.I.S. headquarters cinematic artwork and character artwork have not been produced; the current interface uses existing assets and native Compose UI.

## Next engineering priorities

1. Complete and validate the Android build on GitHub Actions.
2. Add automated tests for virtual filesystem path confinement, command behavior, persistence, and invalid input.
3. Introduce a language-neutral Code Runner contract and harden the Python execution boundary before expanding supported syntax.
4. Build the first complete story-driven operation with evidence, mission workspace, structured evaluator, debrief, and next-mission code clue.
5. Replace placeholder app surfaces with real story-linked data and functionality.
6. Implement checkpoint recovery, code-draft ownership, mission archive access, and granular permissions.
7. Test portrait Android layouts, keyboard behavior, and small-screen usability on physical devices.

## Safety and scope

All A.E.G.I.S. records, people, organizations, cases, and operational targets are fictional. Terminal commands execute only inside the game's virtual environment; they do not invoke Android/Linux shell commands or access arbitrary device files. Core campaign progression must remain playable offline.
