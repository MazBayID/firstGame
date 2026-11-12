package com.zeroday.cyberprotocol.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressionRulesTest {
    @Test
    fun optionalPracticeUnlocksAfterTheRecruitmentCaseBecomesAvailable() {
        assertFalse("PRACTICE" in ProgressionRules.unlockedAppsForStoryBeat(9))
        assertTrue("PRACTICE" in ProgressionRules.unlockedAppsForStoryBeat(10))
    }
}
