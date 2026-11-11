package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class EvidenceAnalysisFilterTest {
    private val records = listOf(
        EvidenceRecord(
            id = "older",
            title = "Receipt mismatch",
            summary = "Two timestamps differ",
            source = "Archive",
            status = EvidenceStatus.UNVERIFIED,
            missionId = "first_contact.compare",
            recordedAtEpochMillis = 10L
        ),
        EvidenceRecord(
            id = "newer",
            title = "Verified signal",
            summary = "Training output matched",
            source = "Python evaluator",
            status = EvidenceStatus.CONFIRMED,
            missionId = "first_contact.signal",
            recordedAtEpochMillis = 20L
        ),
        EvidenceRecord(
            id = "inference",
            title = "Queue delay hypothesis",
            summary = "Possible processing delay",
            source = "Analyst note",
            status = EvidenceStatus.INFERRED,
            missionId = "first_contact.review",
            recordedAtEpochMillis = 15L
        )
    )

    @Test
    fun allRecordsAreSortedNewestFirst() {
        assertEquals(listOf("newer", "inference", "older"),
            EvidenceAnalysisFilter.filter(records, null, "").map { it.id })
    }

    @Test
    fun statusFilterDoesNotMixEvidenceClasses() {
        assertEquals(listOf("older"),
            EvidenceAnalysisFilter.filter(records, EvidenceStatus.UNVERIFIED, "").map { it.id })
    }

    @Test
    fun searchMatchesSourceMissionAndSummaryWithoutCaseSensitivity() {
        assertEquals(listOf("newer"),
            EvidenceAnalysisFilter.filter(records, null, "PYTHON").map { it.id })
        assertEquals(listOf("older"),
            EvidenceAnalysisFilter.filter(records, null, "first_contact.compare").map { it.id })
        assertEquals(listOf("inference"),
            EvidenceAnalysisFilter.filter(records, null, "PROCESSING DELAY").map { it.id })
    }

    @Test
    fun statusAndSearchFiltersCanBeCombined() {
        assertEquals(emptyList<String>(),
            EvidenceAnalysisFilter.filter(records, EvidenceStatus.CONFIRMED, "timestamp").map { it.id })
        assertEquals(listOf("newer"),
            EvidenceAnalysisFilter.filter(records, EvidenceStatus.CONFIRMED, "signal").map { it.id })
    }
}
