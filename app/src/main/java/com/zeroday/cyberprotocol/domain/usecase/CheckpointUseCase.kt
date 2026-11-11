package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository

/** Persists a named, meaningful game checkpoint. */
class CheckpointUseCase(private val repository: GameStateRepository) {
    fun save(checkpointId: String, transform: (GameState) -> GameState = { it }): GameState {
        require(checkpointId.isNotBlank()) { "Checkpoint ID must not be blank." }
        val next = transform(repository.load()).copy(lastCheckpoint = checkpointId)
        repository.save(next)
        return repository.load()
    }
}
