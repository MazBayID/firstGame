package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import com.zeroday.cyberprotocol.core.model.GameLanguage
import org.junit.Test

class MessengerDialogueTest {

    @Test
    fun freyaMissionAttachmentsUseStableMissionIdsIndependentOfDisplayLanguage() {
        assertEquals("message_fragment.txt", MessengerDialogue.missionAttachment("first_contact.signal"))
        assertEquals("identity_fragment.txt", MessengerDialogue.missionAttachment("first_contact.identity"))
        assertNull(MessengerDialogue.missionAttachment(null))
        assertNull(MessengerDialogue.missionAttachment("unknown.mission"))
    }

    @Test
    fun freyaProvidesEnglishResponsesWithoutChangingCanonicalReplyKeys() {
        val response = MessengerDialogue.freyaResponse(
            "Apa fungsi Terminal?",
            GameLanguage.ENGLISH
        )
        assertTrue(response.contains("not your Android shell"))
        assertTrue(response.contains("allowed commands"))
        assertFalse(response.contains("bukan shell Android"))
    }

    @Test
    fun englishFreyaRepliesPreserveBeginnerFriendlyGuidanceAndRecruitmentMystery() {
        val beginner = MessengerDialogue.freyaResponse(
            "Masih banyak yang harus kupelajari.",
            GameLanguage.ENGLISH
        )
        val mystery = MessengerDialogue.freyaResponse(
            "Mengapa kamu memilihku?",
            GameLanguage.ENGLISH
        )
        assertTrue(beginner.contains("pretended to understand"))
        assertTrue(mystery.contains("cannot share through this channel"))
        assertFalse(mystery.contains("I gave the recruitment order"))
    }

    @Test
    fun nexusProvidesEnglishStatusAndOfflineExplanations() {
        val status = MessengerDialogue.nexusResponse(
            "Tampilkan status personel.", null, 2, GameLanguage.ENGLISH
        )
        val offline = MessengerDialogue.nexusResponse(
            "Apakah sistem ini berjalan offline?", null, 0, GameLanguage.ENGLISH
        )
        assertTrue(status.contains("Verified missions: 2"))
        assertTrue(status.contains("Active mission: none"))
        assertTrue(offline.contains("local Python runtime"))
    }


    @Test
    fun freyaOpeningDialogueIsLocalizedAndReflectsCurrentProgress() {
        val english = MessengerDialogue.freyaIntroduction("Identity Verification", 2, GameLanguage.ENGLISH)
        val indonesian = MessengerDialogue.freyaIntroduction("Verifikasi Identitas", 2, GameLanguage.INDONESIAN)

        assertTrue(english.first().startsWith("Hello. I am Freya Knox"))
        assertTrue(english.any { it.contains("Identity Verification") })
        assertTrue(english.any { it.contains("identity_fragment.txt") })
        assertTrue(english.any { it.contains("2 verified missions") })
        assertTrue(indonesian.first().startsWith("Halo. Aku Freya Knox"))
        assertTrue(indonesian.any { it.contains("identity_fragment.txt") })

        val englishTitleWithIndonesianLanguage = MessengerDialogue.freyaIntroduction(
            "Identity Verification", 2, GameLanguage.INDONESIAN
        )
        val indonesianTitleWithEnglishLanguage = MessengerDialogue.freyaIntroduction(
            "Verifikasi Identitas", 2, GameLanguage.ENGLISH
        )
        assertTrue(englishTitleWithIndonesianLanguage.any { it.contains("identity_fragment.txt") })
        assertTrue(indonesianTitleWithEnglishLanguage.any { it.contains("identity_fragment.txt") })
    }

    @Test
    fun nexusOpeningDialogueUsesTheSelectedLanguage() {
        assertTrue(MessengerDialogue.nexusIntroduction(GameLanguage.ENGLISH).first().startsWith("The A.E.G.I.S."))
        assertTrue(MessengerDialogue.nexusIntroduction(GameLanguage.INDONESIAN).first().startsWith("Kanal komunikasi"))
    }

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
