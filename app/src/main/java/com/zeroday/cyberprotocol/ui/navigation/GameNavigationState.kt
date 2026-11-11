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
    val hubReturnScreen: GameScreen = GameScreen.CAMPAIGN
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
            GameScreen.MESSENGER -> GameScreen.APP_HUB
            GameScreen.TERMINAL, GameScreen.DATABASE, GameScreen.DOSSIER,
            GameScreen.ANALYSIS, GameScreen.OPERATIONS -> GameScreen.APP_HUB
            GameScreen.PROFILE, GameScreen.ARCHIVES, GameScreen.ACHIEVEMENTS,
            GameScreen.SETTINGS, GameScreen.LANGUAGE -> GameScreen.MAIN_MENU
            GameScreen.MAIN_MENU -> GameScreen.MAIN_MENU
        }
        return copy(currentScreen = destination)
    }

    companion object {
        val Saver: Saver<GameNavigationState, Any> = listSaver(
            save = { listOf(it.currentScreen.name, it.hubReturnScreen.name) },
            restore = {
                GameNavigationState(
                    currentScreen = GameScreen.valueOf(it[0]),
                    hubReturnScreen = GameScreen.valueOf(it[1])
                )
            }
        )
    }
}
