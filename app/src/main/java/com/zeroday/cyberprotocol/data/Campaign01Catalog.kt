package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.MissionDefinition
import com.zeroday.cyberprotocol.core.model.MissionHint
import com.zeroday.cyberprotocol.core.model.MissionKind
import com.zeroday.cyberprotocol.core.model.MissionObjective

/**
 * Approved First Contact and follow-up investigation missions.
 */
object Campaign01Catalog {
    const val CAMPAIGN_ID = "campaign_01_recruitment"
    const val CHAPTER_ID = "chapter_001_first_contact"

    val missions: List<MissionDefinition> = listOf(
        MissionDefinition(
            id = "first_contact.signal",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Sinyal Pertama",
            briefing = "Freya memberikan satu fragmen pesan pada kanal latihan. Simpan fragmen itu ke variabel signal, lalu tampilkan nilainya agar A.E.G.I.S. dapat memverifikasi respons awalmu.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 9,
            rewardXp = 25,
            requiredClearance = 0,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/signal",
            allowedFiles = setOf("briefing.txt", "signal.py"),
            writableFiles = setOf("signal.py"),
            objectives = listOf(
                MissionObjective(
                    id = "assign_signal",
                    title = "Simpan sinyal",
                    description = "Buat variabel bernama signal dan isi dengan pesan yang diberikan."
                ),
                MissionObjective(
                    id = "display_signal",
                    title = "Tampilkan sinyal",
                    description = "Gunakan print() untuk menampilkan isi variabel signal."
                )
            ),
            hints = listOf(
                MissionHint(1, "Python menyimpan nilai dengan pola nama_variabel = nilai."),
                MissionHint(2, "Nama variabel yang diminta adalah signal."),
                MissionHint(3, "Setelah assignment, panggil print(signal).")
            )
        ),
        MissionDefinition(
            id = "first_contact.identity",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Verifikasi Identitas",
            briefing = "Sinyal pertama membuka pesan verifikasi. Gunakan kembali konsep variabel dan output, tetapi simpan pesan kedua pada variabel message. Freya akan memeriksa hasilnya.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 10,
            rewardXp = 35,
            requiredClearance = 0,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/identity",
            allowedFiles = setOf("briefing.txt", "identity.py"),
            writableFiles = setOf("identity.py"),
            objectives = listOf(
                MissionObjective(
                    id = "assign_message",
                    title = "Simpan pesan verifikasi",
                    description = "Buat variabel bernama message untuk menyimpan pesan identitas."
                ),
                MissionObjective(
                    id = "display_message",
                    title = "Tampilkan pesan",
                    description = "Gunakan print() untuk menampilkan isi variabel message."
                )
            ),
            hints = listOf(
                MissionHint(1, "Struktur dasarnya sama dengan tugas sebelumnya."),
                MissionHint(2, "Gunakan nama variabel message, bukan signal."),
                MissionHint(3, "Tampilkan variabel dengan print(message).")
            )
        ),
        MissionDefinition(
            id = "first_contact.audit",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Audit Jejak Sistem",
            briefing = "Periksa log latihan yang disediakan A.E.G.I.S. Hitung hanya baris yang memuat ERROR. Gunakan for dan if; jangan mengubah sumber data.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 8,
            rewardXp = 45,
            requiredClearance = 0,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/audit",
            allowedFiles = setOf("briefing.txt", "audit.py"),
            writableFiles = setOf("audit.py"),
            runtimeVariables = mapOf("events" to listOf(
                "INFO: training channel initialized",
                "ERROR: receipt checksum mismatch",
                "WARNING: retry scheduled",
                "ERROR: timestamp offset detected",
                "INFO: archive write completed"
            )),
            objectives = listOf(
                MissionObjective("scan_events", "Periksa setiap entri", "Gunakan for untuk memeriksa semua item dalam events."),
                MissionObjective("count_errors", "Hitung entri ERROR", "Gunakan if untuk menghitung item yang memuat teks ERROR.")
            ),
            hints = listOf(
                MissionHint(1, "Mulai dengan count = 0 sebelum perulangan."),
                MissionHint(2, "Di dalam for, periksa apakah \"ERROR\" ada di dalam event."),
                MissionHint(3, "Jika kondisi benar, tambahkan count dengan count += 1, lalu print(count).")
            )
        ),
        MissionDefinition(
            id = "first_contact.inventory",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Inventaris Rekaman",
            briefing = "Sumber kedua menyediakan list bernama records. Hitung jumlah referensi yang tersedia menggunakan len(), simpan pada record_count, lalu tampilkan.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 11,
            rewardXp = 50,
            requiredClearance = 0,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/inventory",
            allowedFiles = setOf("briefing.txt", "inventory.py"),
            writableFiles = setOf("inventory.py"),
            runtimeVariables = mapOf("records" to listOf("RX-14", "RX-19", "RX-21")),
            objectives = listOf(
                MissionObjective("count_records", "Hitung referensi", "Gunakan len(records) untuk memperoleh jumlah item."),
                MissionObjective("display_count", "Tampilkan jumlah", "Simpan hasil pada record_count dan tampilkan dengan print().")
            ),
            hints = listOf(
                MissionHint(1, "len() menerima list dan mengembalikan jumlah item."),
                MissionHint(2, "Gunakan record_count = len(records)."),
                MissionHint(3, "Tampilkan hasilnya dengan print(record_count).")
            )
        )
        MissionDefinition(
            id = "first_contact.trace",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Penelusuran Referensi",
            briefing = "Gunakan enumerate() untuk menemukan baris yang memuat RX-14. Tampilkan indeks dan isi baris agar setiap temuan tetap memiliki posisi sumber.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 12,
            rewardXp = 55,
            requiredClearance = 1,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/trace",
            allowedFiles = setOf("briefing.txt", "trace.py"),
            writableFiles = setOf("trace.py"),
            runtimeVariables = mapOf("receipts" to listOf(
                "A.E.G.I.S. | RX-14 | 09:41:12",
                "ARCHIVE | RX-14 | 09:41:16"
            )),
            objectives = listOf(
                MissionObjective("enumerate_receipts", "Telusuri daftar", "Gunakan enumerate(receipts) untuk memperoleh indeks dan isi setiap item."),
                MissionObjective("filter_reference", "Pilih referensi", "Gunakan if untuk hanya menampilkan baris yang memuat RX-14.")
            ),
            hints = listOf(
                MissionHint(1, "Bentuk dasarnya: for index, receipt in enumerate(receipts):"),
                MissionHint(2, "Di dalam perulangan, periksa apakah \"RX-14\" ada di dalam receipt."),
                MissionHint(3, "Gunakan print(index, receipt) saat kondisi benar.")
            )
        ),
        MissionDefinition(
            id = "first_contact.compare",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Perbandingan Dua Sumber",
            briefing = "Ambil catatan pertama dan kedua dari list receipts dengan indexing. Simpan pada primary dan archive, lalu tampilkan keduanya tanpa mengubah sumber.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 13,
            rewardXp = 60,
            requiredClearance = 1,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/compare",
            allowedFiles = setOf("briefing.txt", "compare.py"),
            writableFiles = setOf("compare.py"),
            runtimeVariables = mapOf("receipts" to listOf(
                "A.E.G.I.S. | RX-14 | 09:41:12",
                "ARCHIVE | RX-14 | 09:41:16"
            )),
            objectives = listOf(
                MissionObjective("read_primary", "Ambil catatan utama", "Gunakan receipts[0] dan simpan pada variabel primary."),
                MissionObjective("read_archive", "Ambil catatan arsip", "Gunakan receipts[1] dan simpan pada variabel archive."),
                MissionObjective("display_sources", "Tampilkan kedua sumber", "Tampilkan primary dan archive secara berurutan.")
            ),
            hints = listOf(
                MissionHint(1, "Index list dimulai dari 0, jadi item pertama adalah receipts[0]."),
                MissionHint(2, "Gunakan receipts[1] untuk item kedua."),
                MissionHint(3, "Simpan masing-masing item sebelum memanggil print().")
            )
        ),
    )

    private val byId = missions.associateBy { it.id }

    fun find(missionId: String): MissionDefinition? = byId[missionId]
}
