# Cyber Protocol

Cyber Protocol is an Android-first Hacker RPG and coding simulation. You play a junior security analyst solving fictional investigations by writing real Python code.

## Current playable foundation

- Native Android UI with Kotlin and Jetpack Compose.
- Embedded CPython 3.13 through Chaquopy.
- Split mission, target-terminal, and code-editor layout.
- Python syntax coloring, line numbers, auto-indent, and editor shortcut keys.
- Chapter 1 campaign with three playable Python investigations:
  - M01 — The Corrupted Access Log (count ERROR entries).
  - M02 — The Warning Signal (count WARNING entries).
  - M03 — The Failed Login Trail (count FAILED login entries).
- Each mission runs actual player-written Python against three different evidence fixtures.
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