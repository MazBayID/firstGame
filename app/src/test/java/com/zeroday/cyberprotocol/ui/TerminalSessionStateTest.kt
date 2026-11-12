package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    fun clearOutputUsesTheSelectedLanguageAndPreservesSession() {
        val original = TerminalSessionState(
            command = "ls",
            currentDirectory = "/A.E.G.I.S/personal",
            output = "old output"
        )
        val english = original.clearOutput("A.E.G.I.S. VIRTUAL TERMINAL  ·  OUTPUT CLEARED\n")
        val indonesian = original.clearOutput("A.E.G.I.S. VIRTUAL TERMINAL  ·  KELUARAN DIBERSIHKAN\n")

        assertEquals("A.E.G.I.S. VIRTUAL TERMINAL  ·  OUTPUT CLEARED\n", english.output)
        assertEquals("A.E.G.I.S. VIRTUAL TERMINAL  ·  KELUARAN DIBERSIHKAN\n", indonesian.output)
        assertEquals("ls", english.command)
        assertEquals("/A.E.G.I.S/personal", english.currentDirectory)
    }


    @Test
    fun initialTerminalBannerMatchesSelectedLanguage() {
        assertTrue(TerminalSessionState.initialOutput(
            com.zeroday.cyberprotocol.core.model.GameLanguage.ENGLISH
        ).contains("Game environment is ready."))
        assertTrue(TerminalSessionState.initialOutput(
            com.zeroday.cyberprotocol.core.model.GameLanguage.INDONESIAN
        ).contains("Lingkungan game siap digunakan."))
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
