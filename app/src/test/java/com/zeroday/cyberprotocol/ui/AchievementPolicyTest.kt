package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01CaseCatalog
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AchievementPolicyTest {
    @Test
    fun freshOperativeStartsWithNoEarnedAchievements() {
        val definition = Campaign01CaseCatalog.rx14
        val achievements = AchievementPolicy.evaluate(
            GameState(),
            definition,
            CaseProgress(caseId = definition.id)
        )

        assertEquals(10, achievements.size)
        assertTrue(achievements.none { it.completed })
    }

    @Test
    fun achievementsReflectSavedCampaignCaseAndOptionalPracticeProgress() {
        val definition = Campaign01CaseCatalog.rx14
        val progress = CaseProgress(
            caseId = definition.id,
            briefingAcknowledged = true,
            discoveredEvidenceIds = definition.requiredEvidenceIds,
            analyzedEvidenceIds = definition.requiredEvidenceIds,
            selectedDecisionId = "insufficient_evidence",
            debriefViewed = true
        )
        val completedIds = setOf("first_contact.signal", "first_contact.identity") +
            Campaign01Catalog.retiredLegacyStoryMissionIds
        val state = GameState(
            completedMissionIds = completedIds,
            experience = 250,
            technicalCompetencyXp = 100,
            intelligenceCompetencyXp = 100
        )

        val achievements = AchievementPolicy.evaluate(state, definition, progress)

        assertTrue(achievements.all { it.completed })
        assertEquals(10, achievements.count { it.completed })
        assertEquals(6, achievements.first { it.id == "practice_archive" }.progress)
    }
}
