# Cyber Protocol — Integrated Development Strategy

**Decision date:** 2026-11-12  
**Authority:** explicit player direction; supersedes the previous habit of producing a development APK after every main-branch push.

## Product-development rule

Build the game as a coherent experience first. Do not optimize for APK count, CI cosmetics, or isolated screen completion while core gameplay remains incomplete.

A successful build proves packaging and compilation. It does not prove that the player can understand, navigate, complete, and recover the game experience.

## Development loop

1. Select one connected player-facing workstream.
2. Inspect the existing story, models, use cases, screens, and save contracts before changing it.
3. Define the player journey and behavior contract for that workstream.
4. Implement the domain rules, authored content, UI, navigation, persistence, and feedback together in sensible vertical slices.
5. Add/run focused unit tests as guardrails while implementing.
6. Integrate the slice into the campaign and verify that adjacent screens and state transitions agree.
7. Update the implementation audit and campaign acceptance checklist honestly.
8. Move to the next connected gap. Do not package an APK merely because a batch ended.

## Current campaign priority

Complete Campaign 01 — FIRST CONTACT and its RX-14 case from a fresh save to a meaningful chapter debrief. Preserve approved story canon and the unresolved four-second discrepancy. Do not invent a culprit or a new case.

The case must connect:
- story and character communications;
- briefing and objective tracking;
- source discovery and immutable evidence;
- analytical work and uncertainty handling;
- operational decisions and deterministic feedback;
- connected A.E.G.I.S. apps;
- progression, debrief, archives, and save recovery.

The five experience phases are flexible design concepts, not five compulsory screens or app visits.

## Unit tests and CI

- Focused unit/domain tests are encouraged during implementation.
- Run broader Python and Android unit suites at meaningful integration checkpoints and before release-candidate approval.
- Ordinary pushes to `main` run source tests only. They do not assemble or upload APKs.
- Pull requests also run source tests only.
- Release packaging, emulator smoke tests, checksum/signature validation, and physical-device acceptance are deferred until the integrated game meets the acceptance definition in `docs/REBUILD_STATUS.md` and the player explicitly requests release packaging.
- Do not spend a feature batch on CI restructuring unless CI is actively blocking the development work.

## Completion standard

A feature is not complete because its button exists, its screen renders, its unit test passes, or its APK builds. It is complete when its behavior is authored, discoverable, integrated into the campaign, correctly persisted, understandable to the player, and tested against failure/recovery paths.
