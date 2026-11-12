package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.CaseDecisionOption
import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.CaseAnalysisOption
import com.zeroday.cyberprotocol.core.model.MissionArchetype

/** Authored case contracts for the guided intelligence campaign. */
object Campaign01CaseCatalog {
    const val RX14_CASE_ID = "first_contact.rx14"
    const val RX14_COMPLETION_REWARD_XP = 100

    val rx14 = CaseDefinition(
        id = RX14_CASE_ID,
        campaignId = Campaign01Catalog.CAMPAIGN_ID,
        title = "RX-14 — Selisih Waktu Penerimaan",
        briefing = "A.E.G.I.S. menemukan dua catatan penerimaan untuk satu transmisi latihan dengan referensi RX-14. Isi dan referensinya cocok, tetapi waktu yang tercatat berbeda empat detik. Tugasmu adalah memeriksa kedua sumber, membandingkan temuan, lalu menentukan langkah yang proporsional. Jangan menganggap selisih waktu sebagai bukti manipulasi.",
        primaryArchetype = MissionArchetype.INVESTIGATION,
        requiredEvidenceIds = EvidenceAssetCatalog.assets.map { it.evidenceId }.toSet(),
        analysisOptions = listOf(
            CaseAnalysisOption(
                id = "observed_discrepancy_only",
                title = "Selisih empat detik teramati; penyebab belum terbukti",
                description = "Kedua sumber menyebut RX-14 dengan waktu 09:41:12 dan 09:41:16. Data membuktikan selisih, tetapi belum mengidentifikasi penyebab.",
                supportedByEvidence = true,
                feedback = "KESIMPULAN DIDUKUNG. Selisih empat detik adalah fakta yang teramati; penyebabnya tetap belum diketahui."
            ),
            CaseAnalysisOption(
                id = "tampering_confirmed",
                title = "Manipulasi waktu telah terbukti",
                description = "Anggap perbedaan waktu sebagai bukti bahwa salah satu sistem mengubah catatan.",
                supportedByEvidence = false,
                feedback = "KESIMPULAN BELUM DIDUKUNG. Perbedaan waktu tidak membuktikan manipulasi atau menunjukkan siapa yang mengubah data."
            ),
            CaseAnalysisOption(
                id = "clock_sync_cause",
                title = "Sinkronisasi jam adalah penyebabnya",
                description = "Tetapkan perbedaan sinkronisasi jam sebagai penyebab pasti.",
                supportedByEvidence = false,
                feedback = "KESIMPULAN BELUM DIDUKUNG. Sinkronisasi jam adalah kemungkinan yang perlu diuji dengan log tambahan, bukan penyebab yang sudah terbukti."
            )
        ),
        decisionOptions = listOf(
            CaseDecisionOption("record_discrepancy", "Catat anomali tanpa menetapkan penyebab",
                "Laporkan kedua waktu dan selisih empat detik sebagai fakta. Tandai penyebabnya belum diketahui."),
            CaseDecisionOption("request_clock_sync_logs", "Minta log sinkronisasi jam",
                "Periksa apakah kedua sistem menggunakan sumber waktu yang tersinkronisasi sebelum menarik kesimpulan."),
            CaseDecisionOption("request_processing_queue_logs", "Minta log antrean pemrosesan",
                "Periksa kemungkinan perbedaan waktu akibat antrean atau proses penerimaan pada kedua sistem."),
            CaseDecisionOption("insufficient_evidence", "Nyatakan bukti belum cukup",
                "Tahan kesimpulan kausal. Dua catatan menunjukkan selisih, tetapi belum menjelaskan mengapa selisih itu terjadi.")
        )
    )
}
