# Cyber Protocol

Cyber Protocol is an Android-first Hacker RPG and coding simulation. You play a junior security analyst solving fictional investigations by writing real Python code.

## Current playable foundation

- Native Android UI with Kotlin and Jetpack Compose.
- Embedded CPython 3.13 through Chaquopy.
- Main menu before entering the campaign, with a dedicated settings screen.
- Indonesian and English game language selection, saved locally on the device.
- Split mission, target-terminal, and code-editor layout.
- On-screen cursor navigation controls (left/right/up/down and line start/end) for touch editing.
- Python syntax coloring, line numbers, auto-indent, and editor shortcut keys.
- Hybrid Learning flow: story briefing → concept lesson → worked demonstration → safe experimentation → code practice → mission evaluation.
- Chapter 01 contains eight progressive Python missions, designed for players with no coding experience:
  - M01 — First Contact: `print()` and first output.
  - M02 — Signal Online: multiple output lines and comments.
  - M03 — Analyst Identity: variables and strings.
  - M04 — Power Calibration: numbers and arithmetic operators.
  - M05 — Repair the Shield: variables combined with arithmetic.
  - M06 — Power Decision: `if`, `else`, and comparisons with changing test values.
  - M07 — Signal Sweep: `for` loops and `range()`.
  - M08 — Threat Triage: lists, iteration, filtering, and counters.
- Beginner output missions are checked against their requested output; behavior-based missions use changing fixtures to verify that the player's logic works beyond one fixed answer.
- Every mission includes a concept explanation, a worked example, an experiment prompt, a starter program, and progressive practice feedback.
- File reading and access-log investigations are intentionally moved to the next learning stage after Chapter 01 foundations.
- Mission unlocks are sequential; completed missions can be replayed without farming rewards.
- XP, credits, unlocked/completed missions, and legacy M01 progress are saved locally.
- GitHub Actions builds a debug APK and uploads it as an artifact.

## Build

Requirements: JDK 17, Android SDK Platform 36, Gradle 8.13, and Chaquopy's Python 3.13 runtime.

Run Python tests with: `python -m unittest discover -s tests -v`

Build the debug APK with: `gradle --no-daemon assembleDebug`

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

See [docs/BUILD_GUIDE.md](docs/BUILD_GUIDE.md) for CI and release details.

## Download a CI build

Open [GitHub Actions](https://github.com/MazBayID/firstGame/actions), choose a successful Android Debug APK run, and download its `cyber-protocol-debug` artifact.

## Project status

This is an actively developed playable foundation, not yet the complete planned game. Upcoming milestones include a fuller chapter narrative with branching dialogue, Python missions for dictionaries/functions/simulated network data, genuine C++ compile/run integration, player profile and inventory, settings, sound and animation, automated Android UI tests, and a stronger isolated code-execution architecture.

## Safety

All targets and evidence are fictional and offline. The embedded Python mission runner is a constrained teaching environment, not a hardened sandbox. It must not execute remote or hostile code.

## License

MIT. See [LICENSE](LICENSE).