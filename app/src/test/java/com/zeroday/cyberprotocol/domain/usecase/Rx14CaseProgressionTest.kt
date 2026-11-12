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

        val rationale = "Dua waktu berbeda empat detik, tetapi tidak ada sumber yang menjelaskan penyebabnya."
        useCase.saveDecisionRationaleDraft(definition, rationale)
        assertEquals(rationale, repository.load().caseProgress.getValue(definition.id).decisionRationale)
        assertEquals(CasePhase.OPERATIONAL_DECISION, phase(definition, repository))
        useCase.selectDecision(definition, "insufficient_evidence", rationale)
        assertEquals(CasePhase.DEBRIEFING, phase(definition, repository))

        useCase.acknowledgeDebrief(definition)
        assertEquals(CasePhase.COMPLETE, phase(definition, repository))
        assertEquals(
            "insufficient_evidence",
            repository.load().caseProgress.getValue(definition.id).selectedDecisionId
        )
    }

    @Test
    fun decisionRationaleDraftCannotBeChangedBeforeOperationalDecision() {
        val definition = Campaign01CaseCatalog.rx14
        val repository = MemoryRepository(GameState())
        val useCase = UpdateCaseProgressUseCase(repository)

        val error = runCatching {
            useCase.saveDecisionRationaleDraft(definition, "I will preserve the discrepancy as unresolved.")
        }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
        assertEquals("", repository.load().caseProgress[definition.id]?.decisionRationale.orEmpty())
    }

    @Test
    fun decisionRationaleDraftCannotExceedStorageLimit() {
        val definition = Campaign01CaseCatalog.rx14
        val records = EvidenceAssetCatalog.assets.map { asset ->
            EvidenceRecord(id = asset.evidenceId, title = asset.title, summary = asset.content.trim(),
                source = asset.sourceLabel, missionId = EvidenceAssetCatalog.MISSION_ID)
        }
        val repository = MemoryRepository(GameState(evidence = records))
        val useCase = UpdateCaseProgressUseCase(repository)
        useCase.acknowledgeBriefing(definition)
        definition.requiredEvidenceIds.forEach { useCase.discoverEvidence(definition, it) }
        definition.requiredEvidenceIds.forEach { useCase.analyzeEvidence(definition, it) }
        useCase.selectAnalysisConclusion(definition, "observed_discrepancy_only")

        val error = runCatching { useCase.saveDecisionRationaleDraft(definition, "x".repeat(601)) }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
        assertEquals("", repository.load().caseProgress.getValue(definition.id).decisionRationale)
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
