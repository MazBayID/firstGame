package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository

/**
 * Applies an already-evaluated mission result.
 * Execution is deliberately outside this use case: RUN never changes campaign progress.
 */
class CompleteMissionUseCase(private val repository: GameStateRepository) {
    fun complete(missionId: String, rewardXp: Int, requiredClearance: Int = 0): GameState {
        require(missionId.isNotBlank()) { "Mission ID must not be blank." }
        require(rewardXp >= 0) { "Mission XP cannot be negative." }
        val current = repository.load()
        require(current.clearance >= requiredClearance) { "Required clearance has not been granted." }
        if (missionId in current.completedMissionIds) return current

        val updated = current.copy(
            activeMissionId = null,
            completedMissionIds = current.completedMissionIds + missionId,
            experience = (current.experience + rewardXp).coerceAtLeast(0),
            lastCheckpoint = "mission_completed:$missionId"
        )
        repository.save(updated)
        return repository.load()
    }
}
