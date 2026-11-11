package com.zeroday.cyberprotocol.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.LocalGameStateRepository
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import com.zeroday.cyberprotocol.domain.usecase.CompleteMissionUseCase
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules
import com.zeroday.cyberprotocol.domain.usecase.RecordEvidenceUseCase

/** Owns campaign persistence and progression mutations outside the Compose screen. */
internal class GameSessionViewModel(
    private val repository: GameStateRepository
) : ViewModel() {
    var gameState by mutableStateOf(repository.load())
        private set

    fun refresh(): GameState {
        gameState = repository.load()
        return gameState
    }

    fun saveStoryBeat(value: Int) {
        val beat = value.coerceIn(0, 7)
        updateState {
            it.copy(
                storyBeat = beat,
                clearance = if (beat >= 7) maxOf(it.clearance, 1) else it.clearance,
                unlockedApps = ProgressionRules.unlockedAppsForStoryBeat(beat),
                lastCheckpoint = "story_beat_" + beat
            )
        }
        if (beat >= 7) {
            recordRecruitmentLead()
            saveCheckpoint("story_beat_7")
        }
    }

    fun openRecruitmentInvitation() {
        updateState { current ->
            if (current.recruitmentNotificationOpened) current
            else current.copy(
                recruitmentNotificationOpened = true,
                storyBeat = maxOf(current.storyBeat, 2),
                lastCheckpoint = "recruitment_invitation_opened"
            )
        }
    }

    fun startMission(missionId: String) {
        if (missionId.isBlank()) return
        updateState { current ->
            if (missionId in current.completedMissionIds || current.activeMissionId == missionId) current
            else current.copy(activeMissionId = missionId, lastCheckpoint = "mission_started:" + missionId)
        }
    }

    /** Returns true only when this call awards the mission for the first time. */
    fun completeMission(missionId: String, rewardXp: Int, requiredClearance: Int): Boolean {
        val alreadyCompleted = repository.load().completedMissionIds.contains(missionId)
        CompleteMissionUseCase(repository).complete(missionId, rewardXp, requiredClearance)
        refresh()
        return !alreadyCompleted
    }

    fun recordEvidence(record: EvidenceRecord) {
        RecordEvidenceUseCase(repository).record(record)
        refresh()
    }

    fun saveCode(key: String, source: String, checkpoint: String) {
        if (key.isBlank() || source.length > 50_000) return
        updateState {
            it.copy(
                savedCode = it.savedCode + (key to source),
                lastCheckpoint = checkpoint
            )
        }
    }

    fun saveCheckpoint(name: String) {
        if (name.isBlank()) return
        updateState { it.copy(lastCheckpoint = name) }
    }

    fun resetCampaign() {
        repository.save(GameState(lastCheckpoint = "new_campaign"))
        refresh()
    }

    private fun recordRecruitmentLead() {
        val state = repository.load()
        if (state.evidence.any { it.id == "transmission_timestamp_discrepancy" }) return
        recordEvidence(
            EvidenceRecord(
                id = "transmission_timestamp_discrepancy",
                title = "Perbedaan Waktu Penerimaan Transmisi",
                summary = "Dua sistem internal mencatat waktu penerimaan pesan yang berbeda. Perbedaan teramati, tetapi penyebabnya belum diketahui.",
                source = "NEXUS / perbandingan metadata tanda terima",
                missionId = "first_contact.recruitment"
            )
        )
    }

    private fun updateState(transform: (GameState) -> GameState) {
        repository.save(transform(repository.load()))
        refresh()
    }

    class Factory(context: Context) : ViewModelProvider.Factory {
        private val appContext = context.applicationContext

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(GameSessionViewModel::class.java))
            return GameSessionViewModel(LocalGameStateRepository(appContext)) as T
        }
    }
}
