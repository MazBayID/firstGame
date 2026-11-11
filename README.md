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

## Story

You are not a trained operative. You are an ordinary person whose device receives a message from an unknown sender—one containing information that should never have reached you.

The sender claims to represent **A.E.G.I.S.**, an independent intelligence organization headquartered in Manila. They have been following a trail of anomalies: records that do not match, financial traces that disappear, and systems whose answers seem correct until they are compared.

Your first task is not to save the world. It is to answer a simple question: **why were you contacted?**

As you enter A.E.G.I.S., a primary mentor guides your recruitment and development. NEXUS, the organization's AI communication system, keeps you connected, while specialists step in when an operation demands their expertise. You will examine dossiers, question evidence, uncover connections, and use programming as a practical tool—not as a detached classroom exercise.

Every case reveals another fragment. Every mission leaves a clue. And the deeper you investigate, the harder it becomes to tell whether the information in front of you is incomplete by accident—or arranged that way.

**Cyber Protocol is an intelligence RPG, not a sword-and-sorcery adventure.** Your growth comes from technical skill, investigation, trust, judgment, relationships, and the consequences of your decisions.

*The message was meant for someone. The question is why it found you.*

### Story and design references

- [Story Line — Narrative Blueprint](Story_Line.md): campaign direction, story mysteries, character roles, mission structure, and continuity rules.
- [Game Concept — Design Bible](GAME_CONCEPT.md): canonical product vision, gameplay, UI, learning, progression, and development principles.

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
