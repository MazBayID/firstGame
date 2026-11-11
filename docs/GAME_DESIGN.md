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

- Skill: beginner loop, condition, string membership, and counter increment.
- Task: use the preloaded `logs` list and count lines containing ERROR. The starter code provides the loop and condition; the player replaces one `pass` with `count += 1`.
- Teaching progression: file reading is deferred to M02 so the first mission teaches one new action at a time.
- Evidence validation: the same program must return 2, 1, and 0 for three different log fixtures.
- Reward: 100 XP and 50 credits, granted once and persisted locally.

### Mission 02 — The Warning Signal

- Skill: opening a file, iterating over lines, string matching, and counting.
- Task: read virtual access.log and count lines containing WARNING. The starter code provides the file-reading structure and leaves the counter action to the player.
- Evidence validation: the same program must return 2, 1, and 0 for three different log fixtures.
- Unlock: complete Mission 01.
- Reward: 100 XP and 50 credits, granted once.

### Mission 03 — The Failed Login Trail

- Skill: reinforce file iteration, filtering records, and identifying failed authentication events.
- Task: count lines containing FAILED while ignoring successful OK login records. The starter code provides the familiar file-reading structure and leaves the counter action to the player.
- Evidence validation: the same program must return 2, 1, and 0 for three different log fixtures.
- Unlock: complete Mission 02.
- Reward: 100 XP and 50 credits, granted once.

## Current implementation

The Android application contains a playable three-mission Chapter 1 campaign. Mission selection is sequential, completed missions can be replayed, and rewards cannot be farmed by replaying. Python code executes locally through Chaquopy / CPython 3.13. The editor provides basic syntax highlighting, line numbers, auto-indent, and insertion shortcuts. XP, credits, and completed mission flags are saved with Android SharedPreferences; the first mission's legacy save key is migrated.

## Planned completion milestones

1. Expand Chapter 1 into a complete story arc with branching dialogue, investigation results, and a chapter finale.
2. Add Python missions covering parsing, dictionaries, functions, and basic network concepts using simulated lab data.
3. Add a C++ learning chapter with a genuine compile/run toolchain; do not label a parser or mock as a real compiler.
4. Add player profile, mission map, inventory, save migration, and settings.
5. Add terminal/avatar animation, audio, onboarding, and responsive layouts.
6. Harden code execution boundaries, add automated Android UI tests, and produce a signed release APK.

## Safety boundary

The current Python runner is a constrained local teaching environment, not a hardened sandbox. It must never execute remote or hostile code. All security scenarios are fictional simulations; the game does not connect to real targets.