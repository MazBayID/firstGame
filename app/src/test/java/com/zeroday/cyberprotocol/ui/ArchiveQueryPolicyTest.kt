package com.zeroday.cyberprotocol.ui

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.GameLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArchiveQueryPolicyTest {
    @Test
    fun missionArchiveShowsOnlyCompletedFilesAndSearchesLocalizedContent() {
        val completed = setOf("first_contact.signal")
        val english = ArchiveQueryPolicy.completedMissions(
            completed, "first signal", GameLanguage.ENGLISH
        )
        assertEquals(listOf("first_contact.signal"), english.map { it.id })

        val indonesian = ArchiveQueryPolicy.completedMissions(
            completed, "sinyal", GameLanguage.INDONESIAN
        )
        assertEquals(listOf("first_contact.signal"), indonesian.map { it.id })

        assertTrue(ArchiveQueryPolicy.completedMissions(
            completed, "identity", GameLanguage.ENGLISH
        ).isEmpty())
    }

    @Test
    fun undiscoveredCaseEvidenceSummaryCannotLeakThroughArchiveSearchOrDetails() {
        val hidden = EvidenceRecord(
            id = "rx14_hidden",
            title = "RX-14 receipt",
            summary = "The two records differ by four seconds.",
            source = "A.E.G.I.S. / receipt metadata",
            missionId = "first_contact.rx14",
            recordedAtEpochMillis = 20L
        )
        val concealed = setOf(hidden.id)

        assertEquals(emptyList<String>(), ArchiveQueryPolicy.evidence(
            listOf(hidden), "four seconds", GameLanguage.ENGLISH, concealed
        ).map { it.id })
        assertEquals(listOf(hidden.id), ArchiveQueryPolicy.evidence(
            listOf(hidden), "RX-14", GameLanguage.ENGLISH, concealed
        ).map { it.id })

        val englishSummary = ArchiveQueryPolicy.evidenceSummary(hidden, concealed, GameLanguage.ENGLISH)
        val indonesianSummary = ArchiveQueryPolicy.evidenceSummary(hidden, concealed, GameLanguage.INDONESIAN)
        assertEquals("Source content has not been discovered yet.", englishSummary)
        assertEquals("Isi sumber belum ditemukan.", indonesianSummary)
        assertEquals(false, englishSummary.contains("four seconds"))
        assertEquals(false, indonesianSummary.contains("empat detik"))
    }

    @Test
    fun evidenceArchiveSortsNewestFirstAndSearchesLocalizedEvidenceMetadata() {
        val missionRecord = EvidenceRecord(
            id = "evidence_first_contact.signal",
            title = "Sinyal Pertama",
            summary = "Hasil tugas latihan berhasil diverifikasi oleh evaluator A.E.G.I.S.",
            source = "A.E.G.I.S. / Python evaluator 9",
            missionId = "first_contact.signal",
            recordedAtEpochMillis = 10L
        )
        val discrepancy = EvidenceRecord(
            id = "transmission_timestamp_discrepancy",
            title = "Perbedaan Waktu Penerimaan Transmisi",
            summary = "Dua sistem internal mencatat referensi RX-14 pada waktu berbeda empat detik. Perbedaan teramati, tetapi penyebabnya belum diketahui.",
            source = "A.E.G.I.S. / perbandingan metadata tanda terima",
            missionId = "first_contact.recruitment",
            recordedAtEpochMillis = 20L
        )

        val all = ArchiveQueryPolicy.evidence(
            listOf(missionRecord, discrepancy), "", GameLanguage.ENGLISH
        )
        assertEquals(listOf(discrepancy.id, missionRecord.id), all.map { it.id })

        val rx14 = ArchiveQueryPolicy.evidence(
            listOf(missionRecord, discrepancy), "RX-14", GameLanguage.ENGLISH
        )
        assertEquals(listOf(discrepancy.id), rx14.map { it.id })

        val evaluator = ArchiveQueryPolicy.evidence(
            listOf(missionRecord, discrepancy), "mission evaluator", GameLanguage.ENGLISH
        )
        assertEquals(listOf(missionRecord.id), evaluator.map { it.id })
        assertEquals("A.E.G.I.S. / Python evaluator 9", missionRecord.source)
    }
}
