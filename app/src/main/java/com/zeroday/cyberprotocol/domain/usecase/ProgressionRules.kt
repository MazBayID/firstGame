package com.zeroday.cyberprotocol.domain.usecase

/** Rank thresholds are campaign rules, kept outside presentation code. */
object ProgressionRules {
    private val ranks = listOf(
        0 to "CIVILIAN",
        25 to "RECRUIT",
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

    fun nextRankThreshold(experience: Int): Int? {
        val safeExperience = experience.coerceAtLeast(0)
        return ranks.firstOrNull { safeExperience < it.first }?.first
    }
}
