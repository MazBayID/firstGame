package com.zeroday.cyberprotocol.domain.repository

import com.zeroday.cyberprotocol.core.model.GameState

/** Persistence boundary; domain logic does not depend on Android storage APIs. */
interface GameStateRepository {
    fun load(): GameState
    fun save(state: GameState)
    fun clear()
}
