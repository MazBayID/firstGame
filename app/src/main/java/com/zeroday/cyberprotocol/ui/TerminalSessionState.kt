package com.zeroday.cyberprotocol.ui

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

/**
 * Serializable UI state for the in-game terminal.
 *
 * The terminal keeps its current directory, pending command, and bounded output
 * together so navigation and Activity recreation cannot desynchronize them.
 * A command that was running during process recreation is restored as idle.
 */
internal data class TerminalSessionState(
    val command: String = "",
    val currentDirectory: String,
    val output: String = INITIAL_OUTPUT,
    val busy: Boolean = false
) {
    fun appendOutput(value: String): TerminalSessionState =
        copy(output = (output + value).takeLast(MAX_OUTPUT_CHARACTERS))

    fun clearOutput(message: String): TerminalSessionState =
        copy(output = message)

    companion object {
        private const val MAX_OUTPUT_CHARACTERS = 24_000
        fun initialOutput(language: com.zeroday.cyberprotocol.core.model.GameLanguage): String =
            if (language == com.zeroday.cyberprotocol.core.model.GameLanguage.ENGLISH) {
                "A.E.G.I.S. VIRTUAL TERMINAL  ·  BUILD 0.1\\n" +
                    "Game environment is ready.\\n" +
                    "Type 'help' to view available commands.\\n"
            } else {
                "TERMINAL VIRTUAL A.E.G.I.S.  ·  BUILD 0.1\\n" +
                    "Lingkungan game siap digunakan.\\n" +
                    "Ketik 'help' untuk melihat daftar perintah.\\n"
            }

        val Saver: Saver<TerminalSessionState, Any> = listSaver(
            save = { state ->
                listOf(state.command, state.currentDirectory, state.output)
            },
            restore = { saved ->
                TerminalSessionState(
                    command = saved[0] as String,
                    currentDirectory = saved[1] as String,
                    output = saved[2] as String,
                    busy = false
                )
            }
        )
    }
}
