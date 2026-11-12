package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class BrowserSessionStateTest {
    @Test
    fun unauthenticatedSessionStartsAtLogin() {
        val state = BrowserSessionState()

        assertEquals(false, state.authenticated)
        assertEquals("login", state.page)
        assertEquals(GameScreen.APP_HUB.name, state.returnScreen)
    }

    @Test
    fun authenticatedSessionStartsAtHome() {
        val state = BrowserSessionState(authenticated = true)

        assertEquals("home", state.page)
        assertEquals("home", state.initialPage())
    }

    @Test
    fun openingBrowserFromMessengerPreservesReturnDestination() {
        val state = BrowserSessionState(authenticated = true)
            .copy(returnScreen = GameScreen.MESSENGER.name, page = "repository")

        assertEquals(GameScreen.MESSENGER.name, state.returnScreen)
        assertEquals("repository", state.page)
        assertEquals(true, state.authenticated)
    }

    @Test
    fun restoringSessionPreservesAuthenticationPageAndReturnDestination() {
        val original = BrowserSessionState(
            authenticated = true,
            page = "mission_control",
            returnScreen = GameScreen.MESSENGER.name
        )
        val saverScope = object : androidx.compose.runtime.saveable.SaverScope {
            override fun canBeSaved(value: Any): Boolean = true
        }

        val saved = with(BrowserSessionState.Saver) { with(saverScope) { save(original) } }
        val restored = BrowserSessionState.Saver.restore(saved!!)

        assertEquals(original, restored)
    }
}
