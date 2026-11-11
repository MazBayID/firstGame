package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class GameProgressUseCasesTest {
    private class MemoryGameStateRepository(
        private var state: GameState = GameState()
    ) : GameStateRepository {
        override fun load(): GameState = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }

    @Test
    fun checkpointPersistsNamedCheckpoint() {
        val repository = MemoryGameStateRepository()
        val result = CheckpointUseCase(repository).save("briefing_complete") {
            it.copy(storyBeat = 3)
        }
        assertEquals(3, result.storyBeat)
        assertEquals("briefing_complete", repository.load().lastCheckpoint)
    }

    @Test
    fun missionCompletionAwardsXpExactlyOnce() {
        val repository = MemoryGameStateRepository()
        val useCase = CompleteMissionUseCase(repository)
        useCase.complete("first_contact", 100)
        val repeated = useCase.complete("first_contact", 100)
        assertEquals(100, repeated.experience)
        assertEquals(setOf("first_contact"), repeated.completedMissionIds)
    }

    @Test
    fun missionCompletionRequiresClearance() {
        val repository = MemoryGameStateRepository(GameState(clearance = 0))
        val useCase = CompleteMissionUseCase(repository)
        assertThrows(IllegalArgumentException::class.java) {
            useCase.complete("restricted_case", 50, requiredClearance = 1)
        }
        assertEquals(emptySet<String>(), repository.load().completedMissionIds)
    }

    @Test
    fun checkpointRejectsBlankIdentifier() {
        val repository = MemoryGameStateRepository()
        assertThrows(IllegalArgumentException::class.java) {
            CheckpointUseCase(repository).save(" ")
        }
    }
}
