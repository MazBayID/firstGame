package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.EvidenceRelationType
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameSessionViewModelTest {
    private class MemoryRepository : GameStateRepository {
        private var state = GameState()
        override fun load(): GameState = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }

    @Test
    fun viewModelOwnsMissionCompletionAndCodeCheckpoints() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.startMission("first_contact.signal")
        assertEquals("first_contact.signal", viewModel.gameState.activeMissionId)

        assertTrue(viewModel.completeMission("first_contact.signal", 25, 0))
        assertFalse(viewModel.completeMission("first_contact.signal", 25, 0))
        viewModel.saveCode("story_4", "signal = \"ACCESS GRANTED\"", "code_saved_story_4")

        assertEquals(25, viewModel.gameState.experience)
        assertEquals("CIVILIAN", viewModel.gameState.rank)
        assertNull(viewModel.gameState.activeMissionId)
        assertEquals("signal = \"ACCESS GRANTED\"", viewModel.gameState.savedCode["story_4"])
    }

    @Test
    fun openingRecruitmentInvitationIsPersistedAndIdempotent() {
        val viewModel = GameSessionViewModel(MemoryRepository())

        viewModel.openRecruitmentInvitation()

        assertTrue(viewModel.gameState.recruitmentNotificationOpened)
        assertFalse(viewModel.gameState.freyaContactOpened)
        assertFalse(viewModel.gameState.nexusContactUnlocked)
        assertEquals(2, viewModel.gameState.storyBeat)
        assertEquals("recruitment_invitation_opened", viewModel.gameState.lastCheckpoint)

        viewModel.openRecruitmentInvitation()
        assertEquals(2, viewModel.gameState.storyBeat)
        assertEquals("recruitment_invitation_opened", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun freyaIntroductionCannotBeOpenedBeforeTheFirstMissionIsActive() {
        val viewModel = GameSessionViewModel(MemoryRepository())

        viewModel.openFreyaIntroduction()
        assertFalse(viewModel.gameState.freyaContactOpened)

        viewModel.saveStoryBeat(4)
        viewModel.openFreyaIntroduction()
        assertFalse(viewModel.gameState.freyaContactOpened)

        viewModel.startMission("first_contact.signal")
        viewModel.openFreyaIntroduction()
        assertTrue(viewModel.gameState.freyaContactOpened)
        assertEquals("freya_introduction_opened", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun freyaContactMustBeOpenedBeforeNexusCanBeUnlocked() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.unlockNexusContact()
        assertFalse(viewModel.gameState.nexusContactUnlocked)

        viewModel.saveStoryBeat(4)
        viewModel.startMission("first_contact.signal")
        viewModel.openFreyaIntroduction()
        assertTrue(viewModel.gameState.freyaContactOpened)
        viewModel.unlockNexusContact()
        assertTrue(viewModel.gameState.nexusContactUnlocked)
        assertEquals("nexus_contact_unlocked", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun chapterCanAdvanceBeyondRecruitmentCheckpointWithoutLosingClearance() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.saveStoryBeat(16)

        assertTrue(viewModel.gameState.unlockedApps.contains("BROWSER"))
        assertEquals(16, viewModel.gameState.storyBeat)
        assertEquals(1, viewModel.gameState.clearance)
        assertEquals("story_beat_16", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun existingUnlockedAppsArePreservedWhenStoryProgressAddsTheBrowser() {
        val repository = MemoryRepository()
        val viewModel = GameSessionViewModel(repository)
        viewModel.saveStoryBeat(3)
        assertFalse(viewModel.gameState.unlockedApps.contains("BROWSER"))

        viewModel.saveStoryBeat(4)

        assertTrue(viewModel.gameState.unlockedApps.contains("BROWSER"))
        assertTrue(viewModel.gameState.unlockedApps.contains("DATABASE"))
        assertTrue(viewModel.gameState.unlockedApps.contains("DOSSIER"))
    }

    @Test
    fun recruitmentCheckpointGrantsClearanceAndRecordsUnverifiedLead() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.saveStoryBeat(10)

        assertEquals(10, viewModel.gameState.storyBeat)
        assertEquals(1, viewModel.gameState.clearance)
        assertTrue(viewModel.gameState.unlockedApps.contains("OPERATIONS"))
        val records = viewModel.gameState.evidence.associateBy { it.id }
        assertEquals(3, records.size)
        assertEquals(EvidenceStatus.UNVERIFIED, records.getValue("transmission_timestamp_discrepancy").status)
        assertEquals(EvidenceStatus.UNVERIFIED, records.getValue("rx14_aegis_receipt").status)
        assertEquals(EvidenceStatus.UNVERIFIED, records.getValue("rx14_archive_receipt").status)
        assertEquals(
            setOf("rx14_aegis_receipt", "rx14_archive_receipt"),
            records.getValue("transmission_timestamp_discrepancy").relations
                .filter { it.type == EvidenceRelationType.DERIVED_FROM }
                .map { it.relatedEvidenceId }.toSet()
        )
        assertEquals(
            "asset_rx14_aegis_receipt_v1",
            records.getValue("rx14_aegis_receipt").provenance.sourceAssetId
        )
        assertEquals(
            EvidenceRelationType.CONTRADICTS,
            records.getValue("rx14_aegis_receipt").relations.single().type
        )
        assertEquals("story_beat_10", viewModel.gameState.lastCheckpoint)
    }
}
