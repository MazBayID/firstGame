package com.zeroday.cyberprotocol.ui

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

/**
 * Serializable navigation state for the game shell.
 *
 * Keeps the hub return destination explicit so opening an app never loses
 * the operation or menu from which the player entered the hub.
 */
internal data class GameNavigationState(
    val currentScreen: GameScreen = GameScreen.MAIN_MENU,
    val hubReturnScreen: GameScreen = GameScreen.CAMPAIGN,
    val caseReturnScreen: GameScreen = GameScreen.APP_HUB,
    val analysisReturnScreen: GameScreen = GameScreen.APP_HUB
) {
    fun navigateTo(destination: GameScreen): GameNavigationState =
        copy(currentScreen = destination)

    fun openHub(from: GameScreen): GameNavigationState =
        copy(currentScreen = GameScreen.APP_HUB, hubReturnScreen = from)

    fun back(storyStage: Int, messengerHasSelectedContact: Boolean): GameNavigationState {
        if (currentScreen == GameScreen.MESSENGER && messengerHasSelectedContact) return this

        val destination = when (currentScreen) {
            GameScreen.CAMPAIGN -> GameScreen.MAIN_MENU
            GameScreen.STORY -> GameScreen.CAMPAIGN
            GameScreen.DEBRIEF -> if (storyStage >= 7) GameScreen.CAMPAIGN else GameScreen.STORY
            GameScreen.APP_HUB -> hubReturnScreen
            GameScreen.MESSENGER, GameScreen.BROWSER -> GameScreen.APP_HUB
            GameScreen.TERMINAL, GameScreen.DATABASE, GameScreen.DOSSIER,
            GameScreen.PRACTICE -> GameScreen.APP_HUB
            GameScreen.ANALYSIS -> analysisReturnScreen
            GameScreen.OPERATIONS -> caseReturnScreen
            GameScreen.PROFILE, GameScreen.ARCHIVES, GameScreen.ACHIEVEMENTS,
            GameScreen.SETTINGS, GameScreen.LANGUAGE -> GameScreen.MAIN_MENU
            GameScreen.MAIN_MENU -> GameScreen.MAIN_MENU
        }
        return copy(currentScreen = destination)
    }

    companion object {
        private fun restoreScreen(value: Any?, fallback: GameScreen): GameScreen =
            (value as? String)
                ?.let { name -> runCatching { GameScreen.valueOf(name) }.getOrNull() }
                ?: fallback

        val Saver: Saver<GameNavigationState, Any> = listSaver(
            save = {
                listOf(
                    it.currentScreen.name,
                    it.hubReturnScreen.name,
                    it.caseReturnScreen.name,
                    it.analysisReturnScreen.name
                )
            },
            restore = { saved ->
                val currentScreen = restoreScreen(saved.getOrNull(0), GameScreen.MAIN_MENU)
                val legacyAnalysisReturn = saved.size < 4 && currentScreen == GameScreen.ANALYSIS
                GameNavigationState(
                    currentScreen = currentScreen,
                    hubReturnScreen = restoreScreen(saved.getOrNull(1), GameScreen.CAMPAIGN),
                    caseReturnScreen = restoreScreen(saved.getOrNull(2), GameScreen.APP_HUB),
                    analysisReturnScreen = restoreScreen(
                        saved.getOrNull(3),
                        if (legacyAnalysisReturn) {
                            restoreScreen(saved.getOrNull(1), GameScreen.APP_HUB)
                        } else GameScreen.APP_HUB
                    )
                )
            }
        )
    }
}
