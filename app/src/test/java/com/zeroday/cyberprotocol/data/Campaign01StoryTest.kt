package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Campaign01StoryTest {
    @Test
    fun firstContactStartsAfterTheMessengerRecruitmentInvitation() {
        assertEquals(9, Campaign01Story.beats.size)
        assertEquals("ORIENTASI A.E.G.I.S.", Campaign01Story.beats.first().title)
        assertEquals("LAPORAN AWAL", Campaign01Story.beats.last().title)
        assertFalse(Campaign01Story.beats.any { it.title.contains("PENGIRIM", ignoreCase = true) })
        assertTrue(Campaign01Story.beats[5].body.contains("dua catatan penerimaan"))
        assertTrue(Campaign01Story.beats.last().body.contains("batas temuan"))
    }

    @Test
    fun fourTechnicalMissionTemplatesAreMappedToTheirStoryStages() {
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
    }

    @Test
    fun unapprovedCaseTitleIsNotLockedIntoOpeningCanon() {
        assertFalse(Campaign01Story.beats.any { it.body.contains("The Missing Ledger", ignoreCase = true) })
    }
}
