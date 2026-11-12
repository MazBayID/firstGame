package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

class Campaign01CatalogTest {
    @Test
    fun openingMissionsHaveStableUniqueIdentifiers() {
        val missions = Campaign01Catalog.missions
        assertEquals(8, missions.size)
        assertEquals(missions.size, missions.map { it.id }.distinct().size)
        assertNotNull(Campaign01Catalog.find("first_contact.signal"))
        assertTrue(!Campaign01Catalog.find("first_contact.signal")!!.briefing.contains("Freya", ignoreCase = true))
        assertNotNull(Campaign01Catalog.find("first_contact.identity"))
        assertNotNull(Campaign01Catalog.find("first_contact.audit"))
        assertNotNull(Campaign01Catalog.find("first_contact.inventory"))
        assertNotNull(Campaign01Catalog.find("first_contact.trace"))
        assertNotNull(Campaign01Catalog.find("first_contact.compare"))
        assertNotNull(Campaign01Catalog.find("first_contact.normalize"))
        assertNotNull(Campaign01Catalog.find("first_contact.validate_delta"))
        assertNull(Campaign01Catalog.find("unapproved_case"))
    }

    @Test
    fun openingMissionsKeepEvaluatorAndRewardContractsAligned() {
        val signal = Campaign01Catalog.find("first_contact.signal")!!
        val identity = Campaign01Catalog.find("first_contact.identity")!!

        assertEquals(9, signal.evaluatorId)
        assertEquals(10, identity.evaluatorId)
        assertEquals(25, signal.rewardXp)
        assertEquals(35, identity.rewardXp)
        assertEquals(setOf("message_fragment.txt"), signal.initialFiles.keys)
        assertEquals("ACCESS GRANTED\n", signal.initialFiles["message_fragment.txt"])
        assertTrue("message_fragment.txt" in signal.allowedFiles)
        assertTrue("message_fragment.txt" !in signal.writableFiles)
        assertEquals(8, Campaign01Catalog.find("first_contact.audit")!!.evaluatorId)
        assertEquals(11, Campaign01Catalog.find("first_contact.inventory")!!.evaluatorId)
        assertEquals(5, Campaign01Catalog.find("first_contact.audit")!!.runtimeVariables["events"]!!.size)
        assertEquals(3, Campaign01Catalog.find("first_contact.inventory")!!.runtimeVariables["records"]!!.size)
        assertEquals(12, Campaign01Catalog.find("first_contact.trace")!!.evaluatorId)
        assertEquals(13, Campaign01Catalog.find("first_contact.compare")!!.evaluatorId)
        assertEquals(14, Campaign01Catalog.find("first_contact.normalize")!!.evaluatorId)
        assertEquals(15, Campaign01Catalog.find("first_contact.validate_delta")!!.evaluatorId)
        assertEquals(2, Campaign01Catalog.find("first_contact.normalize")!!.runtimeVariables["receipt_seconds"]!!.size)
        assertEquals(2, Campaign01Catalog.find("first_contact.validate_delta")!!.runtimeVariables["receipt_seconds"]!!.size)
        assertEquals(2, Campaign01Catalog.find("first_contact.trace")!!.runtimeVariables["receipts"]!!.size)
        assertEquals(2, Campaign01Catalog.find("first_contact.compare")!!.runtimeVariables["receipts"]!!.size)
        assertEquals(Campaign01Catalog.CAMPAIGN_ID, signal.campaignId)
        assertEquals(Campaign01Catalog.CHAPTER_ID, identity.chapterId)
    }

    @Test
    fun operationsResumeMappingTargetsEveryAuthoredMissionCheckpoint() {
        assertEquals(
            listOf(4, 5),
            Campaign01Catalog.missions.mapNotNull { Campaign01Catalog.storyStageForMission(it.id) }
        )
        assertEquals(
            setOf("first_contact.signal", "first_contact.identity"),
            Campaign01Catalog.guidedTrainingMissionIds
        )
        assertEquals(
            setOf(
                "first_contact.audit", "first_contact.inventory", "first_contact.trace",
                "first_contact.compare", "first_contact.normalize", "first_contact.validate_delta"
            ),
            Campaign01Catalog.retiredLegacyStoryMissionIds
        )
        assertNull(Campaign01Catalog.storyStageForMission("unapproved_case"))
        assertEquals(8, Campaign01Catalog.missions.size)
    }

    @Test
    fun missionWorkspaceRejectsWritesOutsideItsAllowlist() {
        val mission = Campaign01Catalog.find("first_contact.signal")!!
        assertEquals(setOf("signal.py"), mission.writableFiles)
        assertThrows(IllegalArgumentException::class.java) {
            mission.copy(writableFiles = setOf("../outside.py"))
        }
        assertThrows(IllegalArgumentException::class.java) {
            mission.copy(writableFiles = setOf("not-allowed.py"))
        }
        assertThrows(IllegalArgumentException::class.java) {
            mission.copy(initialFiles = mapOf("../outside.txt" to "blocked"))
        }
        assertThrows(IllegalArgumentException::class.java) {
            mission.copy(initialFiles = mapOf("signal.py" to "must remain player-writable"))
        }
    }

    @Test
    fun hintsAreGraduatedAndWorkspaceFilesAreMissionScoped() {
        Campaign01Catalog.missions.forEach { mission ->
            assertEquals(listOf(1, 2, 3), mission.hints.map { it.level }.sorted())
            assertTrue(mission.workspaceDirectory.startsWith("/A.E.G.I.S/missions/first_contact/"))
            assertTrue(mission.allowedFiles.none { it.contains("..") || it.startsWith("/") })
            assertTrue(mission.objectives.all { it.required })
            assertTrue("Every campaign mission should carry a forward clue", !mission.nextClue.isNullOrBlank())
        }

        assertTrue(
            Campaign01Catalog.find("first_contact.signal")!!.workspaceDirectory !=
                Campaign01Catalog.find("first_contact.identity")!!.workspaceDirectory
        )
    }
}
