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

internal fun localizedEvidenceStatus(status: String, language: GameLanguage): String =
    EvidenceStatus.entries.firstOrNull { it.name.equals(status, ignoreCase = true) }
        ?.let { localizedEvidenceStatus(it, language) }
        ?: status

internal fun localizedPhaseName(phase: CasePhase, language: GameLanguage): String =
    when (phase) {
        CasePhase.BRIEFING -> localizedUiText(language, "BRIEFING", "BRIEFING")
        CasePhase.INVESTIGATION -> localizedUiText(language, "INVESTIGATION", "INVESTIGASI")
        CasePhase.ANALYSIS -> localizedUiText(language, "ANALYSIS", "ANALISIS")
        CasePhase.OPERATIONAL_DECISION -> localizedUiText(language, "OPERATIONAL DECISION", "KEPUTUSAN OPERASIONAL")
        CasePhase.DEBRIEFING -> localizedUiText(language, "DEBRIEFING", "DEBRIEFING")
        CasePhase.COMPLETE -> localizedUiText(language, "COMPLETE", "SELESAI")
    }

internal fun localizedRankName(rank: String, language: GameLanguage): String {
    val english = when (rank) {
        "CIVILIAN" -> "CIVILIAN"
        "RECRUIT" -> "RECRUIT"
        "INITIATE" -> "INITIATE"
        "JUNIOR_ANALYST" -> "JUNIOR ANALYST"
        "FIELD_ANALYST" -> "FIELD ANALYST"
        "CYBER_OPERATIVE" -> "CYBER OPERATIVE"
        "SENIOR_OPERATIVE" -> "SENIOR OPERATIVE"
        "SPECIAL_AGENT" -> "SPECIAL AGENT"
        "ELITE_OPERATIVE" -> "ELITE OPERATIVE"
        else -> rank.replace('_', ' ')
    }
    if (language == GameLanguage.ENGLISH) return english
    return when (rank) {
        "CIVILIAN" -> "SIPIL"
        "RECRUIT" -> "REKRUT"
        "INITIATE" -> "PEMULA"
        "JUNIOR_ANALYST" -> "ANALIS JUNIOR"
        "FIELD_ANALYST" -> "ANALIS LAPANGAN"
        "CYBER_OPERATIVE" -> "OPERATOR SIBER"
        "SENIOR_OPERATIVE" -> "OPERATOR SENIOR"
        "SPECIAL_AGENT" -> "AGEN KHUSUS"
        "ELITE_OPERATIVE" -> "OPERATOR ELIT"
        else -> english
    }
}

internal fun localizedEvidenceOrigin(origin: com.zeroday.cyberprotocol.core.model.EvidenceOrigin, language: GameLanguage): String =
    when (origin) {
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.LEGACY -> localizedUiText(language, "LEGACY", "LEGACY")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.MESSENGER -> localizedUiText(language, "MESSENGER", "PESAN")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.INTRANET -> localizedUiText(language, "INTRANET", "INTRANET")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.WORKSPACE_FILE -> localizedUiText(language, "WORKSPACE FILE", "FILE RUANG KERJA")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.TERMINAL -> localizedUiText(language, "TERMINAL", "TERMINAL")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.DATABASE -> localizedUiText(language, "DATABASE", "DATABASE")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.ANALYSIS -> localizedUiText(language, "ANALYSIS", "ANALISIS")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.OPERATIONS -> localizedUiText(language, "OPERATIONS", "OPERASI")
        com.zeroday.cyberprotocol.core.model.EvidenceOrigin.SYSTEM -> localizedUiText(language, "SYSTEM", "SISTEM")
    }
