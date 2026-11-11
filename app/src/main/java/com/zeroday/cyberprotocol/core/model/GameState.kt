package com.zeroday.cyberprotocol.core.model

/** Versioned, serializable snapshot of the player's local campaign state. */
data class GameState(
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val campaignId: String = "campaign_01_recruitment",
    val storyBeat: Int = 0,
    val activeMissionId: String? = null,
    val completedMissionIds: Set<String> = emptySet(),
    val rank: String = "CIVILIAN",
    val experience: Int = 0,
    val clearance: Int = 0,
    val unlockedApps: Set<String> = setOf("MESSENGER"),
    val evidence: List<EvidenceRecord> = emptyList(),
    val savedCode: Map<String, String> = emptyMap(),
    val lastCheckpoint: String = "new_game",
    val updatedAtEpochMillis: Long = 0L
) {
    companion object {
        const val CURRENT_SCHEMA_VERSION = 1
    }
}

enum class EvidenceStatus { UNVERIFIED, INFERRED, CONFIRMED, DISPUTED }

data class EvidenceRecord(
    val id: String,
    val title: String,
    val summary: String,
    val source: String,
    val status: EvidenceStatus = EvidenceStatus.UNVERIFIED,
    val missionId: String
)

enum class FilePermission { READ_ONLY, READ_WRITE }

data class WorkspaceFile(
    val path: String,
    val content: String,
    val missionId: String? = null,
    val permission: FilePermission = FilePermission.READ_WRITE
)
