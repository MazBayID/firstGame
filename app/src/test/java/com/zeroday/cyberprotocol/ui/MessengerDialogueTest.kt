package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MessengerDialogueTest {
    @Test
    fun freyaPreservesRecruitmentMysteryWhenAskedWhySheChoseThePlayer() {
        val response = MessengerDialogue.freyaResponse("Mengapa kamu memilihku?")

        assertTrue(response.contains("belum bisa kubagikan"))
        assertFalse(response.contains("aku yang memberikan perintah"))
        assertFalse(response.contains("aku yang merekrutmu"))
    }

    @Test
    fun freyaGuidesThePlayerWithoutShamingBeginnerQuestions() {
        val response = MessengerDialogue.freyaResponse("Masih banyak yang harus kupelajari.")

        assertTrue(response.contains("berpura-pura sudah paham"))
        assertTrue(response.contains("membangun kemampuanmu"))
    }

    @Test
    fun freyaExplainsTheInGameTerminalIsNotTheAndroidShell() {
        val response = MessengerDialogue.freyaResponse("Apa fungsi Terminal?")

        assertTrue(response.contains("bukan shell Android"))
        assertTrue(response.contains("help"))
        assertTrue(response.contains("cat"))
    }

    @Test
    fun freyaExplainsHowToReadTheFirstMissionFile() {
        val response = MessengerDialogue.freyaResponse("Bagaimana membaca berkas misi?")

        assertTrue(response.contains("cat message_fragment.txt"))
        assertTrue(response.contains("tidak menjalankan kode Python"))
    }

    @Test
    fun freyaExplainsPythonVariablesAndStringQuotes() {
        val response = MessengerDialogue.freyaResponse("Apa itu variabel Python?")

        assertTrue(response.contains("signal ="))
        assertTrue(response.contains("print(signal)"))
        assertTrue(response.contains("tanda kutip"))
    }

    @Test
    fun freyaExplainsTerminalRunAndBrowserSubmissionSeparation() {
        val response = MessengerDialogue.freyaResponse("Bagaimana menjalankan dan mengirim kode?")

        assertTrue(response.contains("python signal.py"))
        assertTrue(response.contains("Browser > Mission Control"))
        assertTrue(response.contains("kodenya tidak berubah"))
    }

    @Test
    fun nexusReportsCurrentLocalProgressWithoutInventingAnActiveMission() {
        val response = MessengerDialogue.nexusResponse(
            "Tampilkan status personel.",
            activeMissionTitle = null,
            completedMissionCount = 3
        )

        assertTrue(response.contains("Misi terverifikasi: 3"))
        assertTrue(response.contains("Misi aktif: tidak ada"))
    }

    @Test
    fun nexusUsesTheCurrentMissionTitleWhenAvailable() {
        val response = MessengerDialogue.nexusResponse(
            "Tampilkan status personel.",
            activeMissionTitle = "Normalisasi Waktu",
            completedMissionCount = 6
        )

        assertTrue(response.contains("Normalisasi Waktu"))
        assertTrue(response.contains("Misi terverifikasi: 6"))
    }

    @Test
    fun nexusOfflineExplanationMatchesOfflineFirstCampaignContract() {
        val response = MessengerDialogue.nexusResponse(
            "Apakah sistem ini berjalan offline?",
            activeMissionTitle = null,
            completedMissionCount = 0
        )

        assertTrue(response.contains("runtime Python lokal"))
        assertTrue(response.contains("tanpa layanan AI atau server eksternal"))
    }
}
