package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.EvidenceAssetCatalog
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GameSessionViewModelTest {
    private class MemoryRepository(initial: GameState) : GameStateRepository {
        private var state = initial
        override fun load() = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }

    @Test
    fun existingInvestigationSaveReceivesMissingRx14EvidenceAndRelations() {
        val firstAsset = EvidenceAssetCatalog.assets.first()
        val repository = MemoryRepository(
            GameState(
                storyBeat = 16,
                evidence = listOf(
                    EvidenceRecord(
                        id = firstAsset.evidenceId,
                        title = firstAsset.title,
                        summary = firstAsset.content.trim(),
                        source = firstAsset.sourceLabel,
                        missionId = EvidenceAssetCatalog.MISSION_ID
                    )
                )
            )
        )
        val viewModel = GameSessionViewModel(repository)

        viewModel.ensureRecruitmentEvidence()
        val state = repository.load()
        val expectedIds = EvidenceAssetCatalog.assets.map { it.evidenceId }.toSet()
        val discrepancy = state.evidence.single { it.id == "transmission_timestamp_discrepancy" }

        assertTrue(state.storyBeat == 16)
        assertTrue(state.evidence.map { it.id }.containsAll(expectedIds))
        assertEquals(expectedIds, discrepancy.relations.map { it.relatedEvidenceId }.toSet())
        assertEquals(expectedIds.size + 1, state.evidence.count {
            it.id in expectedIds || it.id == "transmission_timestamp_discrepancy"
        })
    }

    @Test
    fun retiringLegacyActiveMissionPreservesEarnedProgressAndDrafts() {
        val repository = MemoryRepository(
            GameState(
                storyBeat = 12,
                activeMissionId = "first_contact.trace",
                completedMissionIds = setOf("first_contact.signal", "first_contact.audit"),
                experience = 100,
                savedCode = mapOf("story_11" to "custom_player_draft")
            )
        )
        val viewModel = GameSessionViewModel(repository)

        viewModel.retireLegacyStoryMission()
        val state = repository.load()

        assertNull(state.activeMissionId)
        assertEquals(setOf("first_contact.signal", "first_contact.audit"), state.completedMissionIds)
        assertEquals(100, state.experience)
        assertEquals("custom_player_draft", state.savedCode["story_11"])
    }

    @Test
    fun ensuringRx14EvidenceIsIdempotent() {
        val repository = MemoryRepository(GameState(storyBeat = 16))
        val viewModel = GameSessionViewModel(repository)

        viewModel.ensureRecruitmentEvidence()
        val first = repository.load()
        viewModel.ensureRecruitmentEvidence()
        val second = repository.load()

        assertEquals(first.evidence, second.evidence)
        assertEquals(first.lastCheckpoint, second.lastCheckpoint)
    }
}
