# CYBER PROTOCOL — GAME CONCEPT
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

## 1.2 What “RPG” Means in Cyber Protocol

Cyber Protocol uses RPG storytelling and progression, but it is **not a conventional fantasy/action RPG built around sword combat or fighting enemies**.

- The player's role is an intelligence operative. The central activities are investigation, analysis, communication, technical problem-solving, operational decisions, and story-driven missions.
- RPG depth comes from character relationships, dialogue, choices, consequences, skills, rank, clearance, and the player's evolving place within A.E.G.I.S.
- Tension should come primarily from mysteries, intelligence risks, difficult decisions, conflicting motives, time-sensitive operations, and narrative consequences—not from turning the game into a sword-fighting or generic action game.
- Any physical action or confrontation, if ever included, must serve the intelligence-story context and should not displace the core investigative and technical experience.

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
- The headquarters is envisioned as a **closed, mysterious intelligence complex**, not an ordinary office tower. Its architecture and atmosphere should communicate secrecy, restricted access, and a sophisticated hidden organization.
- Do not make the player avatar the focal point. The scene is about the headquarters and its atmosphere.
- Exact architecture, camera angle, lighting, animation, and menu placement remain open for discussion; do not treat unapproved details as final canon.


## 3.2 A.E.G.I.S. Operative Application Hub

The operational interface should provide a compact, elegant way to access the player's in-game tools without turning the experience into a generic dashboard.

- Place a small application-hub control in the **upper-right corner** of the relevant operational interface.
- Selecting it opens a **smartphone-style application panel** containing the operative's available in-game applications. This is the approved hybrid approach: a discreet top-right entry point with a familiar app-panel experience.
- The application panel should feel like a secure A.E.G.I.S. device interface and remain visually consistent with the game's cinematic intelligence atmosphere.
- The initial application set consists of:
  1. **Secure Messenger** — communication with NEXUS and other agents.
  2. **Terminal** — the player's coding and command-line workspace for supported learning and mission tasks.
  3. **Intelligence Database** — access to authorized records and structured intelligence data.
  4. **Dossier** — mission briefings, classified documents, evidence, and case files.
  5. **Analysis** — tools for examining evidence, connecting clues, and organizing findings.
  6. **Operations** — current objectives, mission status, and operational progress.
- Do not expose every application or capability from the beginning. Availability may expand through campaign progress, rank, clearance, and story events.
- Opening an application should present its relevant focused workspace. Closing it should return the player to the previous operation or screen without losing the current context.
- Keep the hub focused and purposeful. Additional applications may be considered later, but should only be added when they serve a clear gameplay or narrative need; do not add icons merely to make the panel look busy.
- Exact icon design, panel layout, transitions, and application-specific interactions remain open for later design.


## 3.3 Character Dialogue — Chat Interface

Character conversations are presented through an in-game **chat interface**, rather than a visual-novel dialogue box as the default dialogue format.

- The player communicates with Freya, NEXUS, and other characters through a secure A.E.G.I.S.-style messaging experience.
- The Messenger opens to a **contact list**. The player selects Freya, NEXUS, or another available agent to open that contact's conversation; do not use a single shared chat as the default landing view.
- Contacts and conversations may become available progressively as characters are introduced and the player's access grows.
- Returning from a conversation should take the player back to the contact list, while closing Messenger should return the player to the previous game screen without losing operation context.
- Messages appear as distinct chat bubbles with clear sender identity. Player replies and character messages must be visually distinguishable.
- Player replies use **predefined response options** in the initial version. The player selects one of the authored messages; free-text replies are not part of the initial implementation.
- Initial dialogue choices are **cosmetic**: they select which authored response the player sends and may change the immediate conversational reply, but they do not alter relationship values, unlock content, change mission access, or branch the main story. Do not implement a relationship meter or persistent dialogue-choice flags for this initial version.
- This keeps the conversation system deterministic, testable, and straightforward to implement. Any later move toward consequential dialogue choices requires a separate design decision.
- Conversation history should remain reviewable, and new messages or important updates may use restrained unread indicators or notifications.
- The interface should support both ordinary conversation and operational communication, while keeping sender identity and context clear.
- Dialogue presentation must remain readable and usable on Android screens. Avoid clutter, excessive decoration, and unnecessary chat animations.
- This is the approved default for character dialogue. Do not assume visual-novel character portraits or dialogue boxes are required. Other story presentation methods may be considered separately when a scene genuinely needs them.
- The dialogue architecture may later support optional AI-assisted conversation through an external API such as the OpenAI API. This is a future possibility, not a current feature or dependency. Keep authored story progression, mission state, relationship flags, and consequential choices under the game's control; do not require a live AI service for the core campaign to function.
- Detailed chat layout, message timing, typing indicators, attachments, and interaction behavior remain open for later UI design.


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


## 5.4 Primary Mentor and Specialist Handoffs

The player should have **one primary human mentor** who accompanies them from the beginning of recruitment and serves as their main point of contact throughout the RPG experience.

- The primary mentor provides ongoing guidance, follows the player's development, and helps maintain continuity across the campaign. In the initial version, relationship development is conveyed through authored story scenes and dialogue rather than a player-controlled relationship meter or persistent consequences from cosmetic reply selections.
- The primary mentor is **Freya Knox**, a woman. Her approved broad appearance direction is **youthful and energetic**. Exact age, facial features, hairstyle, clothing, accessories, detailed history, and exact expertise remain open until separately designed and approved.
- Her approved personality direction takes inspiration from **Power from Chainsaw Man**, without copying that character. She is highly intelligent, confident to the point of ridiculousness, boastful, mischievous, impulsive in her humor, expressive, and wonderfully goofy.
- She may brag about her own brilliance, make outrageous claims, tease the player, act as though she is obviously the greatest mentor in A.E.G.I.S., or behave in an unexpectedly childish way. This is playful characterization, not evidence of incompetence.
- Humor is part of her teaching method, not merely comic relief. She uses funny analogies, absurd examples, playful challenges, exaggerated reactions, and mischievous remarks to make difficult concepts easier to understand and help the player feel comfortable asking questions.
- Her teasing must remain affectionate rather than humiliating. She does not shame the player for mistakes, trivialize victims, dismiss sensitive evidence, or make genuine danger seem harmless.
- She never uses humor to belittle the player, shame mistakes, trivialize victims, dismiss sensitive evidence, or make genuine danger seem harmless. Mistakes can become lighthearted learning moments without making the player feel stupid.
- She is genuinely competent and highly capable. Her goofy behavior must not imply poor judgment, ignorance, or carelessness. When an operation becomes genuinely dangerous or urgent, she can immediately become focused, decisive, and professional.
- At first contact, she presents a composed, professional manner and establishes the seriousness of the situation. Once the player begins to know her, her goofy side becomes increasingly visible—including during serious teaching moments. The initial credibility-building first impression remains important; do not make her instantly overfamiliar in the first briefing.
- The mentor appears to make an outrageous recruitment choice by selecting an ordinary person with no programming background for an urgent situation. This is intentional: the mentor has already reviewed information about the player and identified potential that the player has not yet recognized.
- The mentor's assessment of the player must have a credible in-story basis. The exact source, scope, and method of the mentor's player data—and why the mentor trusts that potential—are deliberate mysteries to reveal gradually. Do not imply unlimited surveillance or expose private real-world user data; this is fictional player-character backstory.
- The player should not feel as though they are being passed randomly between unrelated guides. The primary mentor remains the familiar central figure.
- When an operation requires expertise outside the primary mentor's field, the mentor should arrange a clear handoff in the communication interface, introducing or connecting the player with an appropriate specialist.
- The specialist temporarily leads the conversation for that specific subject or task and provides domain-specific guidance. For example, a technical specialist may assist with device or embedded-system work, while an intelligence analyst may help with evidence interpretation.
- The handoff should feel like a natural RPG communication event: the primary mentor explains why another expert is needed, then the specialist joins the conversation with clear identity and role.
- Specialist assistance should complement the primary mentor rather than replace the ongoing mentor relationship. The primary mentor can remain aware of the player's progress and resume the lead when appropriate.
- Specialist characters, their names, personalities, expertise, and exact handoff mechanics remain to be designed.


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

## 6.2 Campaign Experience and Navigation

The Campaign uses a **hybrid story-driven + mission-driven structure**. Cinematic narrative is the primary experience, while a mission/operation selection interface provides a clear sense of player progress and available assignments.

- Entering Campaign should feel like entering an intelligence operation, not opening a lesson list.
- Use a deliberate transition from the main menu into the campaign experience, with A.E.G.I.S. system/secure-connection presentation where appropriate.
- Present the campaign title, current operation status, and story progress.
- Offer **Continue** and **New Campaign** when applicable.
- Do not expose every chapter and mission immediately. Reveal chapters and operations progressively as the story advances and prerequisites are met.
- Each chapter may have its own title, cinematic artwork, short synopsis, and completion/availability state.
- Story scenes, dialogue, encrypted messages, briefings, and discoveries should lead naturally into operation selection or the next mission.
- The player may choose among operations that are already unlocked, but the main narrative should retain a deliberate sequence and pacing.
- The first campaign begins with **FIRST CONTACT** and the established recruitment sequence; do not skip directly to a mission list or coding lesson.

## 6.3 First Contact — Progressive Message Decryption

The opening recruitment story should introduce decryption as part of the narrative and as the player's first practical learning experience.

- The first incoming message should be presented as an encrypted/obscured transmission, creating curiosity without demanding advanced knowledge.
- NEXUS (or the appropriate guide in the story) teaches the player a very simple first step, tied to a beginner Python concept such as storing and displaying a message with a variable and `print()`.
- The first interaction is guided: the player learns the basic tool or syntax before being asked to solve anything independently.
- A subsequent message becomes a beginner decryption task. The player applies what was just learned to reveal the next part of the recruitment story.
- Increase complexity gradually as the player's technical skills grow. Early “decryption” may use fictional, deliberately simple transformations; do not present it as real cryptographic security.
- Preserve the distinction between narrative suspense and learning fairness: provide contextual clues and graduated hints, and never require an unexplained advanced syntax from a beginner.

## 6.5 Primary Mentor — Relationship Development

The primary mentor is not merely a tutorial voice or a mission dispatcher. The mentor is a major recurring character whose relationship with the player can develop throughout the storyline.

- Give the mentor a distinct, consistent personality, personal motivations, strengths, limitations, and a recognizable way of speaking.
- Let the relationship develop through shared operations, conversations, player decisions, successes, mistakes, and difficult moments.
- Trust, familiarity, emotional closeness, disagreement, and conflict may evolve naturally over time. These are narrative dimensions, not a requirement for a romance system.
- Player choices and actions may influence how the mentor responds, what they disclose, how much they trust the player, and how later conversations or story events unfold.
- Do not make every interaction a relationship score check. Character development should be expressed through believable dialogue, behavior, and consequences.
- The mentor remains the player's central guide even when a specialist temporarily takes over a technical or subject-specific conversation.
- The mentor's name, background, personality details, relationship stages, and any branching outcomes remain open until designed and approved.

## 6.4 Narrative principles

- Story must not feel pasted onto unrelated coding questions.
- Every mission should have a believable narrative reason to exist.
- Documents, evidence, messages, and operational objectives should advance the case or reveal the world.
- Characters and agents should have consistent identities, motives, and voices.
- Choices may influence relationships, access, available information, or later events when appropriate.
- Failure should produce meaningful feedback and learning opportunities, not merely a generic “wrong” screen.
- Maintain continuity across chapters and preserve prior player decisions when the story system supports them.

# 7. Core Gameplay Loop

The initial campaign uses a **hybrid mission system**: each operation has clear objectives and guided stages, while the player can interact with the relevant available in-game tools to investigate and complete the work. It is neither a rigid sequence of tutorial screens nor an unrestricted sandbox.

- **Guided structure:** show the current objective, relevant context, and a clear sense of what progress is expected. Break complex operations into manageable stages.
- **Interactive workspace:** within those stages, let the player use appropriate unlocked applications such as Terminal, Intelligence Database, Dossier, and Analysis. The available tools depend on the operation.
- **Player agency:** allow the player to inspect evidence, run experiments, compare information, and decide what to submit rather than simply pressing Next through instructions.
- **Contextual guidance:** provide hints and explanations when needed, especially for newly introduced concepts, without automatically solving the task.
- **Focused scope:** do not expose every tool or require every app in every mission. Keep the workspace relevant to the current objective and beginner's skill level.
- **Persistent context:** moving between an app and the operation should preserve the player's current mission state and work.

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

## 7.1 Mission-to-Mission Code Clues

Every mission should contain a **code- or syntax-based clue pointing toward the next mission**.

- The clue should emerge naturally from the mission's story, evidence, terminal output, decrypted message, dossier, or debriefing.
- It may be a short code fragment, a syntax pattern, a function name, a variable, or a technical term that foreshadows the next concept or operation.
- The clue should create curiosity and give the player a useful preview of what comes next, without simply revealing the next mission's full solution.
- Where appropriate, the next mission should make the connection clear and reward the player for recognizing or remembering the clue.
- Clues must match the player's current learning level. Do not introduce unexplained advanced syntax too early.
- Keep clues coherent with campaign continuity; they should feel like meaningful intelligence breadcrumbs, not random easter eggs.
- The clue is a narrative/learning bridge. It must not become an unfair blocker: the next mission should still provide the explanation or graduated hints needed to learn its new concept.

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

## 8.1 Hybrid Mission Evaluation

Mission evaluation uses a **hybrid approach**. The evaluator selects checks appropriate to the task rather than requiring every task to use one exact-answer rule.

- **Technical tasks:** validate required behavior, relevant concept use, execution results, and task constraints. Where multiple implementations satisfy the requirements, accept them; do not demand identical source code unless a specific syntax form is itself the learning objective.
- **Investigative tasks:** assess whether the player's conclusion is supported by the available evidence and whether uncertainty is represented responsibly. Accept different defensible conclusions or wording when they meet the evidence criteria.
- **Combined tasks:** evaluate both technical correctness and investigative reasoning as distinct dimensions. A program can work correctly while the interpretation of its data is weak, and a sound observation does not excuse a failed technical requirement.
- **Feedback:** explain which criteria were met, which need attention, and why. Offer contextual hints or another attempt where appropriate instead of a generic pass/fail message.
- **Mission configuration:** define explicit criteria and acceptable outcomes for each task. Do not use an AI model or unconstrained subjective judgment as the authority for core mission completion.

## 8.2 Failure and Retry

The initial version uses a **retry-first, learning-oriented failure model**.

- When a submission fails, explain which requirement was not met and why, using feedback appropriate to the player's current skill level.
- Let the player correct the work and submit again. Failure does not immediately end the operation or impose a narrative punishment.
- Provide graduated hints when useful: begin with a reminder about the relevant concept, then a more specific direction, and only provide stronger guidance when the player needs it. Hints should support learning rather than automatically reveal the complete answer.
- Preserve the player's working context and relevant progress between attempts so a failed submission does not force them to restart the entire operation.
- A failed attempt does not reduce rank, XP, relationship values, mission access, or story progress in the initial version. Do not add a retry counter penalty or consequence-based failure branch at this stage.
- A mission is completed only when its authored evaluation criteria are met. RUN remains available for experimentation, while SUBMIT checks the current work.
- Explain success as well as failure: show which requirements were satisfied and what the player has demonstrated.

The goal is to make experimentation safe and mistakes useful. More consequential failure systems may be considered in a later design phase, but they are not part of the initial version.

Keep **Run** and **Submit** separate: Run executes the player's work; Submit evaluates it against the mission's defined criteria.

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

## 10.4 Game Entry Loading Animation

The game will use the user's supplied 10-second animation as the initial loading presentation when Cyber Protocol is launched.

**Approved source asset specification**
- Source folder on the user's Android device: `~/storage/downloads/LOADING/frames/`
- Numbered PNG sequence: `frame_001.png` through `frame_240.png`
- Frame resolution: 1080 × 1920 pixels (portrait)
- Intended duration: 10 seconds
- Intended playback rate: 24 frames per second

**Initial use**
- Play this animation during the initial game-entry loading phase, before the player reaches the main menu.
- This is the approved use for the current implementation; it is not yet designated as the main-menu background or a story cutscene.
- The same animation may be considered for other loading transitions in the future, but each additional use must be evaluated in its own context rather than assumed.
- Preserve the portrait composition and aspect ratio. Do not stretch or crop important visual content.
- The implementation must consider APK size, memory use, decoding cost, and smooth playback on supported Android devices. Do not assume that bundling all 240 full-resolution PNG files directly is the final packaging strategy.
- The exact playback controls, skip behavior, transition into the main menu, and optimized asset format remain implementation decisions to be tested.

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

1. Read this GAME_CONCEPT design bible.
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

# 16. Runtime, Persistence, and Connectivity Decisions

## 16.1 Code Execution — Local Runtime

The initial version uses a **local Python runtime on the Android device**. Player code is executed on-device; sending code to a remote server is not required.

- Use the established Kotlin + Jetpack Compose application with Python integration through Chaquopy.
- The code runner must execute player-submitted Python in a controlled environment, separate from the app's privileged internal operations.
- Apply practical execution safeguards: execution time limits, output limits, input validation, controlled working directories, and prevention of access to unrelated app/device data or privileged Android capabilities.
- Do not expose unrestricted filesystem, network, process, shell, or Android API access to mission code.
- Keep mission evaluation separate from execution: RUN executes code and displays output/errors; SUBMIT evaluates the mission requirements.
- Core code exercises and missions must work offline. A remote execution service is not a dependency of the initial version.
- Treat sandboxing as a security boundary that requires careful engineering and testing, not as a guarantee merely because Python runs through Chaquopy.

## 16.2 Progress Saving — Autosave and Code Checkpoints

The initial version uses **autosave for campaign progress**, while player-written mission code uses **explicit save points and mission checkpoints**. Manual campaign save slots are not required.

- Save campaign progress automatically at meaningful checkpoints: completed story beats, mission-stage transitions, successful submissions, debrief completion, and rank/XP or unlock changes.
- Do **not** autosave the code editor contents on every keystroke or every small edit.
- Provide a deliberate way for the player to save their current code draft at appropriate points, such as a **Save** action. Also preserve the code when a mission stage is successfully completed or another clearly defined mission checkpoint is reached.
- Make it clear when the latest code draft was saved. Do not imply unsaved edits are safely stored.
- Preserve saved code drafts together with the relevant mission and stage so the player can resume that work later.
- Resume campaign progress and the most recently saved code draft after the app is closed, interrupted, or reopened.
- Use deliberate checkpoints and safe persistence for important state changes; temporary UI state and animations do not need to persist unless a later design decision identifies a clear need.

## 16.3 Connectivity — Offline-first

The initial version is **offline-first**. An internet connection must not be required to play the core campaign.

- The main menu, authored story, character dialogue, mission logic, learning content, code exercises, evaluation, progression, local code execution, and autosave must work without internet access.
- Core mission outcomes and campaign progression must not depend on a remote server or external AI service.
- Store campaign progress locally so the player can resume offline.
- Future online capabilities—such as optional AI-assisted conversation, cloud backup/synchronization, or downloadable content—are supplemental and must fail gracefully when unavailable.
- No online account, external API key, or continuous network connection is required for the initial campaign.
- Offline-first does not permanently prohibit online features; any future online feature must preserve the core offline experience.

## 16.4 Code Editor — Progressive Editor

The mission code editor uses a **Progressive Editor** approach. It begins with a focused, beginner-friendly interface and introduces more capable editing features as the player advances.

- Early operations should present only the essentials needed to write and understand short programs. Avoid overwhelming new players with a dense, professional IDE interface.
- As the player progresses through the campaign, rank, or demonstrated learning milestones, the editor may introduce features such as syntax highlighting, line numbers, automatic indentation, bracket assistance, and other quality-of-life tools.
- Unlocking editor features should support learning and reflect growing operational capability; it must not arbitrarily block a player from writing valid code.
- The editor remains the place where the player writes and edits mission code. Its available features must remain readable and practical on Android screens.
- Preserve the established code-draft saving rules in §16.2: campaign progress autosaves, while code drafts use deliberate Save actions and defined mission checkpoints.

## 16.5 Terminal and Mission Code Editor — Separate Roles

The **Terminal** and **Code Editor** are distinct tools with complementary roles.

- **Terminal** is a separate application available through the in-game App Hub. It is used to run programs and inspect execution output, errors, and relevant simulated system responses.
- **Code Editor** is part of the operation/mission interface. It is where the player writes and modifies the Python code required by the current task.
- The mission flow connects the two: the player edits code in the operation interface, runs it through the Terminal, reviews the result, makes corrections, and submits the work for evaluation when ready.
- Running code and submitting a mission remain separate actions. RUN executes and displays results; SUBMIT evaluates the mission requirements.
- Moving between the operation interface and Terminal should preserve the player's current mission context and unsaved editor contents during that active session. Persistence across app closure follows the explicit code-saving rules in §16.2.
- Other App Hub tools may provide evidence, records, analysis, or operational context, but should not blur the distinct purpose of the editor and Terminal.


## 16.6 Terminal — Real In-Game Execution Environment (B2)

The player uses a **real, functional terminal environment built inside Cyber Protocol**. It is not merely a visual mock-up, but it is also not the Android system shell, Termux, or a launcher for external applications. The player remains inside the game throughout the operation.

- Terminal commands and supported programs must actually execute and produce real, inspectable results within a controlled environment owned by the game.
- The terminal environment should provide a game-managed workspace, command set, files, and simulated operational systems. Its capabilities can expand as missions, rank, and clearance progress.
- Commands may interact with fictional case files, mission data, simulated databases, and controlled network/system environments when the current operation authorizes them.
- The terminal must not open or depend on external Android applications. It must not expose unrestricted access to the Android/Linux host shell, device filesystem, processes, network, or privileged system capabilities.
- The Terminal application remains separate from the mission Code Editor as defined in §16.5. The editor is where the player writes code; the in-game Terminal is where supported commands and programs are executed and their results inspected.
- Python mission code continues to use the on-device runtime described in §16.1, integrated with the game-managed execution environment. Terminal commands and Python execution must share only explicitly designed interfaces and permitted mission data.
- Execution results must be available to the Mission Engine and Evaluator through structured, testable interfaces. This allows genuine command/program output to become evidence, satisfy objectives, or reveal story information without allowing terminal code to modify campaign state arbitrarily.
- Implement strict command permissions, controlled virtual files, execution/resource limits, input validation, and isolation between player code and privileged game logic. Chaquopy or an in-game terminal interface alone does not automatically provide a secure sandbox.
- The terminal is a core gameplay tool, not a decorative screen. Its command behavior, output, errors, and available capabilities must be consistent, learnable, and testable.


## 16.7 Terminal Input — Hybrid Command Interface

The in-game Terminal uses a **Hybrid Command Interface**.

- Players may type supported commands directly, preserving the feel of working in a real terminal.
- Provide discoverable assistance such as a command reference, contextual help, command suggestions, and autocomplete where practical.
- Assistance should make the terminal approachable to beginners without replacing the player's ability to enter commands independently.
- Suggestions and autocomplete must reflect the commands and permissions actually available in the current mission and the player's current clearance.
- Invalid commands should produce understandable terminal errors or help, not silently perform a different action.
- The interface must remain usable on Android touch keyboards and small screens.

## 16.8 Python 3 — Embedded On-Device Runtime

Cyber Protocol will include an embedded **Python 3 runtime** so players can execute real Python programs without leaving the game or installing a separate terminal application.

- Use Chaquopy as the initial Android integration path, subject to compatibility checks against the project's Android Gradle Plugin, minimum Android version, device ABIs, and required Python packages.
- Python executes locally on the Android device and remains available for core missions offline.
- The Code Editor is where players write Python. The in-game Terminal provides the execution interaction and displays standard output, errors, and supported input/results.
- The runtime must integrate with the game-managed workspace and mission data only through explicitly designed interfaces. It must not expose the unrestricted Android/Linux shell or unrestricted access to the device.
- Provide controlled input/output handling, execution time and output limits, cancellation where feasible, and isolation from privileged game state and unrelated player data.
- Do not assume every package available on desktop Python is available on Android. Validate each dependency for the selected Chaquopy/Python version and supported Android ABI before adopting it.
- Keep the Python runtime modular behind a Code Runner interface so the game can evolve its execution implementation without coupling mission logic or UI directly to Chaquopy APIs.
- RUN executes the player's program and presents its actual result. SUBMIT remains a separate action that evaluates the mission criteria.
- The exact Python 3 minor version and package set are implementation decisions to be selected after inspecting the repository's current build configuration and testing compatibility; do not hard-code a version in the design bible prematurely.


### 16.9 — Game-Managed Virtual File System

Cyber Protocol will include a real, game-managed virtual file system as part of its in-game terminal environment. The filesystem is not a view of Android storage and must never expose unrestricted access to the device filesystem.

**Hybrid, progressively unlocked design:**
- Players can work with folders and files through supported terminal commands: navigate, list, read, create, edit, and delete files where permissions allow.
- The initial experience introduces only the small set of filesystem operations needed for the current mission. Additional commands, locations, file types, and capabilities become available as the campaign progresses and the player's rank or clearance increases.
- The terminal provides command help, contextual suggestions, and clear permission or path errors. It must not silently perform a different operation when a command is invalid or unavailable.
- The virtual workspace can contain mission files, evidence, reports, datasets, scripts, and other fictional A.E.G.I.S. resources. Its structure and contents are controlled by the game.
- Mission objectives may grant read-only, read/write, or other narrowly scoped access to particular locations. Access to evidence or operational data must follow the mission's authorization and story context.
- File operations produce genuine, inspectable results within the virtual environment. They are not merely decorative terminal output.
- The virtual filesystem must be integrated with the local Python runtime through explicit, controlled interfaces. Python programs may access only the files and data granted to the current environment; they must not bypass permissions or access unrelated Android files.
- Filesystem state and player-created work must be persisted according to the game's save/checkpoint rules. The exact boundary between persistent personal workspace and mission-scoped temporary files remains a separate design decision.
- The game must validate paths and operations, prevent traversal outside the virtual workspace, enforce resource limits, and protect campaign state and system data from arbitrary file modification.

The virtual filesystem should make the terminal feel like a functional computer environment while preserving the controlled, offline-first, story-driven nature of Cyber Protocol.


### 16.10 — Hybrid Workspace Persistence

Cyber Protocol will provide two distinct areas within its game-managed virtual filesystem:

**1. Personal Workspace**
- A persistent workspace belonging to the player-character and available across missions.
- Players can keep their own scripts, notes, reusable tools, and other permitted files here.
- Files created or saved in this area remain available when a mission ends and can be used in later missions, subject to the current mission's access rules.
- The game should make it clear which files are personal work and which are official A.E.G.I.S. resources.

**2. Mission Workspace**
- Each operation receives its own scoped workspace for mission-specific files, evidence, datasets, reports, and temporary work.
- Mission data and evidence must remain associated with the operation that supplied them, preserving provenance and preventing accidental mixing between cases.
- Access may be read-only or read/write depending on the player's authorization and mission objectives.
- On mission completion, the workspace is retained as part of the operation's archive or case record according to the story and save rules. It must not simply disappear without clear communication to the player.
- Later missions may explicitly grant access to selected archived material when the story or authorization permits it; one mission must not automatically expose every other mission's private workspace.

**Integration and persistence**
- The terminal should clearly identify the current location and distinguish Personal Workspace from the active Mission Workspace.
- Supported commands should allow navigation between areas only when the player has permission.
- Python execution may read or write files in either area only through explicit game-managed interfaces and the permissions granted to the current operation.
- Personal files and mission workspaces must be included in the game's local persistence and checkpoint strategy. Code drafts still follow the explicit-save and checkpoint rules defined elsewhere in this document.
- The design must preserve file ownership, mission association, access permissions, and recovery after interruption or app restart.

This hybrid model gives players a lasting place to develop their own tools while keeping each investigation's evidence and working files organized, attributable, and appropriately restricted.


### 16.11 — Standard Linux-Like Terminal Commands

The in-game terminal will use recognizable, standard Linux-like command conventions for its supported command interface. Players should learn transferable command-line concepts rather than memorize a fictional A.E.G.I.S.-only command language.

**Design principles**
- Use familiar command names and syntax where practical, such as `pwd`, `ls`, `cd`, `cat`, `mkdir`, and other appropriate file-management commands.
- Do not invent arbitrary command names or require players to memorize fictional command syntax merely to interact with the game.
- Commands execute inside Cyber Protocol's game-managed virtual environment. Familiar command syntax does not grant access to the Android/Linux host shell, device files, processes, or unrestricted network resources.
- Implement only the commands and options that are deliberately supported, tested, and appropriate to the current environment. Do not imply full Linux compatibility where it does not exist.
- Provide contextual help, command reference, autocomplete, and clear errors so beginners can learn standard syntax while retaining the ability to type commands independently.
- Unlocking should primarily expand the player's authorized files, datasets, simulated systems, and operational capabilities—not introduce arbitrary fictional command dialects.
- Mission-specific actions should be represented through accessible files, scripts, and clearly explained application interfaces wherever possible, rather than a growing collection of bespoke commands.
- Python scripts are written in the Code Editor and executed through the in-game terminal using the supported Python runtime and a clearly documented invocation.

The goal is a functional, approachable terminal that builds useful real-world command-line familiarity while remaining safely contained within the game's virtual workspace.
