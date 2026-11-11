package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
        assertEquals(null, viewModel.gameState.activeMissionId)
        assertEquals("signal = \"ACCESS GRANTED\"", viewModel.gameState.savedCode["story_4"])
    }

    @Test
    fun recruitmentCheckpointGrantsClearanceAndRecordsUnverifiedLead() {
        val viewModel = GameSessionViewModel(MemoryRepository())
        viewModel.saveStoryBeat(7)

        assertEquals(7, viewModel.gameState.storyBeat)
        assertEquals(1, viewModel.gameState.clearance)
        assertTrue(viewModel.gameState.unlockedApps.contains("OPERATIONS"))
        assertEquals(
            "UNVERIFIED",
            viewModel.gameState.evidence.single { it.id == "transmission_timestamp_discrepancy" }.status.name
        )
        assertEquals("story_beat_7", viewModel.gameState.lastCheckpoint)
    }
}
