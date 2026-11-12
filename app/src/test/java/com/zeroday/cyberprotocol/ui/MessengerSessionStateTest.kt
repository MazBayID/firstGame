package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MessengerSessionStateTest {
    @Test
    fun newSessionStartsWithInboxAndAppHubReturnDestination() {
        val state = MessengerSessionState()
        assertEquals("", state.selectedContact)
        assertEquals(GameScreen.APP_HUB.name, state.returnScreen)
        assertTrue(state.repliesFor("FREYA").isEmpty())
        assertTrue(state.repliesFor("NEXUS").isEmpty())
    }

    @Test
    fun selectingContactAndChangingReturnDestinationPreservesOtherState() {
        val state = MessengerSessionState(freyaReplies = listOf("Apa fungsi Terminal?"))
            .copy(selectedContact = "FREYA", returnScreen = GameScreen.STORY.name)
        assertEquals("FREYA", state.selectedContact)
        assertEquals(GameScreen.STORY.name, state.returnScreen)
        assertEquals(listOf("Apa fungsi Terminal?"), state.repliesFor("FREYA"))
    }

    @Test
    fun replyHistoryIsSeparatedByContactAndLimitedToFifty() {
        val state = MessengerSessionState()
            .appendReply("FREYA", "Pesan Freya")
            .appendReply("NEXUS", "Pesan NEXUS")
        val expanded = (1..55).fold(state) { current, index ->
            current.appendReply("FREYA", "Freya $index")
        }
        assertEquals("Freya 6", expanded.freyaReplies.first())
        assertEquals(50, expanded.freyaReplies.size)
        assertEquals("Freya 55", expanded.freyaReplies.last())
        assertEquals(listOf("Pesan NEXUS"), expanded.nexusReplies)
        assertTrue(expanded.repliesFor("AEGIS").isEmpty())
    }

    @Test
    fun blankOrUnsupportedContactReplyDoesNotChangeState() {
        val state = MessengerSessionState(selectedContact = "AEGIS")
        assertEquals(state, state.appendReply("AEGIS", "Pesan"))
        assertEquals(state, state.appendReply("FREYA", "  "))
    }

    @Test
    fun saverRestoresSelectedContactReturnDestinationAndBothHistories() {
        val original = MessengerSessionState(
            selectedContact = "NEXUS",
            returnScreen = GameScreen.MESSENGER.name,
            freyaReplies = listOf("Balasan Freya 1", "Balasan Freya 2"),
            nexusReplies = listOf("Balasan NEXUS")
        )
        val saverScope = object : androidx.compose.runtime.saveable.SaverScope {
            override fun canBeSaved(value: Any): Boolean = true
        }
        val saved = with(MessengerSessionState.Saver) { with(saverScope) { save(original) } }
        val restored = MessengerSessionState.Saver.restore(saved!!)
        assertEquals(original, restored)
    }
}
