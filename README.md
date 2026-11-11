# CYBER PROTOCOL

**Cyber Intelligence RPG · Educational Programming Game**

Cyber Protocol is an Android-first, story-driven programming RPG. The player begins as an ordinary person and is gradually recruited into a fictional intelligence organization headquartered in the Philippines. Through fictional investigations across Southeast Asia, the player learns programming by writing, running, debugging, and submitting real code.

Programming is core gameplay—not a quiz attached to a story.

## Design pillars
- **Story with consequences:** fictional cases connect into a larger transnational network.
- **Learn by doing:** LEARN explains, PRACTICE invites experimentation, and MISSION asks players to solve story-driven coding challenges.
- **Real execution:** Python code runs locally in the training environment.
- **Meaningful evaluation:** RUN only executes; SUBMIT checks mission requirements and programming concepts.
- **Progressive growth:** technical competence, organizational trust, and character relationships.
- **Expandable architecture:** Python is the first language, not a permanent architectural limit.
- **Safety first:** fictional simulations only. The embedded runner is a teaching prototype, not a hardened hostile-code sandbox.

## Visual foundation
- Cinematic Red Intelligence art direction: Obsidian / Charcoal / Crimson palette with anime character artwork direction.
- Material 3 semantic color tokens and reusable Compose UI components.
- Vector emblem and navigation icon starter set; runtime asset pipeline documented in docs/ASSET_PIPELINE.md.
- Visual rules documented in docs/VISUAL_IDENTITY.md.

## Reconstructed foundation
- Kotlin, Jetpack Compose, Material 3, Android-first.
- CPython 3.13 through Chaquopy.
- Separate models, repositories, domain rules, execution adapter, and UI.
- LEARN, PRACTICE, MISSION entry points.
- Separate Run and Submit actions.
- Chapter 01 content loaded from JSON assets.
- Local progress and one-time mission rewards.
- Python unit tests and GitHub Actions Android build.

## Build
Requirements: JDK 17, Android SDK Platform 36, Build Tools 36.0.0, Gradle 8.13, Python 3.13.

```bash
python -m unittest discover -s tests -v
gradle --no-daemon assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.

See [docs/PROJECT_BIBLE.md](docs/PROJECT_BIBLE.md) for design, story, architecture, security, and roadmap. The current scope is a modular playable foundation—not the complete v1.0 campaign.
