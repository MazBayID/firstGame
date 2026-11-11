Manual save slots are not part of the initial scope and can be reconsidered separately.

## 10.4 Connectivity — Offline-first

The initial campaign is **offline-first** and must remain playable without an internet connection.

- Authored story scenes, fixed-choice dialogue, mission content, learning material, code exercises, evaluation rules, rank/XP progression, and local autosave must function on-device.
- Core campaign progression and mission completion must not require a server, external API, or live AI model.
- Store campaign progress locally so the player can resume without connectivity.
- Future online features—such as optional AI-assisted conversation, cloud synchronization/backup, or downloadable content—are supplemental and must fail gracefully when offline.
- No online account, API key, or continuous network connection is required for the initial campaign.

This is an offline-first design, not a permanent ban on online features. Any later online feature must preserve the ability to play the core campaign offline.