package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.CasePhase
import com.zeroday.cyberprotocol.core.model.CasePhaseEvaluator
import com.zeroday.cyberprotocol.core.model.CaseProgress
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository

/** Deterministic case progression with explicit evidence and decision gates. */
class UpdateCaseProgressUseCase(private val repository: GameStateRepository) {
    fun acknowledgeBriefing(definition: CaseDefinition): GameState =
        update(definition) { progress ->
            if (progress.briefingAcknowledged) progress
            else {
                require(CasePhaseEvaluator.phase(definition, progress) == CasePhase.BRIEFING) {
                    "The case briefing is not the active phase."
                }
                progress.copy(briefingAcknowledged = true)
            }
        }

    fun discoverEvidence(definition: CaseDefinition, evidenceId: String): GameState {
        require(evidenceId in definition.requiredEvidenceIds || evidenceId in definition.optionalEvidenceIds) {
            "Evidence is not part of this case."
        }
        val current = repository.load()
        require(current.evidence.any { it.id == evidenceId }) { "Evidence record not found." }
        val progress = current.caseProgress[definition.id] ?: CaseProgress(definition.id)
        if (evidenceId in progress.discoveredEvidenceIds) return current
        val phase = CasePhaseEvaluator.phase(definition, progress)
        val isRequired = evidenceId in definition.requiredEvidenceIds
        require(
            phase == CasePhase.INVESTIGATION ||
                (!isRequired && phase == CasePhase.ANALYSIS)
        ) {
            "Required evidence is inspected during investigation; optional leads may also be inspected during analysis."
        }
        return update(definition) { it.copy(discoveredEvidenceIds = it.discoveredEvidenceIds + evidenceId) }
    }

    fun analyzeEvidence(definition: CaseDefinition, evidenceId: String): GameState {
        val current = repository.load()
        val progress = current.caseProgress[definition.id] ?: CaseProgress(definition.id)
        require(evidenceId in progress.discoveredEvidenceIds) {
            "Evidence must be discovered before it can be analyzed."
        }
        if (evidenceId in progress.analyzedEvidenceIds) return current
        require(CasePhaseEvaluator.phase(definition, progress) == CasePhase.ANALYSIS) {
            "Complete the required investigation before analyzing evidence."
        }
        return update(definition) { it.copy(analyzedEvidenceIds = it.analyzedEvidenceIds + evidenceId) }
    }

    fun selectDecision(definition: CaseDefinition, decisionId: String): GameState {
        val current = repository.load()
        val progress = current.caseProgress[definition.id] ?: CaseProgress(definition.id)
        if (progress.selectedDecisionId == decisionId) return current
        require(CasePhaseEvaluator.phase(definition, progress) == CasePhase.OPERATIONAL_DECISION) {
            "Required evidence must be discovered and analyzed before an operational decision."
        }
        require(CasePhaseEvaluator.canSelectDecision(definition, progress, decisionId)) {
            "This decision requires additional evidence."
        }
        require(progress.selectedDecisionId == null) {
            "A decision has already been recorded for this case."
        }
        return update(definition) { it.copy(selectedDecisionId = decisionId) }
    }

    fun acknowledgeDebrief(definition: CaseDefinition): GameState {
        val progress = repository.load().caseProgress[definition.id] ?: CaseProgress(definition.id)
        require(progress.selectedDecisionId != null) { "An operational decision is required before debrief." }
        if (progress.debriefViewed) return repository.load()
        require(CasePhaseEvaluator.phase(definition, progress) == CasePhase.DEBRIEFING) {
            "The case must be in debriefing before it can be completed."
        }
        return update(definition) { it.copy(debriefViewed = true) }
    }

    private fun update(
        definition: CaseDefinition,
        transform: (CaseProgress) -> CaseProgress
    ): GameState {
        val current = repository.load()
        val progress = current.caseProgress[definition.id] ?: CaseProgress(definition.id)
        val updated = current.copy(
            caseProgress = current.caseProgress + (definition.id to transform(progress)),
            lastCheckpoint = "case_progress:" + definition.id
        )
        repository.save(updated)
        return repository.load()
    }
}
