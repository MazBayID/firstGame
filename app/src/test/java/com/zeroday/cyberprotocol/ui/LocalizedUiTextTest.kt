package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.CasePhase
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameLanguage
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalizedUiTextTest {
    @Test fun sharedUiTextRespectsSelectedLanguage() {
        assertEquals("BACK", localizedUiText(GameLanguage.ENGLISH, "BACK", "KEMBALI"))
        assertEquals("KEMBALI", localizedUiText(GameLanguage.INDONESIAN, "BACK", "KEMBALI"))
    }

    @Test fun evidenceStatusesAreLocalizedWithoutChangingTheirIdentifiers() {
        assertEquals("CORROBORATED", localizedEvidenceStatus(EvidenceStatus.CORROBORATED, GameLanguage.ENGLISH))
        assertEquals("DIKOROBORASI", localizedEvidenceStatus(EvidenceStatus.CORROBORATED, GameLanguage.INDONESIAN))
        assertEquals("CORROBORATED", EvidenceStatus.CORROBORATED.name)
    }

    @Test fun casePhasesHaveLocalizedDisplayNames() {
        assertEquals("OPERATIONAL DECISION", localizedPhaseName(CasePhase.OPERATIONAL_DECISION, GameLanguage.ENGLISH))
        assertEquals("KEPUTUSAN OPERASIONAL", localizedPhaseName(CasePhase.OPERATIONAL_DECISION, GameLanguage.INDONESIAN))
    }
}
