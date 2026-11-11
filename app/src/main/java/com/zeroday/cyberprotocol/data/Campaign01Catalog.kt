package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.MissionDefinition
import com.zeroday.cyberprotocol.core.model.MissionHint
import com.zeroday.cyberprotocol.core.model.MissionKind
import com.zeroday.cyberprotocol.core.model.MissionObjective

/**
 * Approved opening chapter content only. Later case concepts remain outside
 * this catalog until their story details are approved.
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
            briefing = "NEXUS menemukan satu fragmen pesan pada kanal latihan. Simpan fragmen itu ke variabel signal, lalu tampilkan nilainya agar sistem dapat memverifikasi respons awalmu.",
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
            briefing = "Sinyal pertama membuka pesan verifikasi. Gunakan kembali konsep variabel dan output, tetapi simpan pesan kedua pada variabel message.",
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
        )
    )

    private val byId = missions.associateBy { it.id }

    fun find(missionId: String): MissionDefinition? = byId[missionId]
}
