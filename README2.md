# CYBER PROTOCOL — README2
## Private Design Bible / Canonical Development Reference

> **Purpose:** This document is the canonical creative and product-design reference for Cyber Protocol. Consult it before making significant changes to story, UI, gameplay, progression, learning systems, assets, or architecture.
>
> **Visibility:** This file is intentionally not linked from the repository's main README. The repository is public, so this file is also publicly accessible to anyone who knows its URL. Do not store credentials, secrets, private personal information, API keys, or security-sensitive operational details here.
>
> **Canon rule:** Preserve the decisions explicitly established below. Ideas marked as proposed or provisional may be refined, but do not silently contradict the core vision. When implementation constraints require a change, explain the trade-off and preserve the intended player experience.

---

# 1. Product Identity

**Title:** Cyber Protocol  
**Genre:** Cinematic Story RPG × Tactical Intelligence Simulator  
**Platform:** Android-first  
**Perspective:** First-person (FPP)  
**Art direction:** Cinematic, semi-realistic anime-inspired game presentation  
**Core experience:** The player lives the story of an intelligence operative and learns programming and technical skills by using them to solve problems inside that fictional world.

## 1.1 Core promise

Cyber Protocol is **not a programming course app with a game theme**.

The player should feel like a member of a sophisticated intelligence organization. Learning Python and other technical disciplines is part of becoming a capable operative. Lessons, code exercises, and evaluations must have a believable purpose within the story and the operation being performed.

The two halves must reinforce each other:

- **Cinematic Story RPG:** recruitment, characters, dialogue, narrative events, classified files, investigations, decisions, consequences, and progression through a continuing campaign.
- **Tactical Intelligence Simulator:** operational interfaces, terminals, code editor, simulated evidence, databases, network environments, agent communications, analysis, and mission objectives.

The game should move naturally between cinematic story sequences and hands-on operational gameplay.

# 2. Player Perspective and Identity

- The game uses a first-person perspective.
- The player is the operative; the player is not represented by a visible anime avatar.
- Do not make a visible player-character portrait the center of the interface.
- The player may have a callsign, profile, rank, clearance, statistics, skills, and operational history.
- The world and story are presented through first-person environmental interfaces, documents, messages, terminal screens, briefings, and cinematic transitions.
- Other human characters may have their own identities and appearances, introduced through suitable story presentation, but agent communication does not require face portraits.

# 3. Main Menu and Navigation

The first launch should open on a polished main menu, not directly into a lesson or code editor.

Core menu destinations:

1. **Run Campaign** — start or continue the main story.
2. **Profile** — callsign, rank, XP, clearance, skills, statistics, and history.
3. **Settings** — audio, display, controls, accessibility, and other game settings.
4. **Language** — selectable game language; localization should be designed into the content system.
5. **Archives** — collected dossiers, evidence, reports, unlocked documents, and case history.
6. **Achievements** — awards and milestones.

The main menu should feel like the entry point to an intelligence organization or its secure system, not a generic educational dashboard.

The main menu navigation must take inspiration from classic Java-era mobile games, especially the menu style and navigation patterns associated with Gameloft, GLU Mobile, and EA Mobile titles (for example, the old mobile Assassin's Creed games). The intended feeling is a game title/menu screen, not a modern app dashboard: clear game-style menu choices, strong visual identity, and direct, simple navigation. Adapt the interaction for modern Android touch controls while preserving that classic mobile-game character. This is specifically a direction for the main menu and navigation; do not assume every screen must imitate an old Java game's graphics or limitations.
For the current main-menu background, use a temporary dark cyan or dark blue direction. Treat this as a provisional main-menu visual choice, not a permanent global replacement for the established crimson/obsidian game palette. Keep the background restrained and readable, and let the user refine the exact shade later.


## 3.1 Main Menu Background — Cinematic A.E.G.I.S. Headquarters

The main menu background should be a cinematic view of the A.E.G.I.S. headquarters, rather than a generic abstract interface or a character portrait. The headquarters environment establishes the world and identity of the organization as soon as the game opens.

- Use a cinematic environmental composition of the A.E.G.I.S. headquarters as the main-menu backdrop.
- Keep the current provisional background direction in the dark cyan / dark blue family.
- The menu navigation must remain clearly readable and visually distinct from the background.
- The environment should feel like a believable, sophisticated intelligence headquarters and support the classic Java-era mobile-game menu experience already specified above.
- Do not make the player avatar the focal point. The scene is about the headquarters and its atmosphere.
- Exact architecture, camera angle, lighting, animation, and menu placement remain open for discussion; do not treat unapproved details as final canon.

# 4. Fictional Intelligence Organization: A.E.G.I.S.

## 4.1 Identity

**A.E.G.I.S.**  
**Advanced Espionage & Global Intelligence Service**

- Fictional, independent international intelligence organization.
- Main headquarters: Manila, Philippines.
- Fictional founding year used in the current visual concept: 2008.
- Motto: **“Knowledge is Power. Intelligence is Responsibility.”**

A.E.G.I.S. exists to identify, investigate, and respond to complex threats that cross national borders and cannot easily be addressed by one jurisdiction alone.

Its work may involve fictional cases concerning:
- Transnational corruption networks.
- Money laundering and financial crime.
- Information manipulation and disinformation.
- Digital espionage and data theft.
- Illicit technology infrastructure.
- Criminal networks exploiting technology.

A.E.G.I.S. is not omniscient or always morally correct. Its people face uncertainty, conflicting interests, difficult decisions, internal disagreements, incomplete intelligence, and secrets. Narrative complexity should come from fictional characters and organizations, not unsupported allegations about real people or entities.

## 4.2 Divisions

A.E.G.I.S. has six principal divisions:

### 01 — Intelligence Division
Collects, connects, verifies, and analyzes information. Acts as an intelligence hub for the organization.

### 02 — Cyber Operations
Handles digital security, system analysis, cyber investigations, and controlled technical-operation simulations.

### 03 — Investigation Division
Examines evidence, reconstructs timelines, identifies relationships, and develops cases.

### 04 — Technical Division
Develops and maintains devices, software, communication systems, infrastructure, and organizational technology.

### 05 — Field Operations
Coordinates fictional field assignments, teams, information gathering, logistics, and safety.

### 06 — Command
Sets strategy, authorizes operations, allocates resources, and oversees the divisions.

Access to divisions, tools, files, and operations should be earned through story progression, clearance, rank, and demonstrated competence. The player should not receive every capability immediately.

## 4.3 Headquarters

The main headquarters is in Manila. Supporting facilities may exist in other regions as the story expands.

Potential headquarters areas:
- **Central Command:** mission control and operation briefings.
- **Intelligence Room:** information analysis and reports.
- **Cyber Lab:** technical workstations and safe simulations.
- **Evidence Archive:** dossiers and collected evidence.
- **Agent Quarters:** personal and organizational spaces.
- **Training Facility:** learning and evaluation.
- **Secure Communication:** internal communications.

These locations can be represented through immersive first-person screens and navigable environments as the scope and technical design allow.

# 5. Agent Communication System — NEXUS

## 5.1 Identity

**NEXUS** is the fictional AI communication-intelligence system that accompanies the player during operations.

NEXUS is a system, not a human character.

## 5.2 Visual treatment

- Communication is text-first.
- No human face portrait.
- Use a neutral human silhouette icon, similar to a default profile placeholder when no profile picture is set.
- Messages should have clear sender identity, connection state, and optional timestamp/channel metadata.
- Incoming messages may use contextual notification animations.
- Conversation history should be available for review.
- The communication bar/panel should remain accessible during mission gameplay without obscuring essential controls.

## 5.3 Gameplay responsibilities

NEXUS can:
- Deliver mission briefings and contextual updates.
- Explain programming concepts and syntax.
- Help interpret runtime errors.
- Provide graduated hints.
- React to success, failure, discoveries, and story events.
- Warn the player about relevant conditions.
- Remember the player's in-game progress and previously demonstrated skills, where supported by the game data model.

NEXUS must guide the player's thinking, not automatically solve every mission. Hints should be useful without removing the player's agency. The system should feel like a competent operational partner, not a decorative chatbot.

# 6. Campaign and Narrative

## 6.1 First campaign

**Campaign 01: THE RECRUITMENT**  
**Chapter 001: FIRST CONTACT**

The player begins as an ordinary person. An encrypted message unexpectedly appears on the player's device. The sender is unknown. The message indicates that the player's abilities have attracted the attention of an organization.

The opening should be cinematic and paced. Do not immediately throw the player into a programming lesson.

Current story beats:

1. **Unknown Sender** — the player receives a mysterious encrypted message.
2. **The Invitation** — the player receives a recruitment invitation.
3. **The Organization** — A.E.G.I.S. and its purpose are introduced.
4. **Classified File 001** — the player opens the first classified dossier.
5. **First Assignment** — the player performs a simple first Python task.
6. **System Verification** — the system checks the player's demonstrated ability.
7. **Official Recruitment** — the player is formally accepted as a Recruit.

These beats are the initial narrative outline and can be expanded into dialogue, environmental storytelling, documents, choices, and cinematic transitions.

## 6.2 Narrative principles

- Story must not feel pasted onto unrelated coding questions.
- Every mission should have a believable narrative reason to exist.
- Documents, evidence, messages, and operational objectives should advance the case or reveal the world.
- Characters and agents should have consistent identities, motives, and voices.
- Choices may influence relationships, access, available information, or later events when appropriate.
- Failure should produce meaningful feedback and learning opportunities, not merely a generic “wrong” screen.
- Maintain continuity across chapters and preserve prior player decisions when the story system supports them.

# 7. Core Gameplay Loop

The intended mission cycle:

1. **Story / cinematic event** — a new development occurs.
2. **Briefing** — an agent or system explains the operational context.
3. **Classified dossier** — the player reads files, evidence, and objectives.
4. **Operation interface** — the player uses the relevant simulated tool, terminal, editor, database, or evidence interface.
5. **Agent communication** — NEXUS and other agents provide contextual messages and hints.
6. **Run / experiment** — the player can execute code and inspect output or errors.
7. **Submit / mission check** — a separate evaluator checks whether mission requirements and intended concepts are satisfied.
8. **Debriefing** — the player receives an explanation of results and story consequences.
9. **Progression** — XP, rank progress, clearance, rewards, skills, or access may be granted.
10. **Next operation** — the campaign continues with new challenges.

**Run and Submit must remain separate actions.** Run executes and displays output/errors. Submit evaluates mission requirements. Running code alone must not silently mark a mission complete.

# 8. Learning Through Operations

Programming is an in-world tool. Introduce concepts gradually and tie each skill to an operational purpose.

Potential progression:

| Learning area | Example concepts | Possible fictional use |
|---|---|---|
| Python fundamentals | `print()`, text, variables, types | System messages and basic records |
| Input and logic | `input()`, comparisons, `if/else` | Interactive verification and decisions |
| Iteration | loops, lists, indexing | Reviewing batches of records |
| Functions and data | functions, strings, dictionaries | Reusable analysis procedures |
| Data processing | filtering, sorting, structured data | Intelligence analysis |
| SQL | queries and relational data | Searching a simulated case database |
| Networking | addressing, protocols, request/response concepts | Understanding simulated infrastructure |
| C++ | types, functions, memory and embedded foundations | Technical Division and device systems |
| IoT | sensors, microcontrollers, signals | Fictional device telemetry and monitoring |
| Automation | Python, JavaScript, Bash concepts | Repetitive authorized workflow automation |

This is a candidate learning map, not a requirement to teach every topic in a fixed order. The campaign should determine when a concept is introduced. Do not overload early missions.

## 8.1 Teaching and evaluation principles

- LEARN explains concepts, syntax, examples, and expected behavior.
- PRACTICE allows safe experimentation.
- MISSION applies the skill to a story-driven objective.
- Hints should be graduated: first explain the relevant concept, then syntax/structure, then a more specific clue if needed.
- Evaluators should check actual required concept use where appropriate, not only whether output text matches.
- Do not give the complete solution by default.
- Explain errors in accessible language and connect them to the concept being learned.
- Keep lessons and mission requirements consistent and testable.

# 9. Player Progression

The current proposed rank sequence:

1. Civilian
2. Recruit
3. Initiate
4. Junior Analyst
5. Field Analyst
6. Cyber Operative
7. Senior Operative
8. Special Agent
9. Elite Operative

Progression should represent more than a numerical level. Rank and clearance may unlock:
- New divisions and operational areas.
- More complex dossiers and cases.
- New tools and simulated environments.
- New learning tracks and technical skills.
- New characters, story branches, and responsibilities.
- Achievements, records, and profile distinctions.

XP, skill development, rank, clearance, mission completion, achievements, and story flags should be coherent and persistent. Rewards must not be accidentally granted repeatedly when loading or replaying a mission.

# 10. Visual and Audio Direction

## 10.1 Art direction

- Cinematic, artistic, semi-realistic anime-inspired presentation.
- Mature futuristic intelligence atmosphere.
- Strong environmental storytelling and deliberate cinematic composition.
- First-person immersion.
- High-quality dossier, terminal, communication, and command interfaces.
- Avoid the appearance of a generic course app or generic admin dashboard.

## 10.2 Core palette

| Token | Color | Use |
|---|---|---|
| Obsidian | `#090A0E` | Primary dark background |
| Charcoal | `#15171D` | Panels and surfaces |
| Crimson | `#D62839` | Primary emphasis and alerts |
| Blood Red | `#8E1525` | Deep accents |
| Metallic Silver | `#858A96` | Secondary information and hardware feel |
| Soft White | `#F1F1F3` | Main readable text |

The existing launcher icon may have its own Neo Dark Cyan background treatment; that does not change the core in-game crimson/obsidian visual identity.

## 10.3 Visual rules

- Use strong hierarchy, readable text, and consistent spacing.
- Use red deliberately for emphasis, warnings, and organization identity; avoid flooding every screen with neon.
- Distinguish story scenes, operational screens, and ordinary settings while keeping them within one coherent design system.
- Use assets according to their semantic role; filenames are hints, not unquestionable instructions.
- Avoid unnecessary decorative clutter and generic stock-like visuals.
- Motion and sound should reinforce notifications, transitions, secure communications, mission success, and tension without harming usability or accessibility.

# 11. Safety and Fictional Scope

- A.E.G.I.S., its members, cases, organizations, companies, and targets are fictional unless a real-world educational reference is clearly and safely contextualized.
- Do not invent allegations about real people or real organizations.
- Technical missions should use fictional data, controlled simulations, toy environments, or systems the player is explicitly authorized to use.
- Do not turn story missions into instructions for unauthorized access, credential theft, malware deployment, surveillance, or attacks on real systems.
- Teach defensive, analytical, and authorized technical skills.
- Keep code execution sandboxed with appropriate validation, resource limits, and isolation for the Android environment.

# 12. Technical and Product Architecture Direction

The game should remain modular. The conceptual modules include:

- **UI / Presentation:** menus, first-person interfaces, communication, dialogs, settings.
- **Story Engine:** chapters, dialogue, events, branching, story flags.
- **Campaign Engine:** chapter and mission sequencing.
- **Learning Engine:** explanations, examples, and concept metadata.
- **Practice Engine:** experimentation workflows.
- **Mission Engine:** objectives, requirements, prerequisites, and state.
- **Code Runner:** safe execution and output/error handling.
- **Evaluator:** test fixtures and concept-aware requirement checks.
- **Progression:** XP, rank, clearance, unlocks, achievements.
- **Player Data:** profile, preferences, progress, and history.
- **Archives / Evidence:** dossiers, collected evidence, reports, and case records.
- **Communication System:** NEXUS and agent message history.

Prefer data-driven story and mission definitions where appropriate. Keep content, presentation, evaluation logic, and persistence responsibilities separate. Avoid tightly coupling all gameplay to one large Compose screen.

# 13. Existing Project Continuity

Repository: `MazBayID/firstGame`  
Current application identity from the established project: Kotlin + Jetpack Compose, Android-first, with Python integration through Chaquopy.

Existing work may include:
- Modular domain models and repositories.
- Basic Story and Learning engines.
- Python execution service.
- Compose UI for learning, practice, mission board, editor, and terminal.
- Chapter 01 content.
- Separate Run and Submit behavior.
- Evaluator and test fixtures.
- Settings and language foundations.
- Mobile-friendly code editor.
- Visual design tokens and reusable UI components.
- A.E.G.I.S.-compatible asset library and launcher icon integration.

This is a continuity note, not a guarantee that every item remains present in the current branch. **Always inspect the current repository before editing.** Preserve useful, working foundations, but refactor or replace parts that prevent the intended game experience. Do not assume old workflow/build results still apply to newer commits.

# 14. Development Rules — Do Not Drift

Before each significant development task:

1. Read this README2 design bible.
2. Inspect the current repository and relevant source files.
3. Identify how the requested change supports the first-person RPG + tactical simulator vision.
4. Preserve established story, terminology, visual rules, and player experience.
5. Prefer coherent batches of related improvements over disconnected cosmetic additions.
6. Do not add features merely to increase feature counts.
7. Do not build a generic dashboard when the task calls for a game experience.
8. Do not expose the player as a visible avatar or add agent face portraits to the communication UI.
9. Keep NEXUS as a text-first AI system with a neutral human-silhouette profile icon.
10. Keep Run separate from Submit/evaluation.
11. Do not make programming lessons feel disconnected from the fictional operation.
12. Keep all technical targets fictional or explicitly authorized and simulated.
13. Test changes where feasible; distinguish tests actually run from tests not run.
14. Verify Android build/workflow status before claiming build success.
15. Never force-push. Respect repository history and stop to resolve conflicts safely.
16. Update this document when the user explicitly changes an established design decision.

## 14.1 Decision hierarchy

When requirements conflict, use this order:

1. Explicit latest direction from the user.
2. Core product promise and established canon in this document.
3. Existing tested behavior and compatibility requirements.
4. Technical convenience.

If a proposed change would materially alter the core experience, explain the impact before implementing it.

# 15. Open Design Work

These areas need further design and validation; do not invent them as settled canon:

- Full GDD with chapter-by-chapter campaign and detailed narrative scripts.
- Named human characters, their histories, motivations, relationships, and dialogue.
- Detailed A.E.G.I.S. leadership and internal politics.
- Exact mission content, evidence chains, and branching consequences.
- Final menu and operational UI wireframes.
- Localization workflow and complete translated content.
- NEXUS dialogue model, context rules, and offline/online implementation boundaries.
- Detailed rank thresholds, XP economy, clearance model, and replay rules.
- Final technical architecture and Android performance strategy.
- Audio direction, accessibility, and cinematic asset pipeline.

Develop these systematically. Mark proposals as proposals until approved or clearly established by the user.

---

**North Star:** Make the player feel that they are living inside a cinematic intelligence operation, learning and applying real technical concepts as an operative of A.E.G.I.S. Every screen, story beat, mission, and system should serve that experience.
