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

## Hybrid Learning loop

Every mission follows the same player-centered sequence:

1. **Briefing:** establish the story objective without assuming coding knowledge.
2. **Learning:** explain the new Python concept in plain language.
3. **Demonstration:** show a short working example and its output.
4. **Experiment:** invite the player to change one part and observe the result.
5. **Practice:** provide a starter program and a focused hint.
6. **Mission:** let the player complete the task in the Python editor.
7. **Evaluation:** explain the result and validate the required behavior.

The first chapter teaches Python itself. It does not require file I/O, network knowledge, or security-analysis algorithms.

## Chapter 01 — Signal in the Noise

The player joins Helix Dynamics as a junior analyst. Mara introduces the terminal and gradually teaches the player to communicate with it, store values, calculate, make decisions, and process a small collection of simulated events.

| Mission | Learning objective | Required knowledge |
|---|---|---|
| M01 — First Contact | Print the first message | `print()`, strings |
| M02 — Signal Online | Display multiple lines and add comments | `print()`, comments |
| M03 — Analyst Identity | Store and display analyst details | Variables, strings, assignment |
| M04 — Power Calibration | Calculate a power value | Integers, arithmetic operators |
| M05 — Repair the Shield | Calculate remaining HP | Variables, arithmetic |
| M06 — Power Decision | Choose a system state from energy | `if`, `else`, comparisons |
| M07 — Signal Sweep | Repeat four scan steps | `for`, `range()` |
| M08 — Threat Triage | Count matching events in a list | Lists, iteration, conditions, counters |

### Difficulty and evaluation rules

- M01–M05 are output-based learning tasks. Exact requested output is a valid solution; constant output is not incorrectly classified as cheating.
- M06 uses multiple energy fixtures (positive, zero, and negative) so the condition must work for changing values.
- M07 teaches repetition with a bounded sequence.
- M08 supplies different event lists in each test, requiring a real filter and counter rather than a memorized answer.
- File reading and access-log analysis are deferred until the next chapter, after the player understands the required Python foundations.
- Rewards are granted once per mission; completed missions remain replayable for practice.

## Current implementation

The Android application contains a playable eight-mission Chapter 1 campaign. Mission selection is sequential, completed missions can be replayed, and rewards cannot be farmed by replaying. Python code executes locally through Chaquopy / CPython 3.13. The editor provides basic syntax highlighting, line numbers, auto-indent, and insertion shortcuts. XP, credits, and completed mission flags are saved with Android SharedPreferences; the first mission's legacy save key is migrated.

## Planned completion milestones

1. Expand Chapter 1 into a complete story arc with branching dialogue, investigation results, and a chapter finale.
2. Add Python missions covering parsing, dictionaries, functions, and basic network concepts using simulated lab data.
3. Add a C++ learning chapter with a genuine compile/run toolchain; do not label a parser or mock as a real compiler.
4. Add player profile, mission map, inventory, save migration, and settings.
5. Add terminal/avatar animation, audio, onboarding, and responsive layouts.
6. Harden code execution boundaries, add automated Android UI tests, and produce a signed release APK.

## Safety boundary

The current Python runner is a constrained local teaching environment, not a hardened sandbox. It must never execute remote or hostile code. All security scenarios are fictional simulations; the game does not connect to real targets.