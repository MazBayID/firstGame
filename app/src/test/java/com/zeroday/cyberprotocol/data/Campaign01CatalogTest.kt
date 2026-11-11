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
        assertEquals(6, missions.size)
        assertEquals(missions.size, missions.map { it.id }.distinct().size)
        assertNotNull(Campaign01Catalog.find("first_contact.signal"))
        assertNotNull(Campaign01Catalog.find("first_contact.identity"))
        assertNotNull(Campaign01Catalog.find("first_contact.audit"))
        assertNotNull(Campaign01Catalog.find("first_contact.inventory"))
        assertNotNull(Campaign01Catalog.find("first_contact.trace"))
        assertNotNull(Campaign01Catalog.find("first_contact.compare"))
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
        assertEquals(8, Campaign01Catalog.find("first_contact.audit")!!.evaluatorId)
        assertEquals(11, Campaign01Catalog.find("first_contact.inventory")!!.evaluatorId)
        assertEquals(5, Campaign01Catalog.find("first_contact.audit")!!.runtimeVariables["events"]!!.size)
        assertEquals(3, Campaign01Catalog.find("first_contact.inventory")!!.runtimeVariables["records"]!!.size)
        assertEquals(12, Campaign01Catalog.find("first_contact.trace")!!.evaluatorId)
        assertEquals(13, Campaign01Catalog.find("first_contact.compare")!!.evaluatorId)
        assertEquals(2, Campaign01Catalog.find("first_contact.trace")!!.runtimeVariables["receipts"]!!.size)
        assertEquals(2, Campaign01Catalog.find("first_contact.compare")!!.runtimeVariables["receipts"]!!.size)
        assertEquals(Campaign01Catalog.CAMPAIGN_ID, signal.campaignId)
        assertEquals(Campaign01Catalog.CHAPTER_ID, identity.chapterId)
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
    }

    @Test
    fun hintsAreGraduatedAndWorkspaceFilesAreMissionScoped() {
        Campaign01Catalog.missions.forEach { mission ->
            assertEquals(listOf(1, 2, 3), mission.hints.map { it.level }.sorted())
            assertTrue(mission.workspaceDirectory.startsWith("/A.E.G.I.S/missions/first_contact/"))
            assertTrue(mission.allowedFiles.none { it.contains("..") || it.startsWith("/") })
            assertTrue(mission.objectives.all { it.required })
        }

        assertTrue(
            Campaign01Catalog.find("first_contact.signal")!!.workspaceDirectory !=
                Campaign01Catalog.find("first_contact.identity")!!.workspaceDirectory
        )
    }
}
