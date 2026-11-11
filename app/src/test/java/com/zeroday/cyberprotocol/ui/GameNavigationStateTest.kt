package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class GameNavigationStateTest {
    @Test
    fun hubReturnsToTheDestinationThatOpenedIt() {
        val state = GameNavigationState()
            .openHub(GameScreen.STORY)
            .navigateTo(GameScreen.DATABASE)
            .back(storyStage = 4, messengerHasSelectedContact = false)
            .back(storyStage = 4, messengerHasSelectedContact = false)

        assertEquals(GameScreen.STORY, state.currentScreen)
        assertEquals(GameScreen.STORY, state.hubReturnScreen)
    }

    @Test
    fun browserReturnsToTheAppHub() {
        val state = GameNavigationState(currentScreen = GameScreen.BROWSER)
        assertEquals(GameScreen.APP_HUB, state.back(4, false).currentScreen)
    }

    @Test
    fun backFromDebriefReturnsToStoryUntilRecruitmentIsComplete() {
        val state = GameNavigationState(currentScreen = GameScreen.DEBRIEF)

        assertEquals(GameScreen.STORY, state.back(6, false).currentScreen)
        assertEquals(GameScreen.CAMPAIGN, state.back(7, false).currentScreen)
    }

    @Test
    fun messengerBackFirstClosesTheSelectedConversation() {
        val state = GameNavigationState(currentScreen = GameScreen.MESSENGER)

        assertEquals(state, state.back(0, messengerHasSelectedContact = true))
        assertEquals(GameScreen.APP_HUB, state.back(0, messengerHasSelectedContact = false).currentScreen)
    }

    @Test
    fun appScreensReturnToHubAndMenuScreensReturnToMenu() {
        val hub = GameNavigationState(currentScreen = GameScreen.TERMINAL)
        val menu = GameNavigationState(currentScreen = GameScreen.PROFILE)

        assertEquals(GameScreen.APP_HUB, hub.back(0, false).currentScreen)
        assertEquals(GameScreen.MAIN_MENU, menu.back(0, false).currentScreen)
    }
}
