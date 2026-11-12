package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Campaign01StoryTest {
    @Test
    fun firstContactStartsAfterTheMessengerRecruitmentInvitation() {
        assertEquals(14, Campaign01Story.beats.size)
        assertEquals("ORIENTASI A.E.G.I.S.", Campaign01Story.beats.first().title)
        assertEquals("LAPORAN INVESTIGASI", Campaign01Story.beats.last().title)
        assertFalse(Campaign01Story.beats.any { it.title.contains("PENGIRIM", ignoreCase = true) })
        assertFalse(Campaign01Story.beats[2].body.contains("Freya", ignoreCase = true))
        assertFalse(Campaign01Story.beats[4].body.contains("perintah perekrutan", ignoreCase = true))
        assertTrue(Campaign01Story.beats[5].body.contains("dua catatan penerimaan"))
        assertTrue(Campaign01Story.beats.last().body.contains("belum membuktikan manipulasi"))
    }

    @Test
    fun englishAndIndonesianStoryVariantsHaveExactParityWithoutChangingMissionContracts() {
        assertEquals(Campaign01Story.beats.size, Campaign01StoryLocalization.englishBeats.size)
        Campaign01Story.beats.indices.forEach { index ->
            val indonesian = Campaign01Story.localizedBeat(index, com.zeroday.cyberprotocol.core.model.GameLanguage.INDONESIAN)
            val english = Campaign01Story.localizedBeat(index, com.zeroday.cyberprotocol.core.model.GameLanguage.ENGLISH)
            assertEquals(Campaign01Story.beats[index], indonesian)
            assertEquals(Campaign01StoryLocalization.englishBeats[index], english)
            assertTrue(english.title.isNotBlank())
            assertTrue(english.body.isNotBlank())
            assertTrue(english.button.isNotBlank())
        }
        assertEquals("A.E.G.I.S. ORIENTATION", Campaign01StoryLocalization.englishBeats.first().title)
        assertEquals("INVESTIGATION REPORT", Campaign01StoryLocalization.englishBeats.last().title)
    }

    @Test
    fun missionStarterDraftsContainObjectivesButNoExecutableSolutions() {
        val stages = listOf(4, 5, 8, 9, 11, 12, 13, 14)
        stages.forEachIndexed { index, stage ->
            val draft = Campaign01Story.codeTemplateForStage(stage)
            assertEquals(Campaign01Story.codeTemplates[index], draft)
            assertTrue(draft.isNotBlank())
            assertTrue(draft.lineSequence().filter { it.isNotBlank() }.all { it.trimStart().startsWith("#") })
        }
    }

    @Test
    fun oldBundledAnswersAreClearedButPlayerDraftsArePreserved() {
        assertTrue(Campaign01Story.draftForStage(4, "signal = \"ACCESS GRANTED\"\nprint(signal)")
            .contains("TODO"))
        assertEquals("my_signal = \"custom\"", Campaign01Story.draftForStage(4, "my_signal = \"custom\""))
        assertEquals(Campaign01Story.codeTemplateForStage(4), Campaign01Story.draftForStage(4, null))
    }

    @Test
    fun unapprovedCaseTitleIsNotLockedIntoOpeningCanon() {
        assertFalse(Campaign01Story.beats.any { it.body.contains("The Missing Ledger", ignoreCase = true) })
    }
}
