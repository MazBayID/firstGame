package com.zeroday.cyberprotocol.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.zeroday.cyberprotocol.core.model.CaseDefinition
import com.zeroday.cyberprotocol.core.model.CompetencyTrack
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceProvenance
import com.zeroday.cyberprotocol.core.model.EvidenceRelationType
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.data.EvidenceAssetCatalog
import com.zeroday.cyberprotocol.data.LocalGameStateRepository
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import com.zeroday.cyberprotocol.domain.usecase.CompleteMissionUseCase
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules
import com.zeroday.cyberprotocol.domain.usecase.RecordEvidenceUseCase
import com.zeroday.cyberprotocol.domain.usecase.UpdateCaseProgressUseCase

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
        val beat = value.coerceIn(0, 16)
        updateState {
            it.copy(
                storyBeat = beat,
                clearance = if (beat >= 10) maxOf(it.clearance, 1) else it.clearance,
                unlockedApps = it.unlockedApps + ProgressionRules.unlockedAppsForStoryBeat(beat),
                lastCheckpoint = "story_beat_" + beat
            )
        }
        if (beat >= 10) {
            ensureRecruitmentEvidence()
            saveCheckpoint("story_beat_" + beat)
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

    fun openFreyaIntroduction() {
        updateState { current ->
            // Freya's introduction is a story-gated notification, not an inbox shortcut.
            // It may only be acknowledged after the first mission has actually become active.
            if (current.freyaContactOpened ||
                current.storyBeat < 4 ||
                current.activeMissionId != "first_contact.signal"
            ) current
            else current.copy(freyaContactOpened = true, lastCheckpoint = "freya_introduction_opened")
        }
    }

    fun unlockNexusContact() {
        updateState { current ->
            if (!current.freyaContactOpened || current.nexusContactUnlocked) current
            else current.copy(nexusContactUnlocked = true, lastCheckpoint = "nexus_contact_unlocked")
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
    fun completeMission(
        missionId: String,
        rewardXp: Int,
        requiredClearance: Int,
        competencyTrack: CompetencyTrack? = null
    ): Boolean {
        val alreadyCompleted = repository.load().completedMissionIds.contains(missionId)
        CompleteMissionUseCase(repository).complete(missionId, rewardXp, requiredClearance, competencyTrack)
        refresh()
        return !alreadyCompleted
    }

    fun recordEvidence(record: EvidenceRecord) {
        RecordEvidenceUseCase(repository).record(record)
        refresh()
    }

    fun reviewEvidence(evidenceId: String, status: EvidenceStatus, reason: String) {
        RecordEvidenceUseCase(repository).review(
            evidenceId = evidenceId,
            status = status,
            reviewerId = "operative:player",
            reason = reason
        )
        refresh()
    }

    fun relateEvidence(
        evidenceId: String,
        relatedEvidenceId: String,
        type: EvidenceRelationType,
        note: String = ""
    ) {
        RecordEvidenceUseCase(repository).relate(evidenceId, relatedEvidenceId, type, note)
        refresh()
    }

    fun acknowledgeCaseBriefing(definition: CaseDefinition) {
        UpdateCaseProgressUseCase(repository).acknowledgeBriefing(definition)
        refresh()
    }

    fun discoverCaseEvidence(definition: CaseDefinition, evidenceId: String) {
        UpdateCaseProgressUseCase(repository).discoverEvidence(definition, evidenceId)
        refresh()
    }

    fun analyzeCaseEvidence(definition: CaseDefinition, evidenceId: String) {
        UpdateCaseProgressUseCase(repository).analyzeEvidence(definition, evidenceId)
        refresh()
    }

    fun selectCaseDecision(definition: CaseDefinition, decisionId: String) {
        UpdateCaseProgressUseCase(repository).selectDecision(definition, decisionId)
        refresh()
    }

    fun acknowledgeCaseDebrief(definition: CaseDefinition) {
        UpdateCaseProgressUseCase(repository).acknowledgeDebrief(definition)
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

    /**
     * Repairs missing RX-14 source records for existing saves that already reached
     * the investigation checkpoint before case progression was introduced.
     * Existing reviews, provenance, and relation history are preserved by the evidence use case.
     */
    fun ensureRecruitmentEvidence() {
        val current = repository.load()
        val expectedSourceIds = EvidenceAssetCatalog.assets.map { it.evidenceId }.toSet()
        val discrepancy = current.evidence.firstOrNull { it.id == "transmission_timestamp_discrepancy" }
        val sourceRelationsComplete = discrepancy?.relations
            ?.filter { it.type == EvidenceRelationType.DERIVED_FROM }
            ?.map { it.relatedEvidenceId }
            ?.toSet()
            ?.containsAll(expectedSourceIds) == true
        val contradictionComplete = current.evidence
            .firstOrNull { it.id == expectedSourceIds.firstOrNull() }
            ?.relations
            ?.any { it.relatedEvidenceId == expectedSourceIds.lastOrNull() && it.type == EvidenceRelationType.CONTRADICTS } == true
        if (expectedSourceIds.all { id -> current.evidence.any { it.id == id } } &&
            discrepancy != null && sourceRelationsComplete && contradictionComplete
        ) return

        recordRecruitmentLead()
        refresh()
    }

    private fun recordRecruitmentLead() {
        val collectedAt = System.currentTimeMillis()
        EvidenceAssetCatalog.assets.forEach { asset ->
            recordEvidence(
                EvidenceRecord(
                    id = asset.evidenceId,
                    title = asset.title,
                    summary = asset.content.trim(),
                    source = asset.sourceLabel,
                    missionId = EvidenceAssetCatalog.MISSION_ID,
                    recordedAtEpochMillis = collectedAt,
                    provenance = EvidenceProvenance(
                        origin = asset.origin,
                        sourceLabel = asset.sourceLabel,
                        sourceAssetId = asset.id,
                        sourcePath = asset.path,
                        collectedAtEpochMillis = collectedAt,
                        collectorId = "system:aegis-ingest"
                    )
                )
            )
        }
        recordEvidence(
            EvidenceRecord(
                id = "transmission_timestamp_discrepancy",
                title = "Perbedaan Waktu Penerimaan Transmisi",
                summary = "Dua sistem internal mencatat referensi RX-14 pada waktu berbeda empat detik. Perbedaan teramati, tetapi penyebabnya belum diketahui.",
                source = "A.E.G.I.S. / perbandingan metadata tanda terima",
                missionId = EvidenceAssetCatalog.MISSION_ID,
                recordedAtEpochMillis = collectedAt,
                provenance = EvidenceProvenance(
                    origin = com.zeroday.cyberprotocol.core.model.EvidenceOrigin.ANALYSIS,
                    sourceLabel = "Perbandingan dua sumber penerimaan",
                    collectedAtEpochMillis = collectedAt,
                    collectorId = "system:aegis-analysis"
                )
            )
        )
        EvidenceAssetCatalog.assets.forEach { asset ->
            relateEvidence(
                "transmission_timestamp_discrepancy",
                asset.evidenceId,
                EvidenceRelationType.DERIVED_FROM,
                "Temuan perbedaan waktu diturunkan dari sumber asli ini; sumber tidak diubah."
            )
        }
        relateEvidence(
            EvidenceAssetCatalog.assets[0].evidenceId,
            EvidenceAssetCatalog.assets[1].evidenceId,
            EvidenceRelationType.CONTRADICTS,
            "Kedua sumber mencatat waktu penerimaan RX-14 yang berbeda empat detik."
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
