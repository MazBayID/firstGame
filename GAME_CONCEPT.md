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


## 3.4 Language and Localization — English-First

Cyber Protocol is an **English-first, Indonesian-supported** game.

- **Default for a fresh installation:** English, regardless of the Android system language. The game must not silently choose Indonesian based on device locale.
- **Supported alternative:** Bahasa Indonesia, selectable from the in-game Language menu.
- **Persistence:** the player's language choice is stored locally and remains active after the app is closed or the device restarts.
- **Existing progress:** changing language must never reset, migrate, or otherwise alter campaign progress, evidence, code drafts, rank, clearance, or unlocks.
- **Content parity:** all player-facing content must be available in both languages before the bilingual experience is declared complete. This includes menus, settings, notifications, dialogue, briefings, objectives, hints, mission feedback, reports, evidence labels, debriefs, and accessibility text.
- **English is the source language:** new authored content is written and reviewed in English first; the Indonesian version follows and must preserve meaning, tone, uncertainty, and gameplay requirements.
- **Technical authenticity:** Python code, identifiers, syntax, standard command names, file paths, and literal output expected by evaluators must not be translated. Explanatory text around them may be localized.
- **No mixed-language completion claims:** a language option must not be presented as fully supported while substantial player-facing content remains in the other language. Localization coverage must be tracked and tested by screen/content domain.

The localization implementation should use stable message keys and separate language resources rather than scattering language conditionals through UI code. Authored story and mission content must also have explicit localized variants while retaining stable mission/evidence identifiers. Missing translations must be detected during development and tests; silently falling back to a different language is not an acceptable release behavior.

The current repository still contains Indonesian hard-coded UI and campaign content. This policy defines the approved target; it does not claim that English localization is already implemented. See [Localization Plan](docs/LOCALIZATION_PLAN.md) for rollout and acceptance gates.

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

# 7. Core Gameplay — Intelligence-Driven RPG

Cyber Protocol uses a **Guided Intelligence RPG** approach: the campaign provides clear narrative context, objectives, and fair guidance, while the player retains meaningful freedom in how to investigate and which relevant tools to use.

The central gameplay unit is **a problem to understand and resolve**, not a checklist of applications to visit. Messenger, Browser, Terminal, Code Editor, Dossier, Intelligence Database, Analysis, and Operations are tools inside one connected A.E.G.I.S. environment. They are not mandatory stops in a fixed sequence.

The player fantasy is to become a capable intelligence operative: receive incomplete information, investigate its source and reliability, make sense of evidence, decide what the organization should do, and understand the consequences. Programming is one important operational capability, not the purpose of every mission.

## 7.1 Core Gameplay Loop

The campaign follows five experience phases. These are design phases, not five mandatory screens or applications.

1. **INTELLIGENCE BRIEFING** — A story event, contact, report, alert, or assignment establishes what happened, why it matters, what is known, and what remains uncertain. The player receives a clear primary objective and any essential constraints.
2. **INVESTIGATION** — The player examines relevant sources and chooses reasonable approaches. They may read messages and dossiers, inspect files, browse the fictional intranet, query available records, ask an authorized contact, or use technical tools when the case calls for them. The exact toolset depends on the mission.
3. **INTELLIGENCE ANALYSIS** — The player compares sources, identifies inconsistencies, organizes findings, tests hypotheses, and distinguishes verified facts from assumptions. Analysis should help the player reason; it must not automatically solve the case.
4. **OPERATIONAL DECISION** — The player submits a conclusion, selects an action, requests verification, or explains that the available information is insufficient. Decisions must be grounded in what the player could reasonably discover.
5. **DEBRIEFING & PROGRESSION** — A.E.G.I.S. evaluates the relevant criteria, explains what the player established and what remains unresolved, records appropriate evidence/report outcomes, and advances authored story and progression gates.

A mission may compress, repeat, or omit phases when appropriate. A short narrative mission may need no technical investigation. A focused coding exercise may move directly from briefing to technical work and debrief. A complex case may revisit investigation after analysis exposes a gap. Never force all five phases into a rigid five-screen template.

## 7.2 Guided Freedom — Player Agency Without an Unrestricted Sandbox

The primary structure is **Guided Intelligence RPG**, not a fully open-ended simulator.

- The story establishes the case, stakes, primary objective, and the player's authorized scope.
- The game makes relevant evidence and available tools discoverable without requiring the player to guess hidden controls.
- Players may choose the order of reasonable investigative actions and use any supported tool that fits the task.
- Optional sources, deeper inspection, and corroboration may improve confidence or reveal context, but must not become arbitrary busywork.
- Essential story progression must not depend on an obscure optional clue unless the mission clearly teaches the player how to find it.
- The game may offer contextual prompts, Freya's graduated guidance, and NEXUS diagnostics. Guidance should reduce as the player demonstrates competence, without withholding essential information unfairly.
- Do not simulate freedom through meaningless choices. A choice is meaningful only when it changes the player's approach, the information available, the quality/confidence of the result, or a later authored outcome.
- The initial version keeps predefined dialogue replies cosmetic, as established in §3.3. Do not add relationship meters or persistent dialogue branches without a separate approval.

This approach should feel guided but not like a walkthrough. The player knows what problem they are solving, but is not told every tap or every line of code in advance.

## 7.3 Mission Archetypes

Every mission has one **primary archetype**. It may combine secondary activities, but the primary archetype determines its dominant player experience and evaluation emphasis.

| Archetype | Primary activity | Example |
|---|---|---|
| Narrative | Experience a story event, communicate, understand motives and context | Receive a new assignment from Freya |
| Investigation | Find, inspect, compare, and corroborate information | Trace the origin of an anomalous transmission |
| Technical | Use code or a technical tool to solve a defined problem | Write Python to parse a set of event records |
| Analytical | Interpret evidence, test a hypothesis, and justify a conclusion | Determine what two conflicting logs actually establish |
| Operational | Choose a proportionate next action under stated constraints | Continue monitoring, request corroboration, or pause an operation |

Archetypes are not separate game modes and do not require separate application systems. A mission can be, for example, an Investigation mission with a small Technical task and an Analytical conclusion. Avoid making every mission a blend of all five; one clear dominant activity helps pacing and player expectations.

## 7.4 Evidence & Investigation Engine — Core Cross-Mission System

The Evidence & Investigation Engine is the central connective system for investigative gameplay. It gives evidence a consistent identity and history across Messenger, Browser, VFS, Terminal, Dossier, Intelligence Database, Analysis, Operations, and debriefs.

An evidence record should be able to represent, as appropriate to its source:

- A stable evidence identifier and title.
- Its content or a reference to the underlying mission asset/data.
- Source and provenance: who or what supplied it, through which in-world channel, and in which mission/context it was obtained.
- Relevant timestamps and source metadata.
- A concise finding/summary that does not overstate what the source proves.
- An epistemic status such as unverified, corroborated, verified, or disputed, where that distinction is supported by the case.
- Links to related evidence, reports, hypotheses, or mission objectives when authored.
- Review/update history for status changes that require an audited action.

The exact status vocabulary and transitions must be defined by the case and supported by the data model; do not automatically promote evidence to VERIFIED merely because a file was opened or a script ran.

**Evidence principles**
- Finding information is not the same as verifying it.
- A technically correct result does not automatically prove an interpretation or a cause.
- Two records that agree may corroborate a claim only when their sources are sufficiently independent and relevant.
- Conflicting evidence should remain visible; do not silently discard inconvenient records.
- Preserve original source material. Analysis creates findings or derived records; it must not rewrite source evidence.
- Evidence status changes must be attributable to an explicit, testable review action.
- Optional evidence may enrich the case, but the player must have enough discoverable information to complete the authored primary objective fairly.

**Application responsibilities**
- Messenger and Browser deliver or expose sources.
- The VFS preserves the actual mission file, permissions, and provenance.
- Terminal and Code Editor inspect or process permitted data.
- Dossier presents authored case context and documents.
- Intelligence Database indexes records and findings.
- Analysis helps compare and organize evidence.
- Operations presents objectives, current state, submission, and debrief access.

These are responsibilities, not a requirement to open every app in every mission. Reuse one underlying evidence record and source asset across these views; do not create disconnected copies that can disagree.

## 7.5 Technical Work as a Means, Not a Mandatory Gate

- Use Python, Terminal, SQL, networking concepts, C++, or IoT only when they naturally solve a mission problem or teach a planned competency.
- A mission must not require code merely to make the game appear technical.
- A technical mission must explain why the operation needs the technical task and what its result means in the case.
- Not every mission requires the player to write code. Narrative, investigative, analytical, and operational missions may be completed through other supported interactions.
- When code is required, introduce the necessary concept at the player's level and provide fair graduated hints.
- The Code Editor remains where the player writes and edits code; Terminal remains where code is executed and output/errors are inspected.
- **RUN and SUBMIT remain separate.** RUN executes the exact current source and displays actual output/errors. SUBMIT evaluates the authored mission criteria. A successful run alone never completes a mission.
- The evaluator must distinguish technical correctness from the quality of the player's investigative interpretation.

## 7.6 Mission-to-Mission Code Clues

The opening learning campaign may use authored code/syntax clues to foreshadow the next technical concept. These clues should emerge naturally from a story event, evidence, terminal output, or debrief.

- Clues must match the player's current learning level and create curiosity without revealing the next solution.
- Use a code clue when the next mission genuinely advances a technical concept; do not force a syntax easter egg into every narrative or operational mission.
- A clue is a narrative/learning bridge, never an unfair blocker. The next mission must still teach or explain its new concept.
- Preserve the established clues in First Contact unless a later approved story revision replaces them.

# 8. Learning Through Operations

Programming and technical disciplines are in-world capabilities. They are introduced when a story problem gives the player a reason to learn and apply them.

Potential progression:

| Learning area | Example concepts | Possible fictional use |
|---|---|---|
| Python fundamentals | `print()`, text, variables, types | Read and produce basic system records |
| Input and logic | `input()`, comparisons, `if/else` | Interactive verification and decisions |
| Iteration | loops, lists, indexing | Review batches of event records |
| Functions and data | functions, strings, dictionaries | Reusable analysis procedures |
| Data processing | filtering, sorting, structured data | Intelligence analysis |
| SQL | queries and relational data | Search a simulated case database |
| Networking | addressing, protocols, request/response | Understand simulated infrastructure |
| C++ | types, functions, memory and embedded foundations | Technical Division and device systems |
| IoT | sensors, microcontrollers, signals | Fictional device telemetry and monitoring |
| Automation | Python, JavaScript, Bash concepts | Repetitive authorized workflow automation |

This is a candidate learning map, not a requirement to teach every topic in a fixed order. Python is the first technical track. The campaign determines when other disciplines become relevant; do not overload early missions.

## 8.1 Hybrid Mission Evaluation

Evaluation is deterministic and configured for the mission's primary archetype and objectives. It must not rely on an unconstrained AI model as the authority for core mission completion.

- **Technical criteria:** validate required behavior, relevant concept use where it is the learning objective, execution results, and task constraints. Accept different implementations when they satisfy the defined requirements.
- **Investigative criteria:** evaluate whether the player found the required source(s), used relevant evidence, distinguished observation from inference, and reached a conclusion supported by the available material.
- **Analytical criteria:** evaluate required relationships, comparisons, uncertainty handling, and the reasoning criteria authored for the case. Accept multiple defensible conclusions where the evidence permits them.
- **Operational criteria:** evaluate whether the selected action is allowed, proportionate to the known facts, and consistent with the stated mission constraints.
- **Narrative criteria:** evaluate authored progression gates and required story interactions without pretending cosmetic dialogue choices have consequences.
- **Combined missions:** report technical and reasoning dimensions separately. A correct script does not automatically make an interpretation correct; a sound observation does not excuse a failed technical requirement.
- **Feedback:** explain which criteria were met, which need attention, and why. Give contextual hints or another attempt instead of a generic pass/fail response.
- **Mission configuration:** define evidence requirements, acceptable outcomes, optional findings, evaluation rules, and progression effects explicitly. Do not judge a player's conclusion using criteria that were never discoverable or taught.

## 8.2 Failure, Uncertainty, and Retry

The initial version uses a retry-first, learning-oriented model.

- A failed technical submission explains which requirement was not met and preserves the player's working context.
- An unsupported investigative conclusion should explain which evidence is missing, conflicting, or insufficient; it must not label a reasonable uncertainty statement as failure.
- Where appropriate, the player may request more information, inspect another source, or submit a qualified conclusion such as "insufficient evidence."
- Failure does not immediately end an operation or impose a narrative punishment in the initial version.
- A failed attempt does not reduce rank, XP, relationship values, mission access, or story progress. Do not add retry-counter penalties or consequence branches at this stage.
- Provide graduated hints: conceptual reminder first, then a more specific direction, then stronger help only when needed.
- Preserve the player's work and relevant progress between attempts.
- Explain success as well as failure: show what the player demonstrated and what remains unresolved.
- A mission completes only when its authored completion criteria are met. RUN remains available for experimentation; SUBMIT evaluates the current eligible work.

More consequential operational failure, time pressure, or story branching may be considered in a later design phase, but is not part of the initial version unless separately approved.

## 8.3 Teaching and Evaluation Principles

- Freya is the primary human teacher for terminal use and Python. She introduces concepts from zero, explains what commands and syntax mean, demonstrates when useful, and guides hands-on practice.
- NEXUS provides secondary diagnostics, reference material, and contextual technical support; it does not replace Freya or solve the case.
- LEARN provides an optional, reviewable reference for concepts already introduced or available to study. It supplements Freya's instruction.
- PRACTICE provides safe exercises where the player can experiment without risking protected mission evidence or campaign state.
- MISSION applies learned skills to a story-driven objective.
- Teach purpose, behavior, expected output, and error interpretation—not only exact text to type.
- Hints are graduated and should support learning rather than reveal a complete answer by default.
- Evaluators check actual execution, required behavior, and intended concept use where appropriate, not merely matching output text.
- Keep tutorials, mission files, terminal commands, Python capabilities, and evaluator criteria consistent and testable. Never instruct the player to use an unavailable file, command, or language feature.

# 9. Player Progression

Progression combines organizational standing with demonstrated capability. Rank and clearance remain the visible career path; competency records describe what the player has actually learned and demonstrated.

The current proposed rank sequence remains:

1. Civilian
2. Recruit
3. Initiate
4. Junior Analyst
5. Field Analyst
6. Cyber Operative
7. Senior Operative
8. Special Agent
9. Elite Operative

## 9.1 Rank and Clearance

Rank represents the player's recognized standing and career advancement within A.E.G.I.S. Clearance represents authorization to access specific divisions, records, tools, and operations. They may be related, but they are not interchangeable.

Unlocks may include:
- New divisions and operational areas.
- More complex dossiers, cases, and reports.
- Additional tools and simulated environments.
- New technical learning tracks.
- New characters, story responsibilities, and authored campaign content.
- Achievements, records, and profile distinctions.

Do not grant sensitive access solely because the player accumulated generic XP. Use authored story gates and explicit clearance requirements where appropriate.

## 9.2 Demonstrated Competencies

Track two broad competency dimensions:

- **Technical Competency:** demonstrated ability in relevant technical areas such as Python, terminal use, data handling, networking, SQL, C++, or IoT.
- **Intelligence Competency:** demonstrated ability to inspect sources, preserve provenance, corroborate information, compare records, identify contradictions, distinguish facts from inference, and communicate uncertainty.

Competency records should be evidence-based: record which skill or criterion was demonstrated, in which mission or exercise, and at what level. Do not invent a large skill tree, hidden numerical attributes, or arbitrary stat bonuses in the initial version.

A player may have a high rank while still developing a technical skill, or demonstrate strong analysis while remaining a beginner programmer. The game should represent these profiles honestly.

## 9.3 XP, Rewards, and Replay

- XP rewards successful authored milestones and meaningful optional achievements; do not reward repetitive app visits or repeated submissions.
- Rank thresholds, clearance gates, achievements, mission completion, and story flags must be deterministic and persistent.
- Rewards must not be granted repeatedly by loading, replaying, or resubmitting a completed mission.
- Optional evidence and deeper investigation may improve the debrief or award a specific achievement when authored, but must not create arbitrary grind.
- Keep progression readable. The player should understand why a rank, clearance, competency record, or reward changed.

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

The following direction is now approved as the gameplay foundation: **Guided Intelligence RPG with an Intelligence-driven core loop** (§7–§9). Do not treat the core approach as undecided or revert to a rigid application checklist.

The following details remain open and must be designed deliberately before implementation:
- Full campaign GDD with chapter-by-chapter missions, objectives, authored scenes, and narrative scripts.
- Exact evidence schema, provenance fields, status vocabulary, permitted status transitions, relationship links, and audited review actions.
- Detailed mission authoring contract for primary archetype, required/optional evidence, acceptable conclusions, uncertainty handling, evaluation criteria, hints, and debrief.
- Case-specific investigation flows, optional leads, corroboration paths, and fair completion conditions.
- Detailed rank thresholds, clearance matrix, competency levels, XP economy, optional rewards, and replay policy.
- Whether and when later campaigns introduce consequential operational choices, time pressure, or branching outcomes.
- Named human characters, histories, motivations, relationships, and dialogue beyond already approved canon.
- Detailed A.E.G.I.S. leadership and internal politics.
- Final menu and operational UI wireframes.
- Localization resource architecture, translation workflow, and complete bilingual content parity.
- NEXUS dialogue model, context rules, and offline/online implementation boundaries.
- Final technical architecture and Android performance strategy.
- Audio direction, accessibility, and cinematic asset pipeline.

The five mission archetypes and five-phase gameplay loop are design tools, not a mandate to create five separate modes or screens. Begin implementation with the smallest reusable vertical slice that demonstrates a complete evidence-backed case. Preserve First Contact's approved story, characters, beginner-friendly purpose, and unresolved RX-14 evidence premise. The previous requirement to preserve eight evaluated Python missions as a fixed, linear gameplay sequence is superseded by the latest player decision: redesign, merge, make optional, or retire legacy mission mechanics whenever they conflict with the Guided Intelligence RPG. Keep Python teaching progressive and accessible, but require code only when it serves the current learning objective or operation. Update evaluator IDs, save migration, and regression tests deliberately if the mission structure changes; do not preserve obsolete mechanics merely for compatibility.

Develop open details systematically. Mark unapproved specifics as proposals until the user approves them.

---

**North Star:** Make the player feel that they are living inside a cinematic intelligence operation, learning and applying real technical concepts as an operative of A.E.G.I.S. The campaign guides the player toward meaningful problems; the player investigates with relevant tools, reasons from evidence, and understands the result. Every screen, story beat, mission, and system must serve that experience.

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


# 23. First Contact — Guided Case, Not a Fixed Coding Checklist

First Contact remains the guided beginner chapter and the player's first introduction to A.E.G.I.S. Its approved narrative, character introductions, recruitment sequence, fourteen story beats, and unresolved RX-14 discrepancy remain the narrative foundation.

The gameplay mechanism is now governed by the Guided Intelligence RPG:

- The central unit is the RX-14 problem: two receipt records show a four-second timestamp difference. The player investigates what the records establish, what they do not establish, and what a proportionate next step would be.
- The five experience phases—Intelligence Briefing, Investigation, Intelligence Analysis, Operational Decision, and Debriefing & Progression—are flexible case phases, not mandatory app visits or separate screens.
- The player may inspect relevant messages, source files, intranet records, dossier material, and database entries in a useful order. App navigation is not itself an objective.
- The original receipt assets remain immutable. The derived discrepancy is a separate finding linked to its sources. A timestamp difference does not prove tampering or identify a cause.
- The player must be allowed to report that the available evidence is insufficient. A defensible uncertainty statement is not a failure.
- A small Python task may be used when it genuinely helps inspect or compare records. It must not be inserted merely to force every concept into the case.

### Superseded legacy mission sequence

The former eight evaluated Python missions are no longer a mandatory linear progression contract. Their learning concepts remain useful as the beginner Python curriculum, but their old mission boundaries, evaluator IDs, and completion gates may be redesigned:

- Keep essential beginner instruction with Freya: terminal basics, reading files, variables, output, and understanding errors.
- Move practice that is useful but not essential to the RX-14 decision into optional PRACTICE or LEARN activities.
- Retain a technical mission only when code is a natural and meaningful way to solve the operational problem.
- Do not force `for`, `if`, `len()`, `enumerate()`, indexing, `int()`, or `abs()` into the case merely to preserve the old evaluator sequence.
- If a legacy evaluator is retired or its mission is merged, update catalog references, story transitions, save migration, tests, and documentation together. Do not leave obsolete IDs as hidden progression blockers.
- Preserve player drafts and already-earned campaign progress where a migration can do so safely. Explain any unavoidable incompatibility before shipping it.

This supersedes the former rule that all eight evaluated Python missions must remain unchanged. It does not authorize changing the approved recruitment premise, Freya/NEXUS roles, or the unresolved meaning of RX-14 without a separate story decision.

---
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
- The first terminal exercises use safe practice files and a clearly identified practice workspace. The player must be able to tell practice material apart from mission evidence. Freya introduces `help`, `ls`, and `cat` before asking the player to inspect a mission file.
- The first Python exercises begin with tiny, understandable programs. Freya explains variable names, assignment with `=`, strings and quotation marks, and displaying a value with `print()`. The player types, runs through the Terminal, observes the output, and makes a small change to see how behavior changes.
- For First Contact's first signal task, Freya explains that `message_fragment.txt` is a text file, not a Python variable. The player downloads the shared Messenger/Browser asset, reads it with `cat message_fragment.txt`, then assigns the discovered text to `signal` and displays it.
- Code Editor RUN must save the source to the authorized mission script and open the dedicated Terminal to execute it. Browser Mission Control is the only mission submission interface; Freya explains that RUN and SUBMIT are different actions.
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
- All CI, APK, signing, and device-validation work must follow the source-validation and explicit Final Release process in §24. Development commits run source tests without producing an installable APK; a manually approved Final Release builds, tests, and publishes one exact signed APK.
- `main` is the sole long-lived development and release branch. Do not maintain a separate permanent development branch or a parallel APK-producing workflow.


# 24. Source Validation and Explicit Final Release

Cyber Protocol is developed as a complete, integrated game before release packaging. **Ordinary development commits do not produce APKs.** A Final APK is a separate, explicitly requested release artifact and must pass the complete release pipeline using the exact APK that is published.

## 24.1 — Development source validation

- Pushes to main run Python runner tests and Release-variant Android/Kotlin unit tests.
- Pull requests targeting main run source-level tests only; they do not build installable APKs or access release-signing secrets.
- A failed or incomplete source-test run blocks the relevant validation workflow.
- Focused unit/domain tests are guardrails during feature development. Broader suites run at coherent integration checkpoints.
- Do not spend feature batches producing development APKs, polishing CI, or treating compilation as proof of a complete player experience.
- Source-test success does not mean the game has passed end-to-end gameplay, emulator, or physical-device acceptance.

## 24.2 — Explicit Final APK

- A Final APK may be built only after the integrated game meets the complete-game acceptance criteria in `docs/REBUILD_STATUS.md` and the player explicitly requests release packaging.
- A deliberate `workflow_dispatch` on main with `final_release=true` starts the final pipeline.
- The workflow reruns source tests before packaging.
- CI builds exactly one installable APK using the production Release configuration and the permanent release-signing identity.
- CI validates package identity, monotonically increasing versionCode, supported ABIs, permanent signing certificate, and expected production configuration.
- The Android Emulator smoke test installs and launches that exact signed APK. It must confirm that the app process remains alive.
- CI records the APK SHA-256 before smoke testing and verifies the file is unchanged afterward. The exact tested APK and checksum manifest are published together.
- The final artifact name must clearly identify it as **FINAL — EMULATOR SMOKE-TESTED**. Its artifact status/summary must state that the exact published APK passed the emulator smoke test.
- A failed, skipped, cancelled, or incomplete required final check blocks publication. No artifact may be labeled final unless every required check passed.
- Do not rebuild or re-sign the APK after smoke testing. If validation fails, correct the issue and start a new Final Release run.

## 24.3 — Permanent signing identity

- The Final APK uses the project's permanent Android release keystore. Every published update must use the same key and certificate.
- Restore the keystore only inside the ephemeral Final Release CI runner, restrict permissions, validate the alias without printing secrets, and remove temporary key material after the job.
- Store signing credentials only in a secure offline backup and protected GitHub Actions Secrets. Never commit or publish the keystore, passwords, or signing material.
- Losing the permanent key may make future in-place updates impossible for installations signed with that key.

## 24.4 — Safe updates and device acceptance

- Retain the existing applicationId across updates. Every Final APK must have a versionCode greater than the previously distributed Final APK.
- Preserve local campaign data, player code, evidence, settings, and workspaces during normal updates. Use explicit tested migrations for save-schema changes.
- Diagnose package identity, signing-certificate mismatch, version ordering, device compatibility, and installer errors before recommending uninstall. Warn about data loss before destructive recovery.
- Physical-device acceptance uses the exact signed Final APK: test clean installation, in-place update over the previous signed build, and preservation of local game data.
- Emulator smoke testing is not a substitute for physical-device acceptance. A Final APK is **CI-final and emulator-smoke-tested** after the final pipeline passes; full device acceptance/production readiness still requires physical-device checks.
