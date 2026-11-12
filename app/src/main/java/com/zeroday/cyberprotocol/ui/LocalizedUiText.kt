package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.CasePhase
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameLanguage

internal fun localizedUiText(language: GameLanguage, english: String, indonesian: String): String =
    if (language == GameLanguage.ENGLISH) english else indonesian

internal fun localizedEvidenceStatus(status: EvidenceStatus, language: GameLanguage): String =
    when (status) {
        EvidenceStatus.UNVERIFIED -> localizedUiText(language, "UNVERIFIED", "BELUM TERVERIFIKASI")
        EvidenceStatus.INFERRED -> localizedUiText(language, "INFERRED", "DISIMPULKAN")
        EvidenceStatus.CORROBORATED -> localizedUiText(language, "CORROBORATED", "DIKOROBORASI")
        EvidenceStatus.CONFIRMED -> localizedUiText(language, "CONFIRMED", "TERKONFIRMASI")
        EvidenceStatus.DISPUTED -> localizedUiText(language, "DISPUTED", "DIPERSELISIHKAN")
        EvidenceStatus.RETRACTED -> localizedUiText(language, "RETRACTED", "DICABUT")
    }

internal fun localizedPhaseName(phase: CasePhase, language: GameLanguage): String =
    when (phase) {
        CasePhase.BRIEFING -> localizedUiText(language, "BRIEFING", "BRIEFING")
        CasePhase.INVESTIGATION -> localizedUiText(language, "INVESTIGATION", "INVESTIGASI")
        CasePhase.ANALYSIS -> localizedUiText(language, "ANALYSIS", "ANALISIS")
        CasePhase.OPERATIONAL_DECISION -> localizedUiText(language, "OPERATIONAL DECISION", "KEPUTUSAN OPERASIONAL")
        CasePhase.DEBRIEFING -> localizedUiText(language, "DEBRIEFING", "DEBRIEFING")
        CasePhase.COMPLETE -> localizedUiText(language, "COMPLETE", "SELESAI")
    }
