This approach encourages beginners to experiment, understand mistakes, and improve without fear of losing campaign progress. Consequence-based failure can be reconsidered separately after the core mission loop is proven.

## 10.3 Progress Saving — Autosave

The initial version uses **autosave**, not manual save slots.

- Save at meaningful narrative and gameplay checkpoints: after important story beats, mission-stage changes, successful submissions, debriefs, and progression or unlock updates.
- Preserve campaign position, active operation and stage, completed objectives, relevant mission work, rank/XP, unlocked applications or content, and collected evidence.
- A player who leaves the game or returns after an interruption should resume from the latest valid checkpoint rather than repeat substantial completed sections.
- Use deliberate checkpoints and safe persistence for important state changes; do not require a storage write for every small UI interaction.
- Temporary presentation state, such as an open animation or transient panel position, does not have to persist unless later UI design identifies a clear need.

Manual save slots are not part of the initial scope and can be reconsidered separately.