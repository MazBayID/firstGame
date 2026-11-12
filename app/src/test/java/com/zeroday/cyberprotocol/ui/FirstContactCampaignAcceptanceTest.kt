package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.CasePhase
import com.zeroday.cyberprotocol.core.model.CasePhaseEvaluator
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.Campaign01CaseCatalog
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FirstContactCampaignAcceptanceTest {
    private class MemoryRepository(initial: GameState = GameState()) : GameStateRepository {
        private var state = initial
        override fun load(): GameState = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }

    @Test
    fun freshRecruitCanCompleteTrainingAndRx14WithAnEvidenceBasedConclusion() {
        val repository = MemoryRepository()
        val session = GameSessionViewModel(repository)

        session.saveStoryBeat(4)
        session.startMission("first_contact.signal")
        assertTrue(session.completeMission("first_contact.signal", 25, requiredClearance = 0))
        session.recordEvidence(
            EvidenceRecord(
                id = "evidence_first_contact.signal",
                title = "Sinyal Pertama",
                summary = "Output ACCESS GRANTED diverifikasi.",
                source = "A.E.G.I.S. / Python evaluator 9",
                status = EvidenceStatus.CONFIRMED,
                missionId = "first_contact.signal"
            )
        )

        session.saveStoryBeat(5)
        session.startMission("first_contact.identity")
        assertTrue(session.completeMission("first_contact.identity", 35, requiredClearance = 0))
        session.recordEvidence(
            EvidenceRecord(
                id = "evidence_first_contact.identity",
                title = "Verifikasi Identitas",
                summary = "Output IDENTITY VERIFIED diverifikasi.",
                source = "A.E.G.I.S. / Python evaluator 10",
                status = EvidenceStatus.CONFIRMED,
                missionId = "first_contact.identity"
            )
        )

        session.saveStoryBeat(10)
        val recruited = repository.load()
        assertEquals(1, recruited.clearance)
        assertTrue(recruited.unlockedApps.containsAll(setOf("OPERATIONS", "ANALYSIS", "DATABASE", "DOSSIER")))

        val definition = Campaign01CaseCatalog.rx14
        session.acknowledgeCaseBriefing(definition)
        definition.requiredEvidenceIds.sorted().forEach { session.discoverCaseEvidence(definition, it) }
        definition.requiredEvidenceIds.sorted().forEach { session.analyzeCaseEvidence(definition, it) }
        session.selectCaseAnalysisConclusion(definition, "observed_discrepancy_only")

        val rationale = "Dua sumber mencatat selisih empat detik. Penyebab belum dapat ditentukan tanpa log tambahan."
        session.saveCaseDecisionRationale(definition, rationale)
        session.selectCaseDecision(definition, "insufficient_evidence", rationale)
        session.acknowledgeCaseDebrief(definition)

        val completed = repository.load()
        val progress = completed.caseProgress.getValue(definition.id)
        assertEquals(CasePhase.COMPLETE, CasePhaseEvaluator.phase(definition, progress))
        assertEquals(160, completed.experience)
        assertEquals(100, completed.intelligenceCompetencyXp)
        assertTrue(completed.completedMissionIds.containsAll(
            setOf("first_contact.signal", "first_contact.identity", definition.id)
        ))
        assertEquals(rationale, progress.decisionRationale)
        assertEquals("observed_discrepancy_only", progress.analysisConclusionId)
        assertEquals("insufficient_evidence", progress.selectedDecisionId)
        assertTrue(completed.evidence.any { it.id == "transmission_timestamp_discrepancy" })

        val restoredSession = GameSessionViewModel(repository)
        assertEquals(completed, restoredSession.gameState)
    }
}
