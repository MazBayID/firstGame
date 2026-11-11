package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LegacyProgressMigrationTest {
    @Test
    fun openingStoryStagesRestoreOnlyCompletedMissions() {
        val beforeFirstTask = LegacyProgressMigration.fromStoryBeat(4)
        assertTrue(beforeFirstTask.completedMissionIds.isEmpty())
        assertTrue(!beforeFirstTask.freyaContactOpened)
        assertTrue(!beforeFirstTask.nexusContactUnlocked)
        assertEquals(0, beforeFirstTask.experience)

        val afterFirstTask = LegacyProgressMigration.fromStoryBeat(5)
        assertEquals(setOf("first_contact.signal"), afterFirstTask.completedMissionIds)
        assertEquals(25, afterFirstTask.experience)
        assertEquals("CIVILIAN", afterFirstTask.rank)

        val afterSecondTask = LegacyProgressMigration.fromStoryBeat(6)
        assertEquals(
            setOf("first_contact.signal", "first_contact.identity"),
            afterSecondTask.completedMissionIds
        )
        assertEquals(60, afterSecondTask.experience)
    }

    @Test
    fun completedRecruitmentRestoresClearanceAppsAndUnverifiedLead() {
        val migrated = LegacyProgressMigration.fromStoryBeat(7)
        assertEquals(1, migrated.clearance)
        assertEquals("RECRUIT", migrated.rank)
        assertEquals(
            setOf("MESSENGER", "DATABASE", "DOSSIER", "TERMINAL", "ANALYSIS", "OPERATIONS"),
            migrated.unlockedApps
        )
        val lead = migrated.evidence.single { it.id == "transmission_timestamp_discrepancy" }
        assertEquals(EvidenceStatus.UNVERIFIED, lead.status)
    }

    @Test
    fun legacyProgressIsClampedToCampaignBounds() {
        assertEquals(0, LegacyProgressMigration.fromStoryBeat(-5).storyBeat)
        assertEquals(7, LegacyProgressMigration.fromStoryBeat(99).storyBeat)
    }
}
