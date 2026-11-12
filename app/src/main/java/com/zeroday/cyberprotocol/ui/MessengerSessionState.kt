package com.zeroday.cyberprotocol.ui

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

/**
 * Saveable state for the Secure Messenger UI session.
 *
 * Conversation histories are also mirrored to the existing SharedPreferences keys
 * so they survive app restarts independently of the transient UI session.
 */
internal data class MessengerSessionState(
    val selectedContact: String = "",
    val returnScreen: String = GameScreen.APP_HUB.name,
    val freyaReplies: List<String> = emptyList(),
    val nexusReplies: List<String> = emptyList()
) {
    fun repliesFor(contact: String): List<String> = when (contact) {
        "FREYA" -> freyaReplies
        "NEXUS" -> nexusReplies
        else -> emptyList()
    }

    fun appendReply(contact: String, reply: String): MessengerSessionState {
        if (reply.isBlank()) return this
        return when (contact) {
            "FREYA" -> copy(freyaReplies = (freyaReplies + reply).takeLast(MAX_REPLIES))
            "NEXUS" -> copy(nexusReplies = (nexusReplies + reply).takeLast(MAX_REPLIES))
            else -> this
        }
    }

    companion object {
        private const val MAX_REPLIES = 50
        private const val REPLY_SEPARATOR = "\u001F"

        private fun encodeReplies(replies: List<String>): String =
            replies.joinToString(REPLY_SEPARATOR)

        private fun decodeReplies(value: String): List<String> =
            value.split(REPLY_SEPARATOR).filter { it.isNotBlank() }.takeLast(MAX_REPLIES)

        val Saver: Saver<MessengerSessionState, Any> = listSaver(
            save = { state ->
                listOf(
                    state.selectedContact,
                    state.returnScreen,
                    encodeReplies(state.freyaReplies),
                    encodeReplies(state.nexusReplies)
                )
            },
            restore = { saved ->
                MessengerSessionState(
                    selectedContact = saved[0] as String,
                    returnScreen = saved[1] as String,
                    freyaReplies = decodeReplies(saved[2] as String),
                    nexusReplies = decodeReplies(saved[3] as String)
                )
            }
        )
    }
}
