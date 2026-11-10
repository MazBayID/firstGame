# Cyber Protocol

Cyber Protocol is an Android-first Hacker RPG and coding simulation. You play a junior security analyst solving fictional investigations by writing real Python code.

## Current playable foundation

- Native Android UI with Kotlin and Jetpack Compose.
- Embedded CPython 3.13 through Chaquopy.
- Split mission, target-terminal, and code-editor layout.
- Python syntax coloring, line numbers, auto-indent, and editor shortcut keys.
- Chapter 1 briefing: The Corrupted Access Log.
- Executable Python puzzle reading a virtual access.log.
- Mission validation against multiple log fixtures, so hardcoded output does not pass.
- XP, credits, and mission completion saved locally.
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

This is an actively developed playable foundation, not yet the complete planned game. Upcoming milestones include chapter progression, more Python missions, C++ learning missions, save slots, richer dialogue, sound and animation, and a stronger isolated code-execution architecture.

## Safety

All targets and evidence are fictional and offline. The embedded Python mission runner is a constrained teaching environment, not a hardened sandbox. It must not execute remote or hostile code.

## License

MIT. See [LICENSE](LICENSE).