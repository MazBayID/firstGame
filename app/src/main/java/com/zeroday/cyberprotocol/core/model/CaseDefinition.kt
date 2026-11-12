package com.zeroday.cyberprotocol.core.model

/** Dominant player experience for a mission; secondary activities never replace its focus. */
enum class MissionArchetype { NARRATIVE, INVESTIGATION, TECHNICAL, ANALYTICAL, OPERATIONAL }

/** The five authored experience phases are not mandatory application-navigation steps. */
enum class CasePhase { BRIEFING, INVESTIGATION, ANALYSIS, OPERATIONAL_DECISION, DEBRIEFING, COMPLETE }

data class CaseDecisionOption(
    val id: String,
    val title: String,
    val description: String,
    val requiredEvidenceIds: Set<String> = emptySet()
) {
    init {
        require(id.isNotBlank())
        require(title.isNotBlank())
        require(description.isNotBlank())
        require(requiredEvidenceIds.none { it.isBlank() })
    }
}

data class CaseDefinition(
    val id: String,
    val campaignId: String,
    val title: String,
    val briefing: String,
    val primaryArchetype: MissionArchetype,
    val requiredEvidenceIds: Set<String>,
    val optionalEvidenceIds: Set<String> = emptySet(),
    val decisionOptions: List<CaseDecisionOption>
) {
    init {
        require(id.isNotBlank() && campaignId.isNotBlank())
        require(title.isNotBlank() && briefing.isNotBlank())
        require(requiredEvidenceIds.isNotEmpty() && requiredEvidenceIds.none { it.isBlank() })
        require(optionalEvidenceIds.none { it.isBlank() })
        require(requiredEvidenceIds.intersect(optionalEvidenceIds).isEmpty())
        require(decisionOptions.isNotEmpty())
        require(decisionOptions.map { it.id }.distinct().size == decisionOptions.size)
        require(decisionOptions.all {
            it.requiredEvidenceIds.all { evidenceId ->
                evidenceId in requiredEvidenceIds || evidenceId in optionalEvidenceIds
            }
        })
    }
}

/** Player-owned case progress. It is persisted independently of mission and rank progress. */
data class CaseProgress(
    val caseId: String,
    val briefingAcknowledged: Boolean = false,
    val discoveredEvidenceIds: Set<String> = emptySet(),
    val analyzedEvidenceIds: Set<String> = emptySet(),
    val selectedDecisionId: String? = null,
    val debriefViewed: Boolean = false
) {
    init {
        require(caseId.isNotBlank())
        require(discoveredEvidenceIds.none { it.isBlank() })
        require(analyzedEvidenceIds.none { it.isBlank() })
        require(selectedDecisionId == null || selectedDecisionId.isNotBlank())
    }
}

object CasePhaseEvaluator {
    fun phase(definition: CaseDefinition, progress: CaseProgress): CasePhase {
        require(definition.id == progress.caseId)
        if (!progress.briefingAcknowledged) return CasePhase.BRIEFING
        if (!definition.requiredEvidenceIds.all { it in progress.discoveredEvidenceIds }) {
            return CasePhase.INVESTIGATION
        }
        if (!definition.requiredEvidenceIds.all { it in progress.analyzedEvidenceIds }) {
            return CasePhase.ANALYSIS
        }
        if (progress.selectedDecisionId == null) return CasePhase.OPERATIONAL_DECISION
        if (!progress.debriefViewed) return CasePhase.DEBRIEFING
        return CasePhase.COMPLETE
    }

    fun canSelectDecision(
        definition: CaseDefinition,
        progress: CaseProgress,
        decisionId: String
    ): Boolean {
        require(definition.id == progress.caseId)
        val option = definition.decisionOptions.firstOrNull { it.id == decisionId } ?: return false
        return definition.requiredEvidenceIds.all {
            it in progress.discoveredEvidenceIds && it in progress.analyzedEvidenceIds
        } && option.requiredEvidenceIds.all {
            it in progress.discoveredEvidenceIds && it in progress.analyzedEvidenceIds
        }
    }
}
