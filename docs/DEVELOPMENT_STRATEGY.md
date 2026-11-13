# Cyber Protocol — Development Rules

**Canonical engineering rules:** this document is the single source of truth for day-to-day development workflow and engineering policy.
**Last revised:** 2026-11-13
**Authority:** explicit player-approved project direction. Product canon remains in `GAME_CONCEPT.md` and `Story_Line.md`.

## 1. Decision hierarchy

1. The player's latest explicit direction controls current priorities and approved creative decisions.
2. `GAME_CONCEPT.md` and `Story_Line.md` define product identity, approved story canon, and continuity.
3. This document defines engineering workflow and development rules.
4. Feature plans, audits, status reports, and acceptance checklists describe implementation details; they do not silently override this policy.

When documents conflict, follow the higher authority and update the stale document when practical. Ask before changing established story canon or making a major product-direction decision.

## 2. Development principles

- Prioritize a complete, coherent, playable experience over isolated screens, cosmetic CI work, or APK count.
- Work in connected player-facing slices. Inspect existing architecture, contracts, save behavior, and adjacent flows before making changes.
- Use sound engineering judgment. Refactoring or changing functioning code is allowed when it materially improves correctness, maintainability, integration, or delivery.
- Prefer a working, complete result over endless polish. Do not expand scope without a concrete reason.
- A button, screen, stub, passing unit test, successful compilation, or APK is not by itself proof that a feature is complete.
- Preserve approved story facts, character knowledge, unresolved questions, and evidence boundaries. Do not invent a culprit or unsupported explanation for RX-14.

## 3. Current implementation priority

Complete Campaign 01 — FIRST CONTACT and its RX-14 case from a fresh save through a meaningful chapter debrief. Ensure the player can discover and complete required objectives without developer instructions or hidden navigation.

The integrated journey includes story and character communications, briefing/objectives, source discovery and provenance, analysis and uncertainty, operational decisions and feedback, connected A.E.G.I.S. apps, progression, debrief, archives, and save recovery.

Future story concepts, characters, and campaign outlines may be brainstormed or designed at any time. Their implementation remains behind the active approved priority unless the player changes that priority. Concept design does not automatically approve new canon.

## 4. Build, APK, and smoke-test policy

- Ordinary pushes to `main` run source-level checks appropriate to the changed code. Pull requests run the same relevant source checks.
- A Debug Preview APK may be built and shared at a useful review checkpoint, or when the player requests one. Do not require the entire game or release candidate to be complete first.
- A successful APK build and package validation are sufficient to hand a Debug Preview APK to the player. Emulator smoke testing is optional and must not block preview delivery.
- Do not run a costly emulator smoke test on every push. Run it when investigating a startup/install risk, validating a release candidate, or when specifically requested.
- Clearly report what was verified: source tests, build, package metadata, emulator install/launch, and physical-device behavior are distinct checks. Never imply one proves another.
- Physical-device testing is performed by the player at practical checkpoints and is not required for every commit or small feature batch.
- Final/release packaging follows the separate release workflow and its explicit approval and release checks. A Debug Preview is never described as a final release.

## 5. Risk-based testing

Choose tests according to the change's blast radius and failure cost.

- Small isolated logic or copy change: focused tests or checks for the affected behavior.
- Domain, navigation, evaluator, persistence, localization, or cross-app integration change: relevant unit tests plus affected integration/regression tests.
- Save schema, migration, progression, permissions, or shared state change: include backward-compatibility, recovery, and regression coverage proportionate to the risk.
- Major integration checkpoint: run the broader Python and Android source suites.
- Release candidate: run full relevant regression, exact-artifact validation, release smoke tests, and the physical-device acceptance plan as applicable.

Do not test every imaginable failure after every edit. Do not skip a high-impact regression merely to save time. Record important skipped or unavailable checks honestly.

## 6. Incremental localization

- English remains the authoring/source language; Bahasa Indonesia is a supported localized language.
- Build localization in stages alongside features. Keep stable IDs, evaluator contracts, code, paths, and persisted values language-neutral.
- A feature should provide both language variants when reasonably possible. If one is incomplete, track the gap and do not claim full bilingual coverage.
- Incomplete translation must not block unrelated engineering work. It may block a language-specific acceptance claim or release readiness when the affected route is essential.
- Missing-translation checks should report the affected keys/routes and may be scoped to the changed feature; do not make global parity a blanket prerequisite for every unrelated change.
- Preserve language preference independently from campaign save/progression.

## 7. Story and canon

- Story ideas and future campaign outlines may be developed independently from implementation scheduling.
- Implement only the active approved campaign scope unless the player explicitly reprioritizes it.
- Keep facts, hypotheses, disputed information, and unknown causes distinct.
- Characters may only act on information they plausibly know.
- New reveals may deepen prior events but must not casually contradict established canon.

## 8. Completion and reporting

A feature is complete when its intended player behavior is implemented, discoverable, integrated with adjacent flows, persisted correctly where applicable, understandable, and covered by proportionate tests.

For each meaningful handoff, report:
- what changed;
- what remains incomplete or intentionally deferred;
- which checks actually passed;
- whether an APK exists and what kind it is;
- any known risks or manual checks still needed.

## 9. Document responsibilities

- `GAME_CONCEPT.md` and `Story_Line.md`: product identity, story, characters, canon, and future concepts.
- `docs/DEVELOPMENT_STRATEGY.md`: canonical engineering rules and workflow.
- `docs/IMPLEMENTATION_AUDIT.md`: source-based implementation findings and dated progress; not a competing policy document.
- `docs/REBUILD_STATUS.md`: chronological implementation/status record; older entries are historical and may be superseded.
- `docs/DEVICE_ACCEPTANCE.md`: practical physical-device acceptance checklist for major review/release checkpoints, not a per-commit requirement.
- `docs/LOCALIZATION_PLAN.md`: localization architecture, coverage, and progress; it must follow the incremental policy above.

## 10. Definition of done

The active First Contact experience is ready for acceptance when a fresh-save player can complete the intended chapter journey, important state survives interruption/relaunch, required evidence and decisions remain grounded, no blocking progression or data-loss defect remains, and relevant source checks pass. Release readiness additionally requires the applicable release and device-acceptance checks.