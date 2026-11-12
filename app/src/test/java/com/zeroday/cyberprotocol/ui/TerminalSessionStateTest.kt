package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
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
    fun restoringSessionPreservesWorkspaceAndOutputButClearsTransientBusyFlag() {
        val original = TerminalSessionState(
            command = "python main.py",
            currentDirectory = "/A.E.G.I.S/missions/first_contact",
            output = "ready",
            busy = true
        )
        val saverScope = object : androidx.compose.runtime.saveable.SaverScope {
            override fun canBeSaved(value: Any): Boolean = true
        }

        val saved = with(TerminalSessionState.Saver) { with(saverScope) { save(original) } }
        val restored = TerminalSessionState.Saver.restore(saved!!)

        assertEquals("python main.py", restored?.command)
        assertEquals("/A.E.G.I.S/missions/first_contact", restored?.currentDirectory)
        assertEquals("ready", restored?.output)
        assertEquals(false, restored?.busy)
    }
}
