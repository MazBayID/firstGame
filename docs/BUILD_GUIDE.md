# Build guide

## Requirements

- JDK 17
- Android SDK Platform 36 and Build Tools 36.0.0
- Gradle 8.13
- Android device/emulator with API 24 or newer

## Local build

From the repository root:

```bash
gradle --version
gradle test
gradle assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## GitHub Actions

Every push or pull request to `main` runs the Python mission-runner tests and builds a debug APK. Open the repository's **Actions** tab, select **Android Debug APK**, then download the `cyber-protocol-debug` artifact from a successful run.

## Release signing

The current release workflow deliberately produces an **unsigned** release APK. Before public distribution, configure a private keystore and GitHub Actions secrets, then sign and verify the release artifact. Never commit keystore files or passwords.
