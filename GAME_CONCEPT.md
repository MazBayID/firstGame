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

The operational interface provides a compact, smartphone-style hub for the player's in-world A.E.G.I.S. tools. These applications are parts of one connected operation, not unrelated minigames or a generic dashboard.

- Place a small application-hub control in the **upper-right corner** of relevant operational interfaces.
- Selecting it opens a polished smartphone-style application panel with the applications currently available to the player.
- The initial application ecosystem is:
  1. **Secure Messenger** — in-world messaging with A.E.G.I.S., Freya, NEXUS, and other unlocked agents. It delivers briefings, instructions, mission links, and file attachments.
  2. **A.E.G.I.S. Browser / Intranet** — an offline simulated internal website for fictional login, Mission Control, assignments, File Repository, Intelligence Reports, and mission submissions.
  3. **Terminal** — the in-game command-line environment for navigating the virtual filesystem, inspecting files, and executing supported commands and Python programs.
  4. **Intelligence Database** — a structured interface for authorized records, findings, and verified intelligence.
  5. **Dossier** — briefings, classified documents, evidence, and case files.
  6. **Analysis** — focused tools for examining evidence, comparing information, and organizing findings.
  7. **Operations** — current objectives, mission status, progress, and debrief access.
- The **Code Editor is part of the active operation workspace**, not a replacement for the Terminal. The player writes and edits Python in the Code Editor, then runs the script through the in-game Terminal.
- Introduce tools when the story and the player's learning progression make them relevant; do not expose every capability immediately.
- Opening an application must preserve the active operation context. Returning restores the previous screen and working state.
- Messenger attachments, Browser pages, Repository files, virtual filesystem, Terminal, Code Editor, and mission evaluation must use consistent underlying mission data. A file delivered in a message or downloaded from the fictional intranet must genuinely exist in the authorized virtual workspace.
- The Browser never depends on the public internet. It represents fictional A.E.G.I.S. services and works offline.
- Keep the hub focused. Every tool must serve story, learning, investigation, or operations.
- Exact icon design, panel layout, transitions, and detailed application interactions remain UI design work.

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

NEXUS is the A.E.G.I.S. AI communication-intelligence and technical-support system. It is a supporting system, **not the player's primary teacher or human mentor**.

NEXUS can:
- Deliver system alerts, operational status, mission updates, and technical diagnostics.
- Provide reference documentation and factual explanations about available A.E.G.I.S. systems.
- Help interpret terminal/runtime errors by identifying relevant diagnostics and pointing the player toward useful documentation.
- Provide secondary hints when appropriate, without replacing Freya's teaching role or solving the operation.
- React to success, failure, discoveries, and story events.
- Warn the player about relevant operational conditions.
- Track and surface the player's in-game progress and previously demonstrated skills where supported by the game data model.

**Freya Knox teaches terminal use and Python from zero.** NEXUS may support that process with diagnostics and reference material, but must not take over the primary instructional relationship.

NEXUS guides the player's thinking rather than automatically solving missions. Core dialogue, mission progression, and technical learning work offline; NEXUS does not require a live AI service.

## 5.4 Primary Mentor and Specialist Handoffs

The player should have **one primary human mentor** who accompanies them from the beginning of recruitment and serves as their main point of contact throughout the RPG experience.

- The primary mentor provides ongoing guidance, follows the player's development, and helps maintain continuity across the campaign. In the initial version, relationship development is conveyed through authored story scenes and dialogue rather than a player-controlled relationship meter or persistent consequences from cosmetic reply selections.
- The primary mentor is **Freya Knox**, a woman. Her approved broad appearance direction is **youthful and energetic**. Her role as the player's primary beginner-level terminal and Python instructor is established. Exact age, facial features, hairstyle, clothing, accessories, detailed history, and any broader specialist expertise remain open until separately designed and approved.
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

The player begins as an ordinary person who is unexpectedly contacted by A.E.G.I.S. through an official secure recruitment message. The message explains that an unknown person has ordered A.E.G.I.S. to recruit the player. The identity of that person is deliberately withheld.

The opening follows the approved sequence in §21: official A.E.G.I.S. recruitment notification, the player's deliberate interaction with the invitation, activation of the first mission, Freya Knox's separate mentor notification, and Freya's introduction of NEXUS. Do not add an Unknown Sender prologue or replace the official recruitment message with an unsolicited encrypted-message opening.

The chapter then introduces the A.E.G.I.S. application hub and Freya-led technical orientation. Freya assumes the player has no prior terminal or programming experience and teaches the required foundations before expecting independent technical work.

The opening story beats are:

1. **Official Recruitment** — A.E.G.I.S. / Recruitment Office sends the approved secure invitation.
2. **The Invitation** — the player opens the message and taps the in-world mission-sequence link.
3. **First Mission Activation** — the campaign establishes the initial operational context.
4. **Freya's Contact** — Freya sends the separate notification and introduces herself as the player's primary human mentor.
5. **NEXUS Introduction** — Freya introduces the AI system and the player explicitly opens its contact.
6. **Technical Orientation** — Freya teaches the in-game applications, terminal, virtual files, Code Editor, and Python from the beginning.
7. **A.E.G.I.S. Intranet Access** — the player follows Freya's in-world link, uses fictional credentials, and learns to navigate the simulated organization portal.
8. **First Operation** — the player receives an assignment, obtains supporting files, performs the required work using the tools Freya has taught, and submits the result through the appropriate interface.
9. **Recruitment Verification** — A.E.G.I.S. evaluates the player's demonstrated abilities and advances the recruitment story.

These beats may be expanded into dialogue, documents, evidence, environmental storytelling, and cinematic transitions while preserving the approved notification sequence and hidden story facts.

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

## 6.3 First Contact — Learning Before Technical Operations

The first chapter introduces technical work only after the player has received the appropriate explanation and guided practice. The opening must be fair to someone who has never used a terminal or written code.

- Do not make an unexplained encrypted message the player's first programming test.
- Freya is the primary guide for every foundational terminal and Python concept in the opening chapter. NEXUS may provide secondary system diagnostics or reference material.
- Freya first explains the application hub and the role of Messenger, Browser/Intranet, Terminal, Code Editor, and mission files as they become relevant.
- Freya teaches terminal fundamentals in a safe practice workspace, then introduces Python with small programs the player can type, run, observe, and modify.
- Only after the relevant concepts have been taught and practiced should the player apply them to mission evidence, simple transformations, or deliberately fictional training ciphers.
- Any early decryption exercise uses a transparent, beginner-appropriate toy transformation. Never imply that a beginner is breaking real modern cryptography.
- Mission information and required input must be discoverable through the in-game briefing, Browser, Repository, Dossier, Messenger attachments, or files Freya explicitly explains.
- Suspense comes from the fictional intelligence story, not from hiding information needed to solve a task or demanding syntax the player has not learned.
- The first operation connects directly to the recruitment narrative and uses the same Terminal, Code Editor, virtual filesystem, and submission systems as later operations.

## 6.5 Primary Mentor — Relationship Development

Freya Knox is a major recurring character, the player's primary human mentor, and the central guide throughout the campaign.

- Give Freya a distinct, consistent personality, personal motivations, strengths, limitations, and recognizable voice, following the approved characterization in §5.4.
- Let familiarity and trust develop through shared operations, authored conversations, successes, mistakes, and difficult moments.
- In the initial version, predefined player dialogue choices are **cosmetic**. They may change the immediate authored response, but they do not alter relationship values, unlock content, change mission access, or branch the main story.
- Do not implement a relationship meter, persistent dialogue-choice flags, or consequential relationship branching in the initial version.
- Freya remains the familiar central guide even when a specialist temporarily assists with a subject outside her expertise.
- Specialist handoffs are introduced and contextualized by Freya. A specialist complements her role and does not replace the primary mentor.
- Any future move toward consequential dialogue choices or relationship systems requires a separate explicit design decision.

## 6.4 Narrative principles

- Story must not feel pasted onto unrelated coding questions.
- Every mission should have a believable narrative reason to exist.
- Documents, evidence, messages, and operational objectives should advance the case or reveal the world.
- Characters and agents should have consistent identities, motives, and voices.
- In the initial version, predefined dialogue choices are cosmetic as defined in §3.3 and §6.5. Consequential choices, branching access, and relationship effects are not part of the initial implementation; they require a separate approved design decision.
- Failure should produce meaningful feedback and learning opportunities, not merely a generic “wrong” screen.
- Maintain continuity across chapters and preserve authored story flags and established campaign state. Initial predefined dialogue choices remain cosmetic and must not create consequential relationship or access branches.

# 7. Core Gameplay Loop

Cyber Protocol is a connected intelligence-operation experience. The player receives information through in-world communications and the simulated A.E.G.I.S. Intranet, investigates through authorized applications, performs technical work in the Code Editor and Terminal, and submits findings through the operation's designated interface.

The game combines a clear guided structure with meaningful player agency. It is neither a rigid sequence of tutorial screens nor an unrestricted sandbox.

- **Story-led context:** story scenes and character messages establish why an operation matters.
- **Messenger-led communication:** A.E.G.I.S. and Freya deliver instructions, links, explanations, and attachments through Secure Messenger.
- **Intranet-led assignments:** the simulated Browser provides the official portal for login, mission briefings, file repositories, reports, and submission workflows when required.
- **Evidence and files:** supporting files are genuinely present in the game-managed virtual filesystem, with permissions and provenance appropriate to the operation.
- **Terminal and Code Editor:** the player writes and edits code in the Code Editor, executes it through the Terminal, and inspects actual output and errors.
- **Player agency:** the player can inspect evidence, test ideas, compare information, and decide what to submit rather than merely pressing Continue.
- **Freya-led instruction:** Freya explains unfamiliar tools and concepts before expecting the player to use them. Guidance decreases gradually as demonstrated understanding grows.
- **Persistent context:** moving between Messenger, Browser, Dossier, Database, Code Editor, and Terminal preserves the active operation, files, and current work according to the save rules.

The intended operation cycle:

1. **Story event / recruitment communication** — a new development is introduced.
2. **Freya's guidance** — the player receives the explanation or practice needed for unfamiliar tools and concepts.
3. **Intranet access / briefing** — the player opens the authorized portal and reads the assignment where applicable.
4. **File delivery / repository** — the player receives or retrieves supporting files and verifies where they are stored.
5. **Investigation** — the player reads dossiers, evidence, messages, and authorized database records.
6. **Technical work** — the player edits a Python script and uses the Terminal to run it against permitted virtual files or datasets.
7. **Freya / NEXUS support** — Freya teaches and gives graduated hints; NEXUS supplies secondary diagnostics and system references.
8. **Submission** — the player submits the result through the appropriate in-world interface.
9. **Evaluation and debrief** — the game checks actual behavior and learning objectives, explains the result, and records evidence/report outcomes.
10. **Progression** — story, XP, rank, clearance, skills, and the next operation advance according to authored rules.

The exact apps used depend on the operation; do not force every tool into every mission. Browser and Messenger are simulated in-game applications and do not require public internet access.

**Run and Submit remain separate actions.** RUN executes the player's code and displays actual output/errors. SUBMIT evaluates the authored mission requirements. Running code alone must never silently complete a mission.

# 7.1 Mission-to-Mission Code Clues

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

- Freya is the primary teacher for terminal use and Python. She introduces concepts from zero, explains what commands and syntax mean, demonstrates when useful, and guides the player through hands-on practice.
- LEARN provides an optional, reviewable reference for concepts already introduced or available to study. It supplements Freya's in-world instruction; it does not replace mentor-led onboarding.
- PRACTICE provides safe exercises where the player can experiment without risking protected mission evidence or campaign state.
- MISSION applies learned skills to a story-driven operational objective.
- Teach the purpose and behavior of commands and code, not only the exact text to type. Explain expected output and how to recognize errors.
- Hints are graduated: begin with observation or a conceptual reminder, then offer a syntax cue, then more specific guidance if needed. A complete solution is not the default.
- Evaluators check actual execution, required behavior, and intended concept use where appropriate, not merely whether output text matches.
- Explain errors in accessible language and connect them to the concept being learned.
- Keep tutorials, mission files, terminal commands, Python capabilities, and evaluator criteria consistent and testable. Never instruct the player to use an unavailable file, command, or language feature.

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
- Apply practical execution safeguards: a strict wall-clock budget and line-event budget, source/AST/input/output limits, bounded collections and file writes, controlled working directories, and prevention of access to unrelated app/device data or privileged Android capabilities.
- The initial runner uses a three-second wall-clock budget and a 50,000 line-event budget; keep limits explicit, configurable, and covered by automated tests.
- Do not expose unrestricted filesystem, network, process, shell, or Android API access to mission code.
- Chaquopy execution currently runs in-process. AST restrictions and quotas are defense-in-depth, not process isolation or a guarantee against hostile code. Do not market the initial runner as a hardened hostile-code sandbox or accept arbitrary third-party scripts. A process-isolated runner requires a separately reviewed architecture.
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
- The Code Editor's RUN action saves the current source into the mission's authorized writable script, switches to the dedicated Terminal screen, and invokes the normal `python <script>.py` command path. It must not call the Python runtime directly from the editor UI or bypass Terminal command validation.
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


## 18. Approved Implementation Strategy — Full Rebuild

The approved development direction is **Option A: Full Rebuild**.

- Reconstruct the application architecture and user interface around this design document rather than treating the current prototype structure as binding.
- Retain and maintain the project’s authoritative design and narrative documents: `GAME_CONCEPT.md`, `README.md`, and `Story_Line.md`.
- Preserve existing user-supplied assets, including the optimized loading animation at `app/src/main/assets/loading/`, and reuse suitable assets after inspection.
- Audit the current repository before rebuilding. Existing code may be reused only when it is sound, tested, and consistent with the approved design; otherwise replace it.
- Build the new implementation in coherent, testable milestones. Do not claim a feature is complete merely because its screen exists; verify behavior and integration.
- The rebuild must follow the decisions in this document and the approved narrative in `Story_Line.md`. Do not silently promote unapproved story proposals or assistant suggestions into canon.
- Prioritize a cohesive, attractive Android game experience and functioning gameplay systems over superficial feature count.
- Keep the game offline-first and preserve the safety boundaries, local Python runtime constraints, and game-managed virtual environment defined above.

This approval establishes the rebuild strategy. It does not authorize deleting the authoritative design documents or user-supplied assets.


### 16.12 — Progressive Technical Tracks

The initial campaign teaches Python from zero, with Freya as the primary instructor. Additional technical disciplines are introduced through the story, authorized tools, and optional learning content.

- Python terminal and programming fundamentals are taught before the player is expected to complete independent technical operations.
- SQL, networking, C++, and IoT are introduced progressively when the campaign provides a credible operational reason and the player has the necessary context.
- The LEARN area may provide optional explanations and foundational study material beyond the current mission. It is a review and enrichment resource, not a substitute for Freya's guided onboarding.
- Access to operational tools, datasets, simulators, and mission execution capabilities remains governed by story progression, rank, clearance, and relevant safety boundaries.
- Learning a concept and receiving permission to use an operational system are separate things.
- The first release prioritizes a complete, polished Python learning and mission experience. Additional languages and technical tracks must be implemented as real, integrated systems—not decorative locked cards or promises of future functionality.

This approach keeps the opening accessible, rewards campaign progression, and gives motivated players room to explore without turning the game into an unstructured course catalogue.

### 16.13 — A.E.G.I.S. Browser and Simulated Intranet

Cyber Protocol includes an in-game Browser that provides access to a fictional A.E.G.I.S. internal website. It is a simulated application, not a general-purpose internet browser.

- The Browser operates fully offline using locally authored pages, data, and navigation. It must not require public internet access, external websites, real credentials, or a remote service for the core campaign.
- The opening story introduces the Browser through a link sent in Secure Messenger. The player follows the link, reaches the A.E.G.I.S. Intranet login page, and uses fictional credentials supplied through the story: agent ID `RECRUIT-014` and access code `FIRSTCONTACT`.
- The login flow is a narrative/gameplay simulation. Credentials are fictional, validated locally, and must not be reused as real account credentials. The Browser becomes available in the App Hub when the first mission is active.
- The first mission's read-only repository asset is `message_fragment.txt`, containing the fictional fragment `ACCESS GRANTED`. Freya's Messenger attachment and the Browser File Repository must install the same authored asset into the active mission workspace; downloading it from either location must not create conflicting copies.
- Mission Control is the official submission interface. It sends the player's actual editor source to the mission evaluator and accepts submission only when that exact source has completed a successful RUN. The Code Editor remains the place to write and run code; the Browser handles mission submission and its result.
- The intranet may contain Mission Control, File Repository, Intelligence Reports, Operations Archive, and Agent Profile. Introduce sections progressively according to story and clearance.
- Mission Control presents the active assignment, objectives, required evidence, submission instructions, and current status.
- File Repository provides supporting mission files. When a player downloads or opens an attachment, the corresponding file must genuinely be created or made available in the authorized game-managed virtual filesystem, with correct contents, path, permissions, and mission association.
- Secure Messenger attachments and Browser repository downloads must resolve to the same underlying file asset when they refer to the same evidence. Avoid duplicate or inconsistent copies that disagree.
- Mission submission through the Browser must pass the player's actual work/result to the evaluator. A decorative Submit button or hard-coded answer reveal is not a functioning submission system.
- The Browser preserves navigation state and returns the player to the previous operation context. It must not silently discard code, files, or unsaved work.
- Browser pages, links, login states, downloads, and submission outcomes must be testable and persist according to campaign checkpoint rules.
- Do not imply unrestricted access to real websites or external networks. Later simulated network operations remain controlled, fictional, and explicitly authorized.


# 19. Updated Creative Direction — Adventure RPG and AAA-Level Presentation

The default genre and player-facing identity of Cyber Protocol is **Adventure RPG**. The existing intelligence, investigation, and technical-simulation pillars remain central gameplay systems within that broader adventure-RPG experience; they are not a reason for the campaign to feel like a sequence of static utility screens.

**Quality target:** Aim for the cohesion, atmosphere, production polish, and emotional impact associated with premium/AAA games, while treating this as a quality direction rather than a claim about budget or production scale.

- The campaign must feel like an unfolding adventure with momentum, discoveries, tension, character moments, and a strong sense of place.
- Replace repetitive, static screen-to-screen presentation with purposeful cinematic transitions, scene staging, varied composition, contextual motion, environmental details, and strong visual hierarchy.
- Use animation and effects to communicate events and state changes, not as decoration on every screen. Respect reduced-motion settings.
- Make story beats visually and emotionally distinct: quiet conversations, urgent alerts, classified briefings, discoveries, mission preparation, and debriefs should not all use the same layout.
- Build atmosphere through sound design, music, interface audio cues, lighting, and visual feedback when supported by the platform and production scope. Audio must be configurable and must not be required to understand essential story information.
- Make the player feel present in the world through first-person framing, believable in-world devices, documents, locations, and operational context. Do not turn the game into a generic dashboard or a programming lesson app.
- Keep controls readable and responsive on Android. Cinematic presentation must never make mission objectives, dialogue, accessibility controls, or navigation unclear.
- Prioritize a coherent, memorable campaign experience over adding many shallow screens or features. Every major interface should serve story, exploration, progression, or gameplay.

These are product-quality goals. Specific story branches, characters, locations, art assets, and future cases still require approval before becoming canon.

# 20. Localization — Indonesian and English

Cyber Protocol must support **Bahasa Indonesia** and **English** as selectable game languages.

- Provide a language selector in Settings (and an accessible first-run choice if appropriate).
- The initial supported languages are Bahasa Indonesia and English. Do not assume additional languages without a separate decision.
- Localize all player-facing content, including menus, settings, notifications, app names/descriptions where appropriate, campaign dialogue, mission objectives, hints, terminal help, errors, debriefs, archives, and accessibility text.
- Keep narrative and gameplay data separate from UI code so the selected language can consistently resolve the correct text.
- Changing language must not reset campaign progress, mission state, saved code, evidence, or settings unrelated to language.
- Persist the selected language locally and apply it consistently after app restart.
- Avoid mixing Indonesian and English unintentionally within the same selected-language experience. Code, standard programming syntax, and established technical command names remain unchanged where translation would be misleading.
- English localization must read naturally rather than being a literal, awkward translation. Preserve character voice, especially Freya's professional first impression and gradually revealed mischievous humor.

# 21. Campaign Opening — Official Recruitment and Mentor Contact

The Campaign opening uses two distinct in-game notification events. They must never be collapsed into one conversation.

**First notification — Official A.E.G.I.S. recruitment**
- Transition to a fully black screen on the first Campaign entry.
- After a deliberate pause, show an understated, ordinary-looking **Pesan Aman** notification from **A.E.G.I.S. / Recruitment Office**.
- The player must tap the notification. It opens Secure Messenger directly to the official A.E.G.I.S. recruitment conversation—not to Freya, a generic inbox, or an unknown-sender prologue.
- The official message explains that someone has given A.E.G.I.S. an order to recruit the player. Approved Indonesian core wording: **"Seseorang telah memberikan perintah kepada A.E.G.I.S. untuk merekrutmu."**
- The sender does not disclose the identity of the person behind the order.
- The message contains an interactive in-game link labelled **BUKA RANGKAIAN MISI**. The player must tap it to enter the orientation and mission sequence.
- The A.E.G.I.S. Recruitment Office remains in the Messenger inbox after the invitation is opened.

**Second notification — Freya Knox**
- When the first mission becomes active, a second Pesan Aman notification appears over the mission screen. Its sender is **Freya Knox**.
- The player must tap it to open Freya's conversation. The notification must not automatically open the conversation.
- Freya greets the player, introduces herself as the mentor, and explains that she will guide the missions assigned by A.E.G.I.S.
- Freya must not say that she issued the recruitment order, and her dialogue must not reveal or strongly imply that connection.
- Persist the notification state. If the app is interrupted before the player taps it, present it again when the first mission resumes. After it is opened, do not replay it unexpectedly.

**NEXUS contact unlock**
- During her conversation, Freya introduces NEXUS as an internal A.E.G.I.S. communication and technical-support system.
- Freya provides an interactive **NEXUS / KANAL SISTEM** link.
- Only after the player taps that link may the NEXUS conversation/contact become visible in the Messenger inbox. Do not show NEXUS as an available contact before this unlock.
- NEXUS is a system, not a human mentor. Freya remains the player's primary human mentor.

**Narrative continuity — author-only**
- Freya is the person who initiated the recruitment order. This is a hidden story fact and must not be revealed in the official A.E.G.I.S. invitation or Freya's initial greeting. Reserve the reveal for a later, deliberately authored story moment.
- Do not add a separate **"Pengirim tidak dikenal" / "Unknown Sender"** prologue.
- The notifications and links are in-game simulations only. Do not request Android notification permission, create real system notifications, access real messages/contacts, or require external messaging services.
- Save each meaningful tap/checkpoint so app interruption cannot duplicate messages, skip a required action, or reveal a contact prematurely.

# 22. Secure Messenger — Familiar Smartphone Messaging Experience

The in-game **Pesan Aman / Secure Messenger** should feel like a familiar, polished smartphone messaging application inspired by common messaging usability patterns, while retaining Cyber Protocol and A.E.G.I.S. branding.

- The inbox should feel like a real messaging list, with clear sender identity, latest-message preview, timestamp, unread indicator, and search where practical.
- The official **A.E.G.I.S. / Recruitment Office** conversation is the first visible contact after the opening notification is tapped.
- **Freya Knox** appears in the inbox only after the player taps her second notification during the first mission.
- **NEXUS** appears in the inbox only after the player taps the NEXUS link provided inside Freya's conversation.
- Individual conversations use a recognizable chat layout with distinct incoming and outgoing message bubbles, timestamps, and a clear conversation header.
- Authored dialogue and predefined player replies are acceptable for the initial campaign. Do not introduce free-text consequential dialogue unless separately approved.
- Important mission links, briefings, and evidence attachments should use tappable in-world cards that open the relevant game context.
- Freya's voice is that of the primary human mentor. NEXUS is an AI system and must not be presented as a human character or as the person who recruited the player.
- Returning from a conversation should preserve the inbox; closing Messenger should return to the game screen that opened it without losing campaign context.
- Use original A.E.G.I.S. visual identity. Do not copy WhatsApp logos or proprietary assets, or imply that this is the real WhatsApp service.
- Keep the experience offline-first and locally persisted. No real phone contacts, SMS, notifications, or external messaging account access is required.


# 23. First Contact — Approved Mission and Evidence Sequence

The opening chapter now contains eight technical missions across fourteen story beats. It is one continuous orientation and investigation, not eight unrelated coding exercises.

| Mission | Player learning objective | Evidence purpose |
|---|---|---|
| Sinyal Pertama | Variables and `print()` | Verify a first authored response |
| Verifikasi Identitas | Named variables and output | Verify a second response |
| Audit Jejak Sistem | `for`, `if`, counting | Count ERROR entries in a supplied event log |
| Inventaris Rekaman | `len()` | Count available record references |
| Penelusuran Referensi | `enumerate()`, loop, filtering | Find the matching reference while retaining source indices |
| Perbandingan Dua Sumber | List indexing and named variables | Read and display the two receipt records for comparison |
| Normalisasi Waktu | `int()`, indexing, subtraction | Convert timestamp strings and calculate a signed delta |
| Validasi Selisih | `abs()`, arithmetic | Calculate the absolute difference across reversed source order |

- All datasets are fictional, deterministic, local, and read-only during execution.
- RUN executes the player's code against the current mission dataset; SUBMIT evaluates the solution against multiple fixtures where appropriate.
- Mission evaluators must verify the intended programming concept as well as the expected behavior. A hard-coded answer must not pass a mission that teaches a variable, loop, condition, enumeration, or indexing.
- Each mission carries a concise forward clue, surfaced in the mission debrief after successful verification. Clues should point toward the next story action or technical concept without revealing its full solution.
- The final report records the four-second difference between A.E.G.I.S. and ARCHIVE receipt times without asserting a cause.
- The discrepancy remains unverified as to cause. Do not introduce a culprit, reveal manipulation, or start a separate case without approved narrative content.
- Completion of the fourteen story beats is the First Contact chapter milestone. Recruitment clearance is awarded at the earlier Recruit milestone; the later missions extend the chapter rather than changing the initial recruitment event.


## 23.1 — Zero-Prerequisite Onboarding: Freya's Full Technical Training

Cyber Protocol must be playable and understandable by someone who has **never used a command-line terminal and has never written a line of code**. The player is not expected to arrive with programming knowledge. Teaching these skills from the ground up is a core part of the opening campaign and of the game's identity.

**Freya Knox is the primary instructor.** She personally teaches the player how to use the in-game terminal and Python, beginning with the most elementary concepts and gradually building toward independent problem-solving. This is integrated into the story and operational workflow; it must not feel like a detached programming course or a tutorial checklist outside the fiction.

### Teaching principles

- Assume zero prior knowledge. Never rely on unexplained terms such as command, directory, path, variable, string, argument, function, loop, or error. Introduce each term in plain language before expecting the player to use it.
- Teach the purpose and meaning of an action, not only the exact text to type. Explain what a command or line of code does, why it is written in that form, what the player should expect to see, and how to recognize whether it worked.
- Freya should be patient, encouraging, lively, and consistent with her approved personality. She can joke and tease affectionately, but must never shame a beginner, treat a reasonable question as foolish, or assume the player already understands a concept.
- Present one manageable new idea at a time. Build on previously demonstrated knowledge, revisit concepts naturally, and avoid overwhelming the player with a large block of instructions or several unfamiliar syntax features at once.
- Let the player perform the actions. Freya explains and demonstrates when appropriate, then asks the player to type commands or write and run code themselves. Do not auto-complete the learning experience by silently executing every step on the player's behalf.
- Use guided practice first, followed by a small independent task and then a mission task that uses the same skill in a meaningful operational context.
- Hints should be graduated: first prompt the player to observe or think; then remind them of the relevant concept; then offer a small syntax cue. Show a complete solution only when explicitly appropriate for an instructional demonstration, never as the default way to pass a mission.
- Mistakes and runtime errors are expected parts of learning. Explain errors in beginner-friendly language, identify the relevant part of the player's input, and guide them toward diagnosing and correcting it. Do not punish experimentation with irreversible loss of mission progress.
- Reuse familiar examples and vocabulary so that a concept learned in training remains useful when the campaign later applies it to evidence and operations.
- The player may review explanations, command references, and previously introduced concepts. Essential knowledge must not depend on remembering a one-time chat message.

### Terminal foundations

Before the player is expected to use the terminal for mission work, Freya teaches its basic mental model and supported commands in the game's contained virtual environment.

The initial terminal orientation should cover, in a gradual sequence:

1. What a terminal is and how it differs from the code editor and other in-game applications.
2. What the prompt represents, where the player currently is, and how to recognize the current working directory.
3. How files and folders are organized, including the difference between a filename, a folder, and a path.
4. How to inspect the current location and its contents using supported standard commands such as `pwd` and `ls`.
5. How to move between folders using `cd`, return to an appropriate location, and understand relative versus absolute paths when the player is ready.
6. How to read a text file using `cat`, and how to distinguish reading evidence from editing a script.
7. How to create and manage practice files and folders using supported commands, without risking protected mission evidence.
8. How to open the Python editor, save a script, run it through the terminal, and understand the relationship between the script filename and the command used to execute it.
9. How to use contextual help, recognize common command errors, and recover from a mistake.

Use recognizable Linux-like command conventions already specified in the terminal design. Teach only commands and options that the game actually implements. Clearly communicate that this is a safe, game-managed virtual environment, not unrestricted access to the Android device or host operating system.

### Python foundations

Freya introduces Python from the absolute beginning, with a practical progression appropriate for a first-time learner. The initial teaching sequence should include, when the player is ready:

1. What Python is, what a script is, and how the game executes the player's code.
2. The basic shape of a Python instruction and how to run a minimal first program.
3. `print()` and reading program output.
4. Text values (strings), quotation marks, and the difference between text and a variable name.
5. Variables, assignment with `=`, naming, and reusing stored values.
6. Basic input and output where supported by the mission environment.
7. Common syntax errors, spelling mistakes, missing quotes or parentheses, and how to read error feedback.
8. Conditions (`if` / `else`) and comparisons.
9. Lists, indexing, and understanding that Python list positions begin at zero.
10. Loops, including `for`, and progressively introducing `range()` and `enumerate()`.
11. Counting and inspecting data with tools such as `len()`.
12. Dictionaries, functions, file handling, and other concepts as later missions establish a genuine need for them.

This is a progression guide, not a requirement to expose every topic during the first sitting. The campaign must introduce concepts in context and at a pace supported by the player's demonstrated understanding. Do not advance merely because the player tapped Continue; meaningful practice and successful use should establish readiness.

### Opening tutorial as an in-world experience

- The first technical lessons take place after the approved A.E.G.I.S. recruitment and Freya-contact sequence. Preserve the two distinct opening notifications and all existing narrative constraints.
- Freya explains that the player is new to this work and explicitly reassures them that no prior terminal or programming experience is required.
- She guides the player through the operative application hub and explains Secure Messenger, the A.E.G.I.S. Browser/Intranet, Terminal, Code Editor, Dossier, Intelligence Database, and Operations as each becomes relevant. Do not expose every tool or capability prematurely.
- The first terminal exercises use safe practice files and a clearly identified practice workspace. The player must be able to tell practice material apart from mission evidence.
- The first Python exercises begin with tiny, understandable programs. The player types, runs, observes the output, and makes a small change to see how the behavior changes.
- Freya then connects the newly learned command or Python concept to a real action in the opening First Contact sequence. The tutorial and the mission must share the same files, interfaces, and execution rules; never teach a command in a disconnected mock screen that behaves differently from the actual terminal.
- The transition from guided practice to mission work should be gradual. Early objectives may include explicit scaffolding; later objectives should reduce assistance as the player demonstrates competence.
- The player must always have a way to review the relevant explanation or ask Freya for help without losing their current code, workspace, or mission progress.

### Adaptive guidance and learning integrity

- Track learning progress at the level of concepts demonstrated, not merely tutorial screens viewed. A player who has not successfully used a concept should receive suitable scaffolding when it next matters.
- Allow players to revisit foundational training and request explanations. Do not force experienced players to repeat lengthy explanations unnecessarily; provide a clear, optional accelerated path while preserving the ability to learn any concept they do not know.
- Evaluate mission work by actual execution and behavior, and verify the intended learning objective where appropriate. Do not accept a hard-coded output when the mission is explicitly teaching variables, loops, conditions, indexing, or another specific concept.
- Do not make typing speed, memorization of unexplained syntax, or guessing the developer's intended answer the measure of programming ability.
- Do not conceal required evidence or expected input from the player. Mission files, briefings, and Freya's guidance must provide a fair, discoverable path to the task.
- The in-game Python runtime, editor, terminal, virtual filesystem, file permissions, and mission evaluator must behave consistently with the explanations Freya gives. A tutorial must never instruct the player to use a file, command, or Python feature that the current environment does not support.
- Core training must work offline and must not depend on a live AI service. Freya's instructional dialogue and progression are authored and locally available. NEXUS may provide secondary technical reference or system diagnostics, but Freya remains the primary human mentor and teacher.

The intended outcome is that a complete beginner can enter Cyber Protocol without outside programming knowledge, learn terminal and Python fundamentals through play, and gradually become capable of completing increasingly independent technical operations.

## 23.2 — Canonical Design Compliance for Implementation

`GAME_CONCEPT.md` is the canonical product-design reference for implementation.

- Before writing or changing gameplay code, UI, dialogue, mission data, terminal behavior, Python execution, progression, save-state logic, or supporting architecture, review the relevant sections of this document and the approved narrative in `Story_Line.md`.
- Every implementation must preserve the approved player experience, character roles, Freya-led zero-prerequisite terminal/Python teaching, simulated offline A.E.G.I.S. Intranet, connected Messenger-to-file-to-Terminal workflow, story continuity, offline-first requirement, and safety boundaries defined here.
- Do not introduce a new feature, interaction, teaching method, story fact, mission requirement, or technical capability that contradicts this document or silently turns a provisional idea into canon.
- If a requested implementation reveals a genuine design gap or requires changing an established decision, update this document first (or in the same coherent change) and clearly identify the design decision before implementing dependent code.
- Code must implement the design; the design must not be retroactively reinterpreted to excuse behavior that contradicts the approved experience.
- When a feature is only partially implemented, document its actual status accurately. A screen or placeholder is not proof that the underlying gameplay, learning, persistence, or evaluation behavior works.
- All CI, APK, signing, and device-validation work must follow the workflow separation and permanent-signing requirements in §24. Android Tests must remain test-only; only the signed Release workflow may produce an APK for device testing.
- Keep changes on the active development branch and do not merge into `main` until the game is finished and the user explicitly authorizes the merge.


# 24. Android Release Signing, CI Separation, and Safe Updates

Cyber Protocol uses a permanent Android release-signing identity. This is a product-maintenance requirement, not merely a build convenience. The CI pipeline has a strict separation between automated tests and the APK intended for installation on physical devices.

## 24.1 — Android Tests workflow: tests only

- The **Android Tests** GitHub Actions workflow is exclusively a validation workflow.
- It runs the Python runner test suite and Android/Kotlin unit tests (including `testDebugUnitTest`).
- It must not run APK-producing tasks such as `assembleDebug`, `assembleRelease`, `bundleDebug`, or `bundleRelease`.
- It must not create, sign, upload, publish, or retain an APK artifact.
- It must not read, restore, or require the permanent release keystore or any release-signing secrets.
- It runs for pushes to `main` and the active development branch, for pull requests targeting `main`, and by manual dispatch where supported.
- A failed test run must be reported as failed; it must never create a device-test APK. Pull-request test runs never produce a Release APK.

## 24.2 — Android Release APK workflow: signed device artifact only

- The **Android Release APK** workflow is the only CI workflow authorized to produce an APK for physical-device testing or distribution.
- It runs only for pushes to `main` or the active development branch, or a manual dispatch explicitly targeting one of those branches; it must not run for pull-request events.
- It contains a mandatory source-validation job that runs the Python runner tests and Android/Kotlin unit tests for the exact source commit. The Release build job must depend on that job's success. A failed or cancelled validation job must prevent APK production.
- It builds the Release variant only (for example, `assembleRelease`). It must not build or publish a Debug APK.
- The resulting `app-release.apk` must be signed with the permanent release keystore and signing certificate. No debug key, newly generated key, or temporary replacement identity may be used.
- Restore the keystore only into the ephemeral CI runner, restrict its file permissions, validate the alias without printing secrets, and remove the temporary key material after the job.
- Verify the APK signature and confirm that the package identity, supported ABI, and `versionCode` match the release configuration before uploading the artifact.
- The artifact must contain only the signed Release APK intended for device testing. Keep artifact retention limited and do not upload signing material, intermediate keystores, or secret-bearing files.
- Both the validation job and Release build job must check out the exact same commit SHA. If a newer commit is pushed while validation is running, the in-progress release must remain tied to its own tested commit.

## 24.3 — Permanent identity and safe updates

- The release application must retain its existing `applicationId` across updates. Do not change the package identity after the first externally distributed signed build unless a separate migration strategy is deliberately approved.
- Every release update must be signed with the same permanent release keystore and signing certificate used for the previous release. Never generate a replacement key for routine updates.
- The keystore and all passwords are private release credentials. Store them only in the maintainer's secure offline backup and the repository's protected GitHub Actions Secrets. Never commit the keystore, Base64 keystore data, passwords, or signing material to Git, logs, artifacts, documentation, or this public design file.
- Every published update must have a `versionCode` strictly greater than the previously distributed build. A version name may describe the release, but Android uses `versionCode` to determine update order.
- A signed update should install over the existing app without uninstalling it when the application ID and signing identity match and the version code is higher. Preserve local campaign data during normal updates.
- Database and save-state changes must use explicit, tested migrations. App updates must not silently reset campaign progress, player code, evidence, settings, or workspaces. Back up and recover local state safely when a migration fails.
- Never recommend uninstalling a user's existing installation as the first response to an update failure. Diagnose package identity, signing-certificate mismatch, version-code ordering, device compatibility, and installer errors first. Warn about data loss before any destructive recovery step.
- Debug builds may be used for internal development diagnostics only when separately and explicitly needed; they are never the project’s device-test, acceptance, or distribution artifact. The permanent release key must not be assigned as the Debug signing identity.
- The release process is not complete until CI verifies the Release APK signature and a physical Android device confirms both clean installation and in-place update over the prior signed Release build, including preservation of local game data. All physical-device acceptance testing uses the signed Release APK, never a Debug APK.

The permanent release key must be backed up securely by the maintainer. Losing it may make future in-place updates impossible for installations signed with that key.
