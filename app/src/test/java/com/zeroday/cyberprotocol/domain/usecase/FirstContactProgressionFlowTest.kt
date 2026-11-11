package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FirstContactProgressionFlowTest {
    private class MemoryRepository : GameStateRepository {
        private var state = GameState(storyBeat = 4)
        override fun load(): GameState = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }

    @Test
    fun firstContactCompletionPersistsXpEvidenceRankAndUnlocks() {
        val repository = MemoryRepository()
        val complete = CompleteMissionUseCase(repository)
        val evidence = RecordEvidenceUseCase(repository)

        complete.complete("first_contact.signal", rewardXp = 25)
        evidence.record(EvidenceRecord(
            id = "evidence_first_contact.signal",
            title = "Sinyal Pertama",
            summary = "Output diverifikasi.",
            source = "NEXUS / evaluator 9",
            status = EvidenceStatus.CONFIRMED,
            missionId = "first_contact.signal"
        ))
        assertEquals("CIVILIAN", repository.load().rank)
        assertEquals(25, repository.load().experience)

        complete.complete("first_contact.identity", rewardXp = 35)
        evidence.record(EvidenceRecord(
            id = "evidence_first_contact.identity",
            title = "Verifikasi Identitas",
            summary = "Output diverifikasi.",
            source = "NEXUS / evaluator 10",
            status = EvidenceStatus.CONFIRMED,
            missionId = "first_contact.identity"
        ))

        val recruited = repository.load().copy(
            storyBeat = 7,
            clearance = 1,
            unlockedApps = ProgressionRules.unlockedAppsForStoryBeat(7)
        )
        repository.save(recruited)
        evidence.record(EvidenceRecord(
            id = "transmission_timestamp_discrepancy",
            title = "Perbedaan Waktu Penerimaan Transmisi",
            summary = "Perbedaan tercatat, penyebab belum diketahui.",
            source = "NEXUS / metadata",
            status = EvidenceStatus.UNVERIFIED,
            missionId = "first_contact.recruitment"
        ))

        val finalState = repository.load()
        assertEquals(60, finalState.experience)
        assertEquals("RECRUIT", finalState.rank)
        assertEquals(1, finalState.clearance)
        assertEquals(2, finalState.completedMissionIds.size)
        assertEquals(3, finalState.evidence.size)
        assertTrue(finalState.unlockedApps.containsAll(
            setOf("MESSENGER", "TERMINAL", "DATABASE", "DOSSIER", "ANALYSIS", "OPERATIONS")
        ))
        assertEquals(
            EvidenceStatus.UNVERIFIED,
            finalState.evidence.single { it.id == "transmission_timestamp_discrepancy" }.status
        )
    }
}
