# Cyber Protocol — Project Bible

## 1. Identity and premise
Cyber Protocol is a Cyber Intelligence RPG, Educational Programming Game, Adventure, Puzzle, and Simulation. The player begins as an ordinary person—not a professional hacker—and receives a mysterious message from a fictional intelligence organization headquartered in the Philippines. The organization investigates fictional transnational corruption networks across Southeast Asia. The player contributes through programming, analysis, information processing, and technology. All organizations, characters, and principal cases are fictional.

## 2. Confirmed canon
- Six divisions: Intelligence, Cyber Operations, Investigation, Technical Division, Field Operations, Command.
- Player arc: The Ordinary → Recruitment → Initiation → Field Analyst → Cyber Operative → Advanced Operations.
- Python is the first programming language. Future candidates: JavaScript, C/C++, SQL, Bash.
- LEARN explains; PRACTICE experiments; MISSION challenges the player to write code.
- Run executes only. Submit evaluates.
- Hints have three levels and must not disclose the complete solution.
- Operations use fictional simulations and authorized training targets.

## 3. Open decisions
Official organization name, character names and biographies, protagonist customization, antagonist structure, visual perspective, dialogue branching depth, engine/platform expansion, and campaign length are undecided. Suggestions in this repository remain provisional until approved.

## 4. Story Bible
### World
A fictionalized Southeast Asian setting with headquarters in the Philippines. The tone is a grounded cyber-intelligence thriller with investigation, institutional tension, character relationships, and long-term mystery. Real-world dynamics may inspire atmosphere; fictional actors carry all plot allegations.

### Organization
| Division | Responsibility |
|---|---|
| Command | Strategy, authorization, oversight |
| Intelligence | Collection, analysis, assessment |
| Cyber Operations | Authorized simulated digital operations |
| Investigation | Evidence handling and case development |
| Technical Division | Tools, infrastructure, technical support |
| Field Operations | Fictional physical-world operations |

Reporting hierarchy, official name, insignia, internal culture, and division heads remain open.

### Character Bible template
Every recurring character defines identity/role, background, personality, motivation, strengths, limitations, secrets, relationships, character arc, and knowledge boundaries. Initial role slots: recruiter, mentor, technical specialist, intelligence analyst, investigator, field operative, and command representative. No names are canon yet.

### Conflict layers
1. Case conflict: immediate investigative question.
2. Character conflict: personal motives, trust, relationships.
3. Organizational conflict: competing interpretations and priorities.
4. Network conflict: connections between cases.
5. Central conflict: hidden structure behind the campaign.

### Case structure
Opening incident → briefing/authorization → initial evidence → investigation stages → coding missions → discoveries → complication/counter-evidence → resolution/debrief → consequences → clue for the next case.

### Continuity rules
Maintain a timeline; track character knowledge; track evidence provenance (confirmed, inferred, disputed); do not let characters act on information they do not possess; do not implicate real people or organizations; do not make the player an expert before the curriculum supports the task.

## 5. Storyline-to-curriculum relationship
Use the chain: Story Arc → Chapter → Case → Mission → Programming Objective. Story creates the need; the mission defines the coding task; the curriculum provides the knowledge; evaluation verifies the relevant concept; the outcome changes evidence, trust, or story state.

Provisional case concepts: Missing Funds, Silent Network, The Hidden Ledger, Inside the Organization. They are not finalized canon.

## 6. Learning design
### LEARN
Concept, syntax, rules, complete runnable example, expected output, explanation, common mistakes, and links to practice/missions.

### PRACTICE
Code modification, repeated Run, cause/effect experiments, progressive hints, and error explanations. No mission reward or completion.

### MISSION
Briefing, situation, objective, required concepts, editor, Run, terminal, hints, Submit, evaluation, outcome. Starter code is blank or TODO scaffold only.

### Evaluation
Test behavior across multiple fixtures when generalization matters. Check concept usage when it is an explicit learning objective. Avoid brittle source-text matching. Accept alternative valid implementations where practical. Keep beginner tests small and explainable. Errors are learning opportunities, not permanent campaign locks.

## 7. RPG progression
Track technical competency, organizational trust, and character development separately. Candidate systems: XP, levels, chapter/mission unlocks, achievements, skill statistics, mission history, difficulty, organizational access, and bonus challenges. Rewards are one-time; replay remains available for learning.

## 8. Architecture
- core/model: shared domain models.
- data: campaign content and player progress.
- domain/story: unlocks and narrative state.
- domain/learning: lesson mapping and learning flow.
- domain/mission: objectives, evaluation contracts, test fixtures.
- execution: language-neutral runner interface and language adapters.
- ui: screens, editor, terminal, menus.
- progression: XP, trust, achievements, unlocks.
- player data: versioned save and migration.

Current Android implementation is organized into models, repositories, story rules, execution adapter, and Compose UI. Python runs through Chaquopy. Future language adapters must not force story or progression to depend on Python.

## 9. Security boundary
All cases and targets are fictional. The current embedded Python runner is a restricted teaching prototype, not a hardened sandbox. It executes in the app process. Do not run remote or hostile code. Production requires process isolation, wall-clock/CPU/memory limits, ephemeral filesystem, network denial by default, output caps, and escape/DoS testing.

## 10. Roadmap to v1.0
1. **Pre-production:** approve world rules, organization identity, cast, campaign arc, Chapter 01 beats, curriculum outcomes, and platform strategy.
2. **Core foundation:** navigation, content repository, player profile, versioned saves, domain contracts.
3. **Programming engine:** language-neutral execution, Python Run/Submit separation, diagnostics, resource limits, concept-aware evaluator, security review.
4. **Learning system:** Python fundamentals, examples, practice experiments, hints, error explanations, curriculum mapping.
5. **Narrative vertical slice:** complete Chapter 01, recruiter/mentor, dialogue, evidence, debrief, consequences.
6. **RPG integration:** XP, levels, trust, access unlocks, achievements, history, relationships.
7. **v1.0 candidate:** end-to-end campaign, content/evaluator/UI tests, Android device tests, sandbox hardening, save migration, accessibility, localization, performance, signing, and documentation.

A 3–5 chapter campaign is a planning target, not a fixed commitment. Release quality is more important than feature count.

## 11. Build and test
Requirements: JDK 17, Android SDK Platform 36, Build Tools 36.0.0, Gradle 8.13, Python 3.13.
```bash
python -m unittest discover -s tests -v
gradle --no-daemon assembleDebug
```
APK: `app/build/outputs/apk/debug/app-debug.apk`.

## 12. Device test checklist
1. Install and launch the debug APK.
2. Open LEARN and verify lesson text/examples.
3. Open PRACTICE, run print(), and confirm no mission reward.
4. Open Mission Board and select M01.
5. Press RUN and verify execution output only.
6. Press SUBMIT and verify separate evaluation.
7. Submit incorrect code and inspect feedback.
8. Complete M01, restart the app, and verify progress.
9. Replay M01 and verify XP is not awarded twice.
