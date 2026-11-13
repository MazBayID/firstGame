# CYBER PROTOCOL

**A story-driven cyber-intelligence adventure RPG for Android.**

Cyber Protocol follows an ordinary person recruited into **A.E.G.I.S.**, a fictional independent intelligence organization headquartered in Manila. The player enters a world of investigations, digital evidence, technical simulations, and difficult decisions—while learning how technology works through the situations they encounter.

Programming is part of the story and gameplay. It is not a separate coding course disguised as a game.

## The story

The first campaign, **THE RECRUITMENT**, begins when the player receives an unexpected invitation from A.E.G.I.S. The organization offers no immediate explanation for why the player was selected.

**Freya Knox** becomes the player's human mentor, guiding them through their first assignments and introducing the organization's tools. **NEXUS**, A.E.G.I.S.'s AI support system, provides technical information and system assistance. Freya and NEXUS have different roles: one is a personal mentor, the other an intelligence system.

The opening chapter, **FIRST CONTACT**, takes the player from an unfamiliar recruit to an operative capable of completing basic technical tasks and examining an intelligence case.

Its central case, **RX-14**, begins with two records for the same transmission. One records the event at 09:41:12; another records it at 09:41:16. The four-second difference is observable, but its cause is not established. The player must distinguish verified facts from hypotheses and avoid claiming more than the evidence supports.

The game’s organizations, people, incidents, and datasets are fictional.

## How it plays

Cyber Protocol is a **Guided Intelligence RPG**. Each assignment gives the player a situation, an objective, and enough context to make informed progress, while leaving room to investigate and decide.

A typical case may involve:

1. **Briefing** — understand the assignment and what is still unknown.
2. **Investigation** — inspect original records and collect relevant information.
3. **Analysis** — compare sources, test explanations, and separate facts from assumptions.
4. **Operational decision** — choose a justified action or request more verification.
5. **Debrief** — review the result and advance the campaign.

These are stages of an investigation, not a mandatory sequence of apps. The player uses whichever tools are relevant to the task. Not every mission requires programming.

## A.E.G.I.S. tools

The player's in-game work environment includes connected applications:

- **Secure Messenger** — conversations, mission instructions, and attachments from A.E.G.I.S. personnel.
- **A.E.G.I.S. Browser** — an offline intranet with Mission Control, File Repository, Intelligence Reports, and personnel information.
- **Virtual Terminal** — a Linux-like command interface for working with files inside the game's virtual workspace.
- **Python Code Editor** — write and run mission scripts, then submit the exact successfully executed source for evaluation.
- **Operations and Analysis** — track case phases, inspect evidence, record conclusions, and make operational decisions.
- **Intelligence Database and Personnel Dossier** — review case records, evidence relationships, operative progress, and history.
- **Archives, Profile, Achievements, and Training Simulator** — review progress and revisit optional coding practice.

These tools share campaign and case state. Reading a source in one relevant application can update the same investigation progress seen by the others.

## Learning through play

The opening assignments teach practical programming foundations in context. The player learns to work with files and a terminal, understand simple Python variables and output, and use a program to process information.

The two introductory Python exercises are required for the recruitment journey. Additional coding exercises are available as optional practice in the Training Simulator; they do not block the story or the RX-14 investigation.

The game keeps **RUN** and **SUBMIT** separate: running a script executes it in the mission workspace, while submitting asks the mission evaluator to assess the successfully run source.

## Design principles

- **Evidence before certainty.** Sources, provenance, observations, hypotheses, and unknowns must remain distinguishable.
- **Learning with a purpose.** Technical tasks appear when they make sense for the story or investigation.
- **Connected gameplay.** Apps use shared case state rather than separate copies of progress.
- **Player progress matters.** Campaign checkpoints, mission workspaces, evidence, decisions, and rewards are persisted locally.
- **Offline-first.** Core story, tools, Python missions, evaluation, and progression do not require an account, remote server, external AI service, or API key.
- **Safe simulation boundaries.** Terminal commands operate only on the game's virtual filesystem and never execute commands on the Android host.

The current Python runner applies AST restrictions and resource limits, but runs in-process. It is not a hardened security sandbox for hostile code.

## Current development focus

The active priority is to make **FIRST CONTACT** a coherent, complete player experience—from a fresh save and recruitment through the required onboarding, the RX-14 investigation, and its chapter debrief.

The project already contains the core campaign structure, connected A.E.G.I.S. applications, two required Python onboarding exercises, the RX-14 case flow, persistence, and automated source-level tests. The complete player journey and physical-device acceptance still require verification. Future campaign ideas are not a substitute for finishing the active chapter.

The goal is a game that works from beginning to end—not a collection of screens, unfinished systems, or documentation.

## Technology

- Android application built with Kotlin and Jetpack Compose.
- Local Python execution through CPython 3.13 and Chaquopy.
- Local persistence and game-managed virtual workspaces.
- English-first content with Bahasa Indonesia support under incremental development.

## Build and test

Local development currently expects JDK 17, Android SDK Platform 36, Build Tools 36.0.0, Gradle 8.13, and Python 3.13.

Run the source test suites:

```bash
python -m unittest discover -s tests -v
gradle --no-daemon testReleaseUnitTest
```

Debug Preview APKs can be built at useful review checkpoints. Final release packaging and release acceptance are separate steps and require explicit approval.

---

**Cyber Protocol is about learning to investigate before deciding what is true.**
