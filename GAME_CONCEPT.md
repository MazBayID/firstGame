The goal is to make experimentation safe and mistakes useful. More consequential failure systems may be considered in a later design phase, but they are not part of the initial version.

## 8.3 Progress Saving — Autosave

The initial version uses **automatic saving (autosave)**; a manual save system is not required.

- Save automatically at meaningful checkpoints, such as completed story beats, mission-stage transitions, successful submissions, debrief completion, and progression or unlock changes.
- Preserve the active mission stage, completed objectives, relevant work/context, campaign progress, player rank/XP, unlocked content, and collected records.
- Save important state changes safely so that leaving the game, closing the app, or an unexpected interruption does not routinely erase meaningful progress.
- Avoid writing to storage on every minor UI interaction. Use deliberate save points and safe handling of important state changes.
- When the player returns, restore the latest valid saved state and return them to an appropriate point in the campaign or operation.
- Do not imply that every keystroke or transient screen animation is saved. Define which data is persistent and which temporary UI state may reset.

Autosave is the approved initial approach because it reduces friction and protects progress for an Android-first game. Manual save slots may be considered later as a separate feature decision.