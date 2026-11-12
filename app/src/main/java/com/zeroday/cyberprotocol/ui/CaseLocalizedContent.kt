package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.CaseAnalysisOption
import com.zeroday.cyberprotocol.core.model.CaseDecisionOption
import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.GameLanguage
import com.zeroday.cyberprotocol.core.model.MissionDefinition
import com.zeroday.cyberprotocol.core.model.MissionObjective
import com.zeroday.cyberprotocol.core.model.MissionHint
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.data.Campaign01Catalog

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

    fun missionTitle(mission: MissionDefinition, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) mission.title else when (mission.id) {
            "first_contact.signal" -> "First Signal"
            "first_contact.identity" -> "Identity Verification"
            "first_contact.audit" -> "System Trace Audit"
            "first_contact.inventory" -> "Record Inventory"
            "first_contact.trace" -> "Reference Trace"
            "first_contact.compare" -> "Compare Two Sources"
            "first_contact.normalize" -> "Time Normalization"
            "first_contact.validate_delta" -> "Validate the Difference"
            else -> mission.title
        }

    fun missionBriefing(mission: MissionDefinition, language: GameLanguage): String =
        if (language != GameLanguage.ENGLISH) mission.briefing else when (mission.id) {
            "first_contact.signal" -> "A.E.G.I.S. has placed a message fragment in the intranet File Repository. Download message_fragment.txt, read it in Terminal with cat, store its text in a variable named signal, then display its value."
            "first_contact.identity" -> "The first signal reveals a verification message. Download identity_fragment.txt from the A.E.G.I.S. File Repository and read it in Terminal with cat. Store its exact contents in a variable named message, then display it with print(message). Freya will check the result."
            "first_contact.audit" -> "Inspect the training log supplied by A.E.G.I.S. Count only the lines containing ERROR. Use for and if; do not modify the source data."
            "first_contact.inventory" -> "The second source provides a list named records. Count the available references with len(), store the result in record_count, then display it."
            "first_contact.trace" -> "Use enumerate() to find lines containing RX-14. Display both the index and line so each finding retains its source position."
            "first_contact.compare" -> "Use list indexing to retrieve the first and second entries from receipts. Store them as primary and archive, then display both without changing the source."
            "first_contact.normalize" -> "The two intake times are provided as numeric strings representing seconds since midnight. Convert both with int(), store them as first_time and second_time, then calculate delta = second_time - first_time. Preserve source order; a negative result is valid."
            "first_contact.validate_delta" -> "Calculate the magnitude of the time difference in receipt_seconds. Convert both text values to integers, subtract one from the other, then use abs() so difference is non-negative. This measures the time gap; it does not explain its cause."
            else -> mission.briefing
        }

    fun missionNextClue(mission: MissionDefinition, language: GameLanguage): String? {
        if (language != GameLanguage.ENGLISH) return mission.nextClue
        return when (mission.id) {
            "first_contact.signal" -> "The next fragment uses a variable named message. Check the requested variable name before displaying its value."
            "first_contact.identity" -> "Two training systems stored receipts for one transmission. Compare their sources and recorded times."
            "first_contact.audit" -> "Logs are not the only source. A separate inventory stores the record references."
            "first_contact.inventory" -> "RX-14 appears in more than one record. Trace the list without losing each item's position."
            "first_contact.trace" -> "Indexes 0 and 1 point to two records that should be read side by side."
            "first_contact.compare" -> "A four-second difference has been observed. Time alone does not explain its cause; state the limits of your conclusion."
            "first_contact.normalize" -> "The signed value shows the direction of the difference. The final step measures its magnitude without a sign."
            "first_contact.validate_delta" -> "The final First Contact report is ready. Record the observed number, its sources, and what cannot yet be concluded."
            else -> mission.nextClue
        }
    }

    fun objectiveTitle(mission: MissionDefinition, objective: MissionObjective, index: Int, language: GameLanguage): String {
        if (language != GameLanguage.ENGLISH) return objective.title
        return when (mission.id to objective.id) {
            "first_contact.signal" to "assign_signal" -> "Store the signal"
            "first_contact.signal" to "display_signal" -> "Display the signal"
            "first_contact.identity" to "assign_message" -> "Store the verification message"
            "first_contact.identity" to "display_message" -> "Display the message"
            "first_contact.audit" to "scan_events" -> "Inspect every entry"
            "first_contact.audit" to "count_errors" -> "Count ERROR entries"
            "first_contact.inventory" to "count_records" -> "Count the references"
            "first_contact.inventory" to "display_count" -> "Display the count"
            "first_contact.trace" to "enumerate_receipts" -> "Traverse the list"
            "first_contact.trace" to "filter_reference" -> "Select the reference"
            "first_contact.compare" to "read_primary" -> "Retrieve the primary record"
            "first_contact.compare" to "read_archive" -> "Retrieve the archive record"
            "first_contact.compare" to "display_sources" -> "Display both sources"
            "first_contact.normalize" to "convert_first" -> "Convert the first time"
            "first_contact.normalize" to "convert_second" -> "Convert the second time"
            "first_contact.normalize" to "calculate_delta" -> "Calculate the signed difference"
            "first_contact.validate_delta" to "absolute_delta" -> "Calculate the magnitude"
            "first_contact.validate_delta" to "display_delta" -> "Display the result"
            else -> objective.title
        }
    }

    fun objectiveDescription(mission: MissionDefinition, objective: MissionObjective, index: Int, language: GameLanguage): String {
        if (language != GameLanguage.ENGLISH) return objective.description
        return when (mission.id to objective.id) {
            "first_contact.signal" to "assign_signal" -> "Read message_fragment.txt from the workspace, then store its text in a variable named signal."
            "first_contact.signal" to "display_signal" -> "Use print() to display the contents of the signal variable."
            "first_contact.identity" to "assign_message" -> "Read identity_fragment.txt and store its message in a variable named message."
            "first_contact.identity" to "display_message" -> "Use print() to display the contents of message."
            "first_contact.audit" to "scan_events" -> "Use for to inspect every item in events."
            "first_contact.audit" to "count_errors" -> "Use if to count items containing the text ERROR."
            "first_contact.inventory" to "count_records" -> "Use len(records) to obtain the number of items."
            "first_contact.inventory" to "display_count" -> "Store the result in record_count and display it with print()."
            "first_contact.trace" to "enumerate_receipts" -> "Use enumerate(receipts) to obtain each item's index and content."
            "first_contact.trace" to "filter_reference" -> "Use if to display only lines containing RX-14."
            "first_contact.compare" to "read_primary" -> "Use receipts[0] and store it in a variable named primary."
            "first_contact.compare" to "read_archive" -> "Use receipts[1] and store it in a variable named archive."
            "first_contact.compare" to "display_sources" -> "Display primary and archive in that order."
            "first_contact.normalize" to "convert_first" -> "Use int() to convert receipt_seconds[0] and store it in first_time."
            "first_contact.normalize" to "convert_second" -> "Convert receipt_seconds[1] and store it in second_time."
            "first_contact.normalize" to "calculate_delta" -> "Subtract first_time from second_time and store the result in delta."
            "first_contact.validate_delta" to "absolute_delta" -> "Use abs() on the subtraction of both times after converting them with int()."
            "first_contact.validate_delta" to "display_delta" -> "Store the result in difference and display it with print()."
            else -> objective.description
        }
    }

    fun hint(mission: MissionDefinition, hint: MissionHint, language: GameLanguage): String {
        if (language != GameLanguage.ENGLISH) return hint.message
        return when (mission.id to hint.level) {
            "first_contact.signal" to 1 -> "Use cat message_fragment.txt in Terminal to read the downloaded fragment."
            "first_contact.signal" to 2 -> "Store the fragment text in a variable named signal using the pattern signal = \"text\"."
            "first_contact.signal" to 3 -> "Display the variable with print(signal)."
            "first_contact.identity" to 1 -> "Download identity_fragment.txt from the File Repository, then read it with cat in Terminal."
            "first_contact.identity" to 2 -> "Store the exact file contents in a variable named message."
            "first_contact.identity" to 3 -> "Display the variable with print(message)."
            "first_contact.audit" to 1 -> "Start with count = 0 before the loop."
            "first_contact.audit" to 2 -> "Inside for, check whether \"ERROR\" is contained in event."
            "first_contact.audit" to 3 -> "When the condition is true, increment count with count += 1, then print(count)."
            "first_contact.inventory" to 1 -> "len() accepts a list and returns its number of items."
            "first_contact.inventory" to 2 -> "Use record_count = len(records)."
            "first_contact.inventory" to 3 -> "Display the result with print(record_count)."
            "first_contact.trace" to 1 -> "The basic form is: for index, receipt in enumerate(receipts):"
            "first_contact.trace" to 2 -> "Inside the loop, check whether \"RX-14\" is contained in receipt."
            "first_contact.trace" to 3 -> "Use print(index, receipt) when the condition is true."
            "first_contact.compare" to 1 -> "List indexes start at 0, so the first item is receipts[0]."
            "first_contact.compare" to 2 -> "Use receipts[1] for the second item."
            "first_contact.compare" to 3 -> "Store each item before calling print()."
            "first_contact.normalize" to 1 -> "The input values are text; int() converts numeric text to an integer."
            "first_contact.normalize" to 2 -> "Retrieve the two items with receipt_seconds[0] and receipt_seconds[1]."
            "first_contact.normalize" to 3 -> "Calculate delta = second_time - first_time, then print(delta)."
            "first_contact.validate_delta" to 1 -> "abs(value) returns the magnitude without a negative sign."
            "first_contact.validate_delta" to 2 -> "Use int() on both items before subtracting."
            "first_contact.validate_delta" to 3 -> "Store abs(int(receipt_seconds[1]) - int(receipt_seconds[0])) in difference."
            else -> hint.message
        }
    }

    fun evidenceTitle(record: EvidenceRecord, language: GameLanguage): String {
        if (language != GameLanguage.ENGLISH) return record.title
        return when (record.id) {
            "rx14_aegis_receipt" -> "A.E.G.I.S. Receipt Log — RX-14"
            "rx14_archive_receipt" -> "Archive Receipt Log — RX-14"
            "transmission_timestamp_discrepancy" -> "Transmission Receipt Timestamp Discrepancy"
            else -> if (record.id.startsWith("evidence_")) {
                Campaign01Catalog.find(record.missionId)?.let { missionTitle(it, language) } ?: record.title
            } else record.title
        }
    }

    fun evidenceSummary(record: EvidenceRecord, language: GameLanguage): String {
        if (language != GameLanguage.ENGLISH) return record.summary
        if (record.id == "transmission_timestamp_discrepancy") {
            return "Two internal systems recorded reference RX-14 at times four seconds apart. The difference is observable, but its cause is unknown."
        }
        if (record.id.startsWith("evidence_")) {
            val mission = Campaign01Catalog.find(record.missionId) ?: return record.summary
            return if (mission.id in Campaign01Catalog.retiredLegacyStoryMissionIds) {
                "Optional training submission verified by the A.E.G.I.S. evaluator."
            } else {
                "Mission submission verified by the A.E.G.I.S. evaluator."
            }
        }
        return record.summary
    }

    fun evidenceSource(record: EvidenceRecord, language: GameLanguage): String {
        if (record.id.startsWith("evidence_")) {
            val mission = Campaign01Catalog.find(record.missionId)
            if (mission != null && mission.evaluatorId != null) {
                val evaluatorId = mission.evaluatorId
                val optional = mission.id in Campaign01Catalog.retiredLegacyStoryMissionIds
                return if (language == GameLanguage.ENGLISH) {
                    if (optional) "A.E.G.I.S. / optional Python practice evaluator $evaluatorId"
                    else "A.E.G.I.S. / Python mission evaluator $evaluatorId"
                } else {
                    if (optional) "A.E.G.I.S. / evaluator Python latihan opsional $evaluatorId"
                    else "A.E.G.I.S. / evaluator misi Python $evaluatorId"
                }
            }
        }
        if (language != GameLanguage.ENGLISH) return record.source
        return when (record.id) {
            "rx14_aegis_receipt" -> "A.E.G.I.S. Intranet / Receipt Log"
            "rx14_archive_receipt" -> "A.E.G.I.S. Archive / Receipt Log"
            "transmission_timestamp_discrepancy" -> "A.E.G.I.S. / receipt metadata comparison"
            else -> record.source
        }
    }

}
