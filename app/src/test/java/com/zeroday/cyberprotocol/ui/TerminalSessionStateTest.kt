package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class TerminalSessionStateTest {
    @Test
    fun appendingOutputKeepsOnlyTheMostRecentTwentyFourThousandCharacters() {
        val state = TerminalSessionState(currentDirectory = "/A.E.G.I.S/personal")
            .appendOutput("x".repeat(25_000))

        assertEquals(24_000, state.output.length)
        assertEquals("x".repeat(24_000), state.output)
    }

    @Test
    fun clearOutputReplacesOldTerminalHistoryWithClearMarker() {
        val state = TerminalSessionState(
            command = "ls",
            currentDirectory = "/A.E.G.I.S/personal",
            output = "old output"
        ).clearOutput()

        assertEquals("A.E.G.I.S. VIRTUAL TERMINAL  ·  KELUARAN DIBERSIHKAN\n", state.output)
        assertEquals("ls", state.command)
        assertEquals("/A.E.G.I.S/personal", state.currentDirectory)
    }

    @Test
    fun changingDirectoryDoesNotDiscardCommandOrOutput() {
        val state = TerminalSessionState(
            command = "cat message_fragment.txt",
            currentDirectory = "/A.E.G.I.S/personal",
            output = "ACCESS GRANTED\n"
        ).copy(currentDirectory = "/A.E.G.I.S/missions/first_contact")

        assertEquals("/A.E.G.I.S/missions/first_contact", state.currentDirectory)
        assertEquals("cat message_fragment.txt", state.command)
        assertEquals("ACCESS GRANTED\n", state.output)
    }

    @Test
    fun restoredTerminalSessionDoesNotRemainBusy() {
        // The Saver deliberately excludes transient execution state.
        val state = TerminalSessionState(
            command = "python main.py",
            currentDirectory = "/A.E.G.I.S/personal",
            output = "ready",
            busy = false
        )

        assertFalse(state.busy)
        assertEquals("python main.py", state.command)
    }
}
