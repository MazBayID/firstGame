# Game design — Cyber Protocol

## Core fantasy

The player is a junior ethical hacker / security analyst working on fictional investigations. Missions teach practical programming and defensive security concepts through real code, not multiple-choice questions or fake terminal output.

## Design pillars

1. **Real code:** mission solutions execute in CPython; success is validated against multiple unseen evidence fixtures.
2. **Ethical investigations:** all targets, systems, and evidence are fictional and offline in the first release.
3. **Mobile-first editor:** mission terminal above, code editor below, syntax colors, line numbers, auto-indent, and shortcut keys.
4. **RPG progression:** XP, credits, levels, mission unlocks, character dialogue, and saved progress.
5. **Expandable curriculum:** Python fundamentals first; advanced C++ missions follow when a real compiler runtime is integrated.

## Story — Chapter 1: Signal in the Noise

The player joins Helix Dynamics as a junior analyst. Senior analyst Mara assigns a night-shift audit after suspicious access attempts appear in a damaged service log. The first case is **The Corrupted Access Log**.

### Mission 01 — The Corrupted Access Log

- Skill: file reading, loops, conditionals, string membership, list append, and len.
- Task: read the virtual access.log and count lines containing ERROR.
- Evidence validation: the same program must return 2, 1, and 0 for three different log fixtures.
- Reward: 100 XP and 50 credits, granted once and persisted locally.

## Current implementation

The Android application currently contains the Chapter 1 opening briefing and Mission 01. Python code executes locally through Chaquopy / CPython 3.13. The editor provides basic syntax highlighting, line numbers, auto-indent, and insertion shortcuts. XP, credits, and mission completion are saved with Android SharedPreferences.

## Planned completion milestones

1. Finish Chapter 1 mission flow and branching dialogue.
2. Add additional Python missions covering parsing, dictionaries, functions, and basic network concepts using simulated lab data.
3. Add a C++ learning chapter with a genuine compile/run toolchain; do not label a parser or mock as a real compiler.
4. Add player profile, mission map, inventory, save migration, and settings.
5. Add terminal/avatar animation, audio, onboarding, and responsive layouts.
6. Harden code execution boundaries, add automated Android UI tests, and produce a signed release APK.

## Safety boundary

The current Python runner is a constrained local teaching environment, not a hardened sandbox. It must never execute remote or hostile code. All security scenarios are fictional simulations; the game does not connect to real targets.