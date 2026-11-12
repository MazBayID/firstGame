package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.*
import com.zeroday.cyberprotocol.data.Campaign01CaseCatalog
import com.zeroday.cyberprotocol.data.EvidenceAssetCatalog
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Rx14CaseProgressionTest {
    private class MemoryRepository(initial: GameState) : GameStateRepository {
        private var state = initial
        override fun load() = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }

    @Test
    fun playerCanCompleteRx14CaseByReportingInsufficientEvidence() {
        val records = EvidenceAssetCatalog.assets.map { asset ->
            EvidenceRecord(
                id = asset.evidenceId,
                title = asset.title,
                summary = asset.content.trim(),
                source = asset.sourceLabel,
                missionId = EvidenceAssetCatalog.MISSION_ID,
                provenance = EvidenceProvenance(
                    origin = asset.origin,
                    sourceLabel = asset.sourceLabel,
                    sourceAssetId = asset.id,
                    sourcePath = asset.path
                )
            )
        }
        val repository = MemoryRepository(GameState(evidence = records))
        val useCase = UpdateCaseProgressUseCase(repository)
        val definition = Campaign01CaseCatalog.rx14

        useCase.acknowledgeBriefing(definition)
        assertEquals(CasePhase.INVESTIGATION, phase(definition, repository))

        definition.requiredEvidenceIds.forEach { useCase.discoverEvidence(definition, it) }
        assertEquals(CasePhase.ANALYSIS, phase(definition, repository))

        definition.requiredEvidenceIds.forEach { useCase.analyzeEvidence(definition, it) }
        assertEquals(CasePhase.ANALYSIS, phase(definition, repository))

        useCase.selectAnalysisConclusion(definition, "observed_discrepancy_only")
        assertEquals(CasePhase.OPERATIONAL_DECISION, phase(definition, repository))

        useCase.selectDecision(
            definition,
            "insufficient_evidence",
            "Dua waktu berbeda empat detik, tetapi tidak ada sumber yang menjelaskan penyebabnya."
        )
        assertEquals(CasePhase.DEBRIEFING, phase(definition, repository))

        useCase.acknowledgeDebrief(definition)
        assertEquals(CasePhase.COMPLETE, phase(definition, repository))
        assertEquals(
            "insufficient_evidence",
            repository.load().caseProgress.getValue(definition.id).selectedDecisionId
        )
    }

    @Test
    fun caseEvidenceUsesTheSameImmutableVfsAssets() {
        val definition = Campaign01CaseCatalog.rx14
        assertEquals(EvidenceAssetCatalog.assets.map { it.evidenceId }.toSet(), definition.requiredEvidenceIds)
        assertTrue(EvidenceAssetCatalog.assets.all { it.path.startsWith("/A.E.G.I.S/evidence/first_contact/") })
    }

    private fun phase(definition: CaseDefinition, repository: GameStateRepository): CasePhase {
        val progress = repository.load().caseProgress[definition.id] ?: CaseProgress(definition.id)
        return CasePhaseEvaluator.phase(definition, progress)
    }
}
