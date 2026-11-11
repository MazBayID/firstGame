Manual save slots may be considered later as a separate feature decision.

## 8.4 Connectivity — Offline-first

The initial version is **offline-first**. An internet connection must not be required to play the core campaign.

- The main menu, authored story, character dialogue, mission logic, learning content, code exercises, evaluation, progression, and autosave must work locally without an internet connection.
- Core mission outcomes and campaign progression must not depend on a remote server or external AI service.
- Store the player's campaign state and progress locally on the device. Design local persistence so that important saved data can be validated and recovered safely where practical.
- Any future online capability—such as optional AI-assisted conversation, cloud backup/synchronization, or additional downloadable content—must be supplemental. Clearly handle unavailable connectivity and provide a graceful offline fallback.
- Do not make an external API key, account login, or network connection a prerequisite for the initial campaign.
- The initial offline-first decision does not prohibit online features later; each proposed online feature must have a clear purpose and must not break the core offline experience.