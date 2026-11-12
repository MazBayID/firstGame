package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.CaseAnalysisOption
import com.zeroday.cyberprotocol.core.model.CaseDecisionOption
import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.GameLanguage

/**
 * Player-facing translations for authored First Contact case content.
 * IDs remain the stable source of truth for progression and save compatibility.
 */
internal object CaseLocalizedContent {
    fun title(definition: CaseDefinition, language: GameLanguage): String =
        if (language == GameLanguage.ENGLISH && definition.id == "first_contact.rx14")
            "RX-14 — Intake Timestamp Discrepancy" else definition.title

    fun briefing(definition: CaseDefinition, language: GameLanguage): String =
        if (language == GameLanguage.ENGLISH && definition.id == "first_contact.rx14") {
            "A.E.G.I.S. found two intake records for a training transmission carrying reference RX-14. Their contents and reference match, but their recorded times differ by four seconds. Your task is to inspect both sources, compare the findings, and choose a proportionate next step. Do not treat a timestamp difference as proof of tampering."
        } else definition.briefing

    fun analysisTitle(option: CaseAnalysisOption, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) option.title else when (option.id) {
            "observed_discrepancy_only" -> "A four-second discrepancy is observed; its cause is unproven"
            "tampering_confirmed" -> "Timestamp tampering has been proven"
            "clock_sync_cause" -> "Clock synchronization caused the discrepancy"
            else -> option.title
        }

    fun analysisDescription(option: CaseAnalysisOption, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) option.description else when (option.id) {
            "observed_discrepancy_only" -> "Both sources reference RX-14 at 09:41:12 and 09:41:16. The records establish a time difference, but do not identify its cause."
            "tampering_confirmed" -> "Treat the time difference as proof that one system altered its record."
            "clock_sync_cause" -> "Declare clock synchronization differences to be the definite cause."
            else -> option.description
        }

    fun analysisFeedback(option: CaseAnalysisOption, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) option.feedback else when (option.id) {
            "observed_discrepancy_only" -> "SUPPORTED CONCLUSION. The four-second difference is directly observable; its cause remains unknown."
            "tampering_confirmed" -> "UNSUPPORTED CONCLUSION. A time difference does not prove tampering or identify who changed the data."
            "clock_sync_cause" -> "UNSUPPORTED CONCLUSION. Clock synchronization is a possibility to test with additional logs, not an established cause."
            else -> option.feedback
        }

    fun decisionTitle(option: CaseDecisionOption, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) option.title else when (option.id) {
            "record_discrepancy" -> "Record the anomaly without assigning a cause"
            "request_clock_sync_logs" -> "Request clock synchronization logs"
            "request_processing_queue_logs" -> "Request processing queue logs"
            "insufficient_evidence" -> "Declare the evidence insufficient"
            else -> option.title
        }

    fun decisionDescription(option: CaseDecisionOption, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) option.description else when (option.id) {
            "record_discrepancy" -> "Report both timestamps and the four-second difference as facts. Mark the cause as unknown."
            "request_clock_sync_logs" -> "Check whether both systems used synchronized time sources before drawing a conclusion."
            "request_processing_queue_logs" -> "Investigate whether queueing or intake processing could explain the different recorded times."
            "insufficient_evidence" -> "Withhold a causal conclusion. The two records show a discrepancy but do not explain why it occurred."
            else -> option.description
        }
    /**
     * Translates only the on-screen rendering of the two RX-14 source records.
     * Canonical file bytes and Terminal output remain unchanged for provenance checks.
     */
    fun evidenceSource(evidenceId: String, content: String, language: GameLanguage): String {
        if (language != GameLanguage.ENGLISH) return content
        val labels = when (evidenceId) {
            "rx14_aegis_receipt" -> mapOf(
                "A.E.G.I.S. RECEIPT LOG" to "A.E.G.I.S. INTAKE RECEIPT LOG",
                "REFERENCE:" to "REFERENCE:",
                "RECEIVED:" to "RECEIVED:",
                "CHANNEL:" to "CHANNEL:",
                "RECORD:" to "RECORD:"
            )
            "rx14_archive_receipt" -> mapOf(
                "ARCHIVE RECEIPT LOG" to "ARCHIVE INTAKE RECEIPT LOG",
                "REFERENCE:" to "REFERENCE:",
                "RECEIVED:" to "RECEIVED:",
                "CHANNEL:" to "CHANNEL:",
                "RECORD:" to "RECORD:"
            )
            else -> return content
        }
        return content.lineSequence().map { line ->
            val separator = line.indexOf(':')
            if (separator < 0) labels[line] ?: line
            else {
                val label = line.substring(0, separator + 1)
                val value = line.substring(separator + 1)
                labels[label]?.plus(value) ?: line
            }
        }.joinToString("\n")
    }

}
