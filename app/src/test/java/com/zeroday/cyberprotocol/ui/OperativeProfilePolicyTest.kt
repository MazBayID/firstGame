package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class OperativeProfilePolicyTest {
    @Test
    fun rankProgressUsesTheCurrentAndNextCanonicalThresholds() {
        val progress = OperativeProfilePolicy.rankProgress(80)

        assertEquals(60, progress.currentRankXp)
        assertEquals(100, progress.nextRankXp)
        assertEquals(0.5f, progress.progress, 0.0001f)
        assertEquals(20, progress.remainingXp)
    }

    @Test
    fun rankProgressHandlesNegativeExperienceAndMaximumRank() {
        val negative = OperativeProfilePolicy.rankProgress(-10)
        assertEquals(0, negative.currentRankXp)
        assertEquals(60, negative.nextRankXp)
        assertEquals(0f, negative.progress, 0.0001f)
        assertEquals(60, negative.remainingXp)

        val maximum = OperativeProfilePolicy.rankProgress(2_500)
        assertEquals(2_500, maximum.currentRankXp)
        assertEquals(null, maximum.nextRankXp)
        assertEquals(1f, maximum.progress, 0.0001f)
        assertEquals(0, maximum.remainingXp)
    }
}
