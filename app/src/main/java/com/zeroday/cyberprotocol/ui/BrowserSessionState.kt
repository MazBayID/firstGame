package com.zeroday.cyberprotocol.ui

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

/**
 * Saveable state for the offline A.E.G.I.S. Browser session.
 *
 * Authentication remains persisted by the existing SharedPreferences store;
 * this model keeps the current page and return destination in the UI session.
 */
internal data class BrowserSessionState(
    val authenticated: Boolean = false,
    val page: String = initialPage(authenticated),
    val returnScreen: String = GameScreen.APP_HUB.name
) {
    fun initialPage(): String = if (authenticated) "home" else "login"

    companion object {
        private fun initialPage(authenticated: Boolean): String =
            if (authenticated) "home" else "login"

        val Saver: Saver<BrowserSessionState, Any> = listSaver(
            save = { state ->
                listOf(state.authenticated, state.page, state.returnScreen)
            },
            restore = { saved ->
                BrowserSessionState(
                    authenticated = saved[0] as Boolean,
                    page = saved[1] as String,
                    returnScreen = saved[2] as String
                )
            }
        )
    }
}
