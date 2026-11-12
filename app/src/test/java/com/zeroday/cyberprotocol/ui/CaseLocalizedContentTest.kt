package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.data.Campaign01CaseCatalog
import com.zeroday.cyberprotocol.data.Campaign01Catalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaseLocalizedContentTest {
    @Test
    fun rx14CaseBriefingAndTitleRespectSelectedLanguage() {
        val definition = Campaign01CaseCatalog.rx14
        assertEquals("RX-14 — Selisih Waktu Penerimaan",
            CaseLocalizedContent.title(definition, GameLanguage.INDONESIAN))
        assertEquals("RX-14 — Intake Timestamp Discrepancy",
            CaseLocalizedContent.title(definition, GameLanguage.ENGLISH))
        assertTrue(CaseLocalizedContent.briefing(definition, GameLanguage.ENGLISH)
            .contains("Do not treat a timestamp difference as proof of tampering."))
        assertEquals(definition.briefing,
            CaseLocalizedContent.briefing(definition, GameLanguage.INDONESIAN))
    }

    @Test
    fun everyAuthoredFirstContactMissionHasEnglishPlayerFacingCopy() {
        Campaign01Catalog.missions.forEach { mission ->
            assertNotEquals(mission.title,
                CaseLocalizedContent.missionTitle(mission, GameLanguage.ENGLISH))
            assertNotEquals(mission.briefing,
                CaseLocalizedContent.missionBriefing(mission, GameLanguage.ENGLISH))
            if (mission.nextClue != null) {
                assertNotEquals(mission.nextClue,
                    CaseLocalizedContent.missionNextClue(mission, GameLanguage.ENGLISH))
            }
            mission.objectives.forEachIndexed { index, objective ->
                assertNotEquals(objective.title,
                    CaseLocalizedContent.objectiveTitle(mission, objective, index, GameLanguage.ENGLISH))
                assertNotEquals(objective.description,
                    CaseLocalizedContent.objectiveDescription(mission, objective, index, GameLanguage.ENGLISH))
            }
            mission.hints.forEach { hint ->
                assertNotEquals(hint.message,
                    CaseLocalizedContent.hint(mission, hint, GameLanguage.ENGLISH))
            }
        }
    }

    @Test
    fun caseAnalysisAndDecisionCopyIsLocalizedByStableIds() {
        val definition = Campaign01CaseCatalog.rx14
        definition.analysisOptions.forEach { option ->
            assertTrue(CaseLocalizedContent.analysisTitle(option, GameLanguage.ENGLISH).isNotBlank())
            assertTrue(CaseLocalizedContent.analysisDescription(option, GameLanguage.ENGLISH).isNotBlank())
            assertTrue(CaseLocalizedContent.analysisFeedback(option, GameLanguage.ENGLISH).isNotBlank())
        }
        definition.decisionOptions.forEach { option ->
            assertTrue(CaseLocalizedContent.decisionTitle(option, GameLanguage.ENGLISH).isNotBlank())
            assertTrue(CaseLocalizedContent.decisionDescription(option, GameLanguage.ENGLISH).isNotBlank())
        }
    }

    @Test
    fun sourceTranslationDoesNotChangeCanonicalIndonesianRecordBytes() {
        val original = "A.E.G.I.S. RECEIPT LOG\nREFERENCE: RX-14\nRECEIVED: 09:41:12\n"
        assertEquals(original, CaseLocalizedContent.evidenceSource(
            "rx14_aegis_receipt", original, GameLanguage.INDONESIAN
        ))
        assertTrue(CaseLocalizedContent.evidenceSource(
            "rx14_aegis_receipt", original, GameLanguage.ENGLISH
        ).contains("09:41:12"))
    }
}
