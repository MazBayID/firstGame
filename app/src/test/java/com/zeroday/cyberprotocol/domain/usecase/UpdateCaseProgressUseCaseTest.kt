package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.*
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateCaseProgressUseCaseTest {
    private class MemoryRepository(initial: GameState) : GameStateRepository {
        private var state = initial
        override fun load() = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }
    private val definition = CaseDefinition(
        "case_a", "campaign_test", "Case A", "Investigate the records.",
        MissionArchetype.INVESTIGATION,
        requiredEvidenceIds = setOf("primary", "secondary"),
        optionalEvidenceIds = setOf("optional"),
        decisionOptions = listOf(
            CaseDecisionOption("monitor", "Monitor", "Request another source."),
            CaseDecisionOption("pause", "Pause", "Hold the operation.", setOf("optional"))
        )
    )
    private fun repository() = MemoryRepository(GameState(evidence = listOf(
        EvidenceRecord("primary", "Primary", "Observation one.", "Archive", missionId = "case_a"),
        EvidenceRecord("secondary", "Secondary", "Observation two.", "Intranet", missionId = "case_a"),
        EvidenceRecord("optional", "Optional", "Additional lead.", "Messenger", missionId = "case_a")
    )))

    @Test fun requiredEvidenceGatesAnalysisAndDecisionButOptionalLeadDoesNotGateCase() {
        val repo = repository(); val useCase = UpdateCaseProgressUseCase(repo)
        useCase.acknowledgeBriefing(definition)
        useCase.discoverEvidence(definition, "primary")
        assertThrows(IllegalArgumentException::class.java) { useCase.analyzeEvidence(definition, "primary") }
        useCase.discoverEvidence(definition, "secondary")
        useCase.analyzeEvidence(definition, "primary")
        assertThrows(IllegalArgumentException::class.java) { useCase.selectDecision(definition, "monitor", "Kedua sumber berbeda empat detik; penyebabnya belum terbukti.") }
        useCase.analyzeEvidence(definition, "secondary")
        useCase.analyzeEvidence(definition, "primary")
        assertEquals(CasePhase.OPERATIONAL_DECISION,
            CasePhaseEvaluator.phase(definition, repo.load().caseProgress.getValue("case_a")))
        useCase.selectDecision(definition, "monitor")
        useCase.selectDecision(definition, "monitor")
        assertEquals(CasePhase.DEBRIEFING,
            CasePhaseEvaluator.phase(definition, repo.load().caseProgress.getValue("case_a")))
        useCase.acknowledgeDebrief(definition)
        useCase.acknowledgeDebrief(definition)
        assertEquals(CasePhase.COMPLETE,
            CasePhaseEvaluator.phase(definition, repo.load().caseProgress.getValue("case_a")))
    }

    @Test fun optionSpecificEvidenceIsRequiredOnlyForThatOption() {
        val repo = repository(); val useCase = UpdateCaseProgressUseCase(repo)
        useCase.acknowledgeBriefing(definition)
        listOf("primary", "secondary").forEach { useCase.discoverEvidence(definition, it) }
        listOf("primary", "secondary").forEach { useCase.analyzeEvidence(definition, it) }
        assertTrue(CasePhaseEvaluator.canSelectDecision(
            definition, repo.load().caseProgress.getValue("case_a"), "monitor"
        ))
        assertFalse(CasePhaseEvaluator.canSelectDecision(
            definition, repo.load().caseProgress.getValue("case_a"), "pause"
        ))
    }


    @Test fun requiredEvidenceCannotBeInspectedBeforeBriefing() {
        val repo = repository(); val useCase = UpdateCaseProgressUseCase(repo)
        assertThrows(IllegalArgumentException::class.java) {
            useCase.discoverEvidence(definition, "primary")
        }
    }

    @Test fun optionalLeadCanBeInspectedAndAnalyzedDuringAnalysis() {
        val repo = repository(); val useCase = UpdateCaseProgressUseCase(repo)
        useCase.acknowledgeBriefing(definition)
        useCase.discoverEvidence(definition, "primary")
        useCase.discoverEvidence(definition, "secondary")
        useCase.discoverEvidence(definition, "optional")
        assertEquals(CasePhase.ANALYSIS, CasePhaseEvaluator.phase(definition, repo.load().caseProgress.getValue("case_a")))
        useCase.analyzeEvidence(definition, "optional")
        useCase.analyzeEvidence(definition, "primary")
        useCase.analyzeEvidence(definition, "secondary")
        useCase.selectDecision(definition, "pause", "Tahan operasi sampai sumber tambahan diperiksa.")
        assertEquals("pause", repo.load().caseProgress.getValue("case_a").selectedDecisionId)
    }

    @Test fun evidenceOutsideCaseCannotBeAdded() {
        val repo = repository(); val useCase = UpdateCaseProgressUseCase(repo)
        assertThrows(IllegalArgumentException::class.java) {
            useCase.discoverEvidence(definition, "unrelated")
        }
    }
}
