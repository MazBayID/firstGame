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
    fun missionCompletionUpdatesRankFromTotalExperience() {
        val repository = MemoryGameStateRepository(GameState(experience = 90))
        val completed = CompleteMissionUseCase(repository).complete("first_contact.signal", 25)
        assertEquals(115, completed.experience)
        assertEquals("INITIATE", completed.rank)
    }

    @Test
    fun progressionThresholdsFollowApprovedRankOrder() {
        assertEquals("CIVILIAN", ProgressionRules.rankForExperience(0))
        assertEquals("RECRUIT", ProgressionRules.rankForExperience(25))
        assertEquals("INITIATE", ProgressionRules.rankForExperience(100))
        assertEquals("JUNIOR_ANALYST", ProgressionRules.rankForExperience(250))
        assertEquals("FIELD_ANALYST", ProgressionRules.rankForExperience(500))
        assertEquals("CYBER_OPERATIVE", ProgressionRules.rankForExperience(850))
        assertEquals("SENIOR_OPERATIVE", ProgressionRules.rankForExperience(1250))
        assertEquals("SPECIAL_AGENT", ProgressionRules.rankForExperience(1800))
        assertEquals("ELITE_OPERATIVE", ProgressionRules.rankForExperience(2500))
        assertEquals(25, ProgressionRules.nextRankThreshold(0))
        assertEquals(null, ProgressionRules.nextRankThreshold(2500))
    }

    @Test
    fun checkpointRejectsBlankIdentifier() {
        val repository = MemoryGameStateRepository()
        assertThrows(IllegalArgumentException::class.java) {
            CheckpointUseCase(repository).save(" ")
        }
    }
}
