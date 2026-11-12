package com.zeroday.cyberprotocol.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CasePhaseEvaluatorTest {
    private val definition = CaseDefinition(
        id = "case_alpha",
        campaignId = "campaign_test",
        title = "A fictional intelligence case",
        briefing = "Determine what the available records establish.",
        primaryArchetype = MissionArchetype.INVESTIGATION,
        requiredEvidenceIds = setOf("ev_primary", "ev_secondary"),
        optionalEvidenceIds = setOf("ev_optional"),
        decisionOptions = listOf(
            CaseDecisionOption("monitor", "Continue monitoring", "Request additional corroboration."),
            CaseDecisionOption("pause", "Pause operation", "Preserve sources while uncertainty remains.", setOf("ev_optional"))
        )
    )

    @Test fun phaseProgressionFollowsEvidenceAndDecisionGates() {
        var progress = CaseProgress("case_alpha")
        assertEquals(CasePhase.BRIEFING, CasePhaseEvaluator.phase(definition, progress))
        progress = progress.copy(briefingAcknowledged = true)
        assertEquals(CasePhase.INVESTIGATION, CasePhaseEvaluator.phase(definition, progress))
        progress = progress.copy(discoveredEvidenceIds = setOf("ev_primary", "ev_secondary"))
        assertEquals(CasePhase.ANALYSIS, CasePhaseEvaluator.phase(definition, progress))
        progress = progress.copy(analyzedEvidenceIds = setOf("ev_primary", "ev_secondary"))
        assertEquals(CasePhase.OPERATIONAL_DECISION, CasePhaseEvaluator.phase(definition, progress))
        progress = progress.copy(selectedDecisionId = "monitor")
        assertEquals(CasePhase.DEBRIEFING, CasePhaseEvaluator.phase(definition, progress))
        progress = progress.copy(debriefViewed = true)
        assertEquals(CasePhase.COMPLETE, CasePhaseEvaluator.phase(definition, progress))
    }

    @Test fun evidenceDiscoveryPolicyKeepsRequiredAndOptionalLeadsInTheirPhases() {
        var progress = CaseProgress("case_alpha")
        assertFalse(CasePhaseEvaluator.canDiscoverEvidence(definition, progress, "ev_primary"))
        progress = progress.copy(briefingAcknowledged = true)
        assertTrue(CasePhaseEvaluator.canDiscoverEvidence(definition, progress, "ev_primary"))
        assertTrue(CasePhaseEvaluator.canDiscoverEvidence(definition, progress, "ev_optional"))
        progress = progress.copy(discoveredEvidenceIds = setOf("ev_primary", "ev_secondary"))
        assertEquals(CasePhase.ANALYSIS, CasePhaseEvaluator.phase(definition, progress))
        assertFalse(CasePhaseEvaluator.canDiscoverEvidence(definition, progress, "ev_primary"))
        assertTrue(CasePhaseEvaluator.canDiscoverEvidence(definition, progress, "ev_optional"))
        assertFalse(CasePhaseEvaluator.canDiscoverEvidence(definition, progress, "unknown"))
    }

    @Test fun optionalEvidenceDoesNotBlockCaseProgressOrEveryDecision() {
        val progress = CaseProgress(
            caseId = "case_alpha",
            briefingAcknowledged = true,
            discoveredEvidenceIds = setOf("ev_primary", "ev_secondary"),
            analyzedEvidenceIds = setOf("ev_primary", "ev_secondary")
        )
        assertTrue(CasePhaseEvaluator.canSelectDecision(definition, progress, "monitor"))
        assertFalse(CasePhaseEvaluator.canSelectDecision(definition, progress, "pause"))
        assertEquals(CasePhase.OPERATIONAL_DECISION, CasePhaseEvaluator.phase(definition, progress))
    }

    @Test fun caseProgressMustMatchCaseDefinition() {
        val error = runCatching {
            CasePhaseEvaluator.phase(definition, CaseProgress("another_case"))
        }.exceptionOrNull()
        assertTrue(error is IllegalArgumentException)
    }
}
