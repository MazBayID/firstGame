# Cyber Protocol

**Cyber Protocol** is an Android-first hacker RPG and real coding simulator. Players solve fictional security investigations by writing and executing real Python, with C++ missions planned for later chapters.

## Project status

- Version: `0.1.0` foundation
- Platform: Android
- UI: Kotlin + Jetpack Compose
- Python runtime: Chaquopy
- Build: GitHub Actions

## Current milestone

The first milestone establishes the Android application, a split terminal/editor screen, a constrained virtual-file Python exercise runner, Chapter 1 content, and an automated debug APK workflow.

## Safety model

The initial Python runner is an educational, in-process execution environment with a restricted set of built-ins, a virtual `access.log`, AST checks, and an instruction budget. It is **not** a hardened sandbox for hostile code. Do not expose it to untrusted remote submissions. Strong isolation is required before adding arbitrary networking, native code, or multiplayer submissions.

## Build

Open this project in Android Studio or run `gradle assembleDebug` in an environment with JDK 17 and Android SDK 36 installed. GitHub Actions builds a debug APK on pushes and pull requests.

See [BUILD_GUIDE.md](docs/BUILD_GUIDE.md) and [ARCHITECTURE.md](docs/ARCHITECTURE.md).
