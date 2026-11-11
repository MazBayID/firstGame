package com.zeroday.cyberprotocol.ui

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
        assertEquals(2, viewModel.gameState.storyBeat)
        assertEquals("recruitment_invitation_opened", viewModel.gameState.lastCheckpoint)

        viewModel.openRecruitmentInvitation()
        assertEquals(2, viewModel.gameState.storyBeat)
        assertEquals("recruitment_invitation_opened", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun freyaContactMustBeOpenedBeforeNexusCanBeUnlocked() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.unlockNexusContact()
        assertFalse(viewModel.gameState.nexusContactUnlocked)

        viewModel.openFreyaIntroduction()
        assertTrue(viewModel.gameState.freyaContactOpened)
        viewModel.unlockNexusContact()
        assertTrue(viewModel.gameState.nexusContactUnlocked)
        assertEquals("nexus_contact_unlocked", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun chapterCanAdvanceBeyondRecruitmentCheckpointWithoutLosingClearance() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.saveStoryBeat(13)

        assertEquals(13, viewModel.gameState.storyBeat)
        assertEquals(1, viewModel.gameState.clearance)
        assertEquals("story_beat_13", viewModel.gameState.lastCheckpoint)
    }

    @Test
    fun recruitmentCheckpointGrantsClearanceAndRecordsUnverifiedLead() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.saveStoryBeat(10)

        assertEquals(10, viewModel.gameState.storyBeat)
        assertEquals(1, viewModel.gameState.clearance)
        assertTrue(viewModel.gameState.unlockedApps.contains("OPERATIONS"))
        assertEquals(
            "UNVERIFIED",
            viewModel.gameState.evidence.single { it.id == "transmission_timestamp_discrepancy" }.status.name
        )
        assertEquals("story_beat_10", viewModel.gameState.lastCheckpoint)
    }
}
