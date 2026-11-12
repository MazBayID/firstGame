package com.zeroday.cyberprotocol.core.model

data class GameState(
    val schemaVersion: Int = CURRENT_SCHEMA_VERSION,
    val campaignId: String = "campaign_01_recruitment",
    val storyBeat: Int = 0,
    val recruitmentNotificationOpened: Boolean = false,
    val freyaContactOpened: Boolean = false,
    val nexusContactUnlocked: Boolean = false,
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
    companion object { const val CURRENT_SCHEMA_VERSION = 6 }
}

enum class EvidenceStatus { UNVERIFIED, INFERRED, CORROBORATED, CONFIRMED, DISPUTED, RETRACTED }
enum class EvidenceOrigin { LEGACY, MESSENGER, INTRANET, WORKSPACE_FILE, TERMINAL, DATABASE, ANALYSIS, OPERATIONS, SYSTEM }
enum class EvidenceRelationType { SUPPORTS, CONTRADICTS, CORROBORATES, DERIVED_FROM, DUPLICATES }

data class EvidenceProvenance(
    val origin: EvidenceOrigin = EvidenceOrigin.LEGACY,
    val sourceLabel: String = "",
    val sourceAssetId: String? = null,
    val sourcePath: String? = null,
    val collectedAtEpochMillis: Long = 0L,
    val collectorId: String? = null
) {
    init {
        require(collectedAtEpochMillis >= 0L)
        require(sourceAssetId == null || sourceAssetId.isNotBlank())
        require(sourcePath == null || sourcePath.isNotBlank())
        require(collectorId == null || collectorId.isNotBlank())
    }
}

data class EvidenceRelation(
    val relatedEvidenceId: String,
    val type: EvidenceRelationType,
    val note: String = ""
) {
    init { require(relatedEvidenceId.isNotBlank()) }
}

data class EvidenceReviewEntry(
    val previousStatus: EvidenceStatus,
    val newStatus: EvidenceStatus,
    val reviewerId: String,
    val reason: String,
    val reviewedAtEpochMillis: Long
) {
    init {
        require(reviewerId.isNotBlank())
        require(reason.isNotBlank())
        require(reviewedAtEpochMillis >= 0L)
        require(previousStatus != newStatus)
    }
}

data class EvidenceRecord(
    val id: String,
    val title: String,
    val summary: String,
    val source: String,
    val status: EvidenceStatus = EvidenceStatus.UNVERIFIED,
    val missionId: String,
    val recordedAtEpochMillis: Long = System.currentTimeMillis(),
    val provenance: EvidenceProvenance = EvidenceProvenance(
        sourceLabel = source, collectedAtEpochMillis = recordedAtEpochMillis
    ),
    val relations: List<EvidenceRelation> = emptyList(),
    val reviewHistory: List<EvidenceReviewEntry> = emptyList()
) {
    init {
        require(id.isNotBlank())
        require(title.isNotBlank())
        require(summary.isNotBlank())
        require(source.isNotBlank())
        require(missionId.isNotBlank())
        require(recordedAtEpochMillis >= 0L)
        require(relations.none { it.relatedEvidenceId == id }) { "Evidence cannot relate to itself." }
        require(relations.map { it.relatedEvidenceId to it.type }.distinct().size == relations.size) {
            "Duplicate evidence relations are not allowed."
        }
    }
}

enum class FilePermission { READ_ONLY, READ_WRITE }
data class WorkspaceFile(
    val path: String,
    val content: String,
    val missionId: String? = null,
    val permission: FilePermission = FilePermission.READ_WRITE
)
