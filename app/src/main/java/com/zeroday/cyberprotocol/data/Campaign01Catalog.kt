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
            nextClue = "Fragmen berikutnya memakai nama variabel message. Perhatikan nama yang diminta sebelum menampilkan nilainya.",
            briefing = "A.E.G.I.S. menyediakan fragmen pesan di File Repository intranet. Unduh message_fragment.txt, baca isinya melalui Terminal dengan cat, simpan teks tersebut ke variabel signal, lalu tampilkan nilainya.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 9,
            rewardXp = 25,
            requiredClearance = 0,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/signal",
            allowedFiles = setOf("briefing.txt", "message_fragment.txt", "signal.py"),
            writableFiles = setOf("signal.py"),
            initialFiles = mapOf("message_fragment.txt" to "ACCESS GRANTED\n"),
            objectives = listOf(
                MissionObjective(
                    id = "assign_signal",
                    title = "Simpan sinyal",
                    description = "Baca message_fragment.txt dari ruang kerja, lalu simpan teksnya ke variabel signal."
                ),
                MissionObjective(
                    id = "display_signal",
                    title = "Tampilkan sinyal",
                    description = "Gunakan print() untuk menampilkan isi variabel signal."
                )
            ),
            hints = listOf(
                MissionHint(1, "Gunakan cat message_fragment.txt di Terminal untuk membaca fragmen yang diunduh."),
                MissionHint(2, "Simpan isi fragmen ke variabel bernama signal dengan pola signal = \"teks\"."),
                MissionHint(3, "Tampilkan variabel tersebut dengan print(signal).")
            )
        ),
        MissionDefinition(
            id = "first_contact.identity",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Verifikasi Identitas",
            nextClue = "Dua sistem latihan menyimpan tanda terima untuk satu transmisi. Perhatikan sumber dan waktu pencatatannya.",
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
            nextClue = "Log bukan satu-satunya sumber. Sebuah inventaris terpisah menyimpan daftar referensi rekaman.",
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
            nextClue = "Referensi RX-14 muncul pada lebih dari satu catatan. Telusuri daftar tanpa menghilangkan posisi setiap item.",
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
        ),
        MissionDefinition(
            id = "first_contact.trace",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Penelusuran Referensi",
            nextClue = "Indeks 0 dan 1 menunjuk dua catatan yang perlu dibaca berdampingan.",
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
            nextClue = "Selisih empat detik telah teramati. Waktu saja belum menjelaskan penyebabnya; tulis batas kesimpulanmu.",
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
        MissionDefinition(
            id = "first_contact.normalize",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Normalisasi Waktu",
            nextClue = "Nilai bertanda sudah menunjukkan arah selisih. Tahap terakhir akan menghitung besar selisih tanpa tanda.",
            briefing = "Dua waktu penerimaan disediakan sebagai string angka detik sejak tengah malam. Gunakan int() untuk mengubah keduanya menjadi integer, simpan pada first_time dan second_time, lalu hitung delta = second_time - first_time. Pertahankan urutan sumber; nilai negatif tetap merupakan hasil yang sah.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 14,
            rewardXp = 65,
            requiredClearance = 1,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/normalize",
            allowedFiles = setOf("briefing.txt", "normalize.py"),
            writableFiles = setOf("normalize.py"),
            runtimeVariables = mapOf("receipt_seconds" to listOf("34872", "34876")),
            objectives = listOf(
                MissionObjective("convert_first", "Konversi waktu pertama", "Gunakan int() untuk mengubah receipt_seconds[0] menjadi first_time."),
                MissionObjective("convert_second", "Konversi waktu kedua", "Ubah receipt_seconds[1] menjadi second_time."),
                MissionObjective("calculate_delta", "Hitung selisih bertanda", "Kurangi second_time dengan first_time dan simpan pada delta.")
            ),
            hints = listOf(
                MissionHint(1, "Nilai dari input masih berupa teks; int() mengubah teks angka menjadi integer."),
                MissionHint(2, "Ambil dua item dengan receipt_seconds[0] dan receipt_seconds[1]."),
                MissionHint(3, "Hitung delta = second_time - first_time, lalu print(delta).")
            )
        ),
        MissionDefinition(
            id = "first_contact.validate_delta",
            campaignId = CAMPAIGN_ID,
            chapterId = CHAPTER_ID,
            title = "Validasi Selisih",
            nextClue = "Laporan akhir First Contact siap disusun. Catat angka yang teramati, sumbernya, dan hal-hal yang masih belum dapat disimpulkan.",
            briefing = "Hitung besar selisih dua waktu dalam receipt_seconds. Konversikan nilai teks menjadi integer, kurangi satu dengan yang lain, lalu gunakan abs() agar hasil difference selalu non-negatif. Nilai ini adalah ukuran jarak waktu, bukan penjelasan penyebab.",
            kind = MissionKind.STORY_CODE,
            evaluatorId = 15,
            rewardXp = 70,
            requiredClearance = 1,
            workspaceDirectory = "/A.E.G.I.S/missions/first_contact/validate_delta",
            allowedFiles = setOf("briefing.txt", "validate_delta.py"),
            writableFiles = setOf("validate_delta.py"),
            runtimeVariables = mapOf("receipt_seconds" to listOf("34872", "34876")),
            objectives = listOf(
                MissionObjective("absolute_delta", "Hitung besar selisih", "Gunakan abs() pada hasil pengurangan dua waktu yang sudah dikonversi dengan int()."),
                MissionObjective("display_delta", "Tampilkan hasil", "Simpan hasil pada difference dan tampilkan dengan print().")
            ),
            hints = listOf(
                MissionHint(1, "abs(nilai) mengembalikan besar nilai tanpa tanda negatif."),
                MissionHint(2, "Gunakan int() pada kedua item sebelum melakukan pengurangan."),
                MissionHint(3, "Simpan bentuk abs(int(receipt_seconds[1]) - int(receipt_seconds[0])) pada difference.")
            )
        ),
    )

    private val byId = missions.associateBy { it.id }

    /** Only the two beginner onboarding exercises remain mandatory story missions. */
    val guidedTrainingMissionIds = setOf("first_contact.signal", "first_contact.identity")

    /** Retained for old saves and historical records; these are no longer story progression gates. */
    val retiredLegacyStoryMissionIds = setOf(
        "first_contact.audit", "first_contact.inventory", "first_contact.trace",
        "first_contact.compare", "first_contact.normalize", "first_contact.validate_delta"
    )

    private val storyStageByMissionId = mapOf(
        "first_contact.signal" to 4,
        "first_contact.identity" to 5
    )

    fun find(missionId: String): MissionDefinition? = byId[missionId]

    /** Returns the authored story checkpoint that hosts a mission, if any. */
    fun storyStageForMission(missionId: String): Int? = storyStageByMissionId[missionId]
}
