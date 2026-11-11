package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Campaign01StoryTest {
    @Test
    fun firstContactStartsAfterTheMessengerRecruitmentInvitation() {
        assertEquals(12, Campaign01Story.beats.size)
        assertEquals("ORIENTASI A.E.G.I.S.", Campaign01Story.beats.first().title)
        assertEquals("LAPORAN INVESTIGASI", Campaign01Story.beats.last().title)
        assertFalse(Campaign01Story.beats.any { it.title.contains("PENGIRIM", ignoreCase = true) })
        assertTrue(Campaign01Story.beats[5].body.contains("dua catatan penerimaan"))
        assertTrue(Campaign01Story.beats.last().body.contains("belum membuktikan manipulasi"))
    }

    @Test
    fun sixTechnicalMissionTemplatesAreMappedToTheirStoryStages() {
        assertEquals(
            "signal = \"ACCESS GRANTED\"\nprint(signal)",
            Campaign01Story.codeTemplates[0]
        )
        assertEquals(
            "message = \"IDENTITY VERIFIED\"\nprint(message)",
            Campaign01Story.codeTemplates[1]
        )
        assertEquals(Campaign01Story.codeTemplates[2], Campaign01Story.codeTemplateForStage(8))
        assertEquals(Campaign01Story.codeTemplates[3], Campaign01Story.codeTemplateForStage(9))
        assertEquals(Campaign01Story.codeTemplates[4], Campaign01Story.codeTemplateForStage(11))
        assertEquals(Campaign01Story.codeTemplates[5], Campaign01Story.codeTemplateForStage(12))
    }

    @Test
    fun unapprovedCaseTitleIsNotLockedIntoOpeningCanon() {
        assertFalse(Campaign01Story.beats.any { it.body.contains("The Missing Ledger", ignoreCase = true) })
    }
}
