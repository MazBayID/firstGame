package com.zeroday.cyberprotocol.domain.usecase

/** Rank thresholds are campaign rules, kept outside presentation code. */
object ProgressionRules {
    private val ranks = listOf(
        0 to "CIVILIAN",
        60 to "RECRUIT",
        100 to "INITIATE",
        250 to "JUNIOR_ANALYST",
        500 to "FIELD_ANALYST",
        850 to "CYBER_OPERATIVE",
        1_250 to "SENIOR_OPERATIVE",
        1_800 to "SPECIAL_AGENT",
        2_500 to "ELITE_OPERATIVE"
    )

    fun rankForExperience(experience: Int): String {
        val safeExperience = experience.coerceAtLeast(0)
        return ranks.lastOrNull { safeExperience >= it.first }?.second ?: "CIVILIAN"
    }

    fun unlockedAppsForStoryBeat(storyBeat: Int): Set<String> = buildSet {
        val beat = storyBeat.coerceAtLeast(0)
        add("MESSENGER")
        if (beat >= 3) {
            add("DATABASE")
            add("DOSSIER")
        }
        if (beat >= 4) add("TERMINAL")
        if (beat >= 5) add("ANALYSIS")
        if (beat >= 7) add("OPERATIONS")
    }

    fun nextRankThreshold(experience: Int): Int? {
        val safeExperience = experience.coerceAtLeast(0)
        return ranks.firstOrNull { safeExperience < it.first }?.first
    }
}
