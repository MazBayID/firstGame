package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Campaign01StoryTest {
    @Test
    fun firstContactContainsSevenApprovedStoryBeats() {
        assertEquals(7, Campaign01Story.beats.size)
        assertEquals("PENGIRIM TAK DIKENAL", Campaign01Story.beats.first().title)
        assertEquals("REKRUTMEN RESMI", Campaign01Story.beats.last().title)
        assertTrue(Campaign01Story.beats.last().body.contains("waktu penerimaan pesan yang berbeda"))
    }

    @Test
    fun openingCodeTemplatesMatchTheTwoTeachingEvaluators() {
        assertEquals(
            "signal = \"ACCESS GRANTED\"\nprint(signal)",
            Campaign01Story.codeTemplates[0]
        )
        assertEquals(
            "message = \"IDENTITY VERIFIED\"\nprint(message)",
            Campaign01Story.codeTemplates[1]
        )
    }

    @Test
    fun unapprovedCaseTitleIsNotLockedIntoOpeningCanon() {
        assertFalse(Campaign01Story.beats.any { it.body.contains("The Missing Ledger", ignoreCase = true) })
    }
}
