package com.zeroday.cyberprotocol.core.model

/**
 * Authored mission content is data, not UI logic. The evaluator ID maps to a
 * deterministic teaching contract in the Python runtime.
 */
enum class MissionKind { STORY_CODE, INVESTIGATION, COMBINED }

data class MissionObjective(
    val id: String,
    val title: String,
    val description: String,
    val required: Boolean = true
) {
    init {
        require(id.isNotBlank())
        require(title.isNotBlank())
        require(description.isNotBlank())
    }
}

data class MissionHint(
    val level: Int,
    val message: String
) {
    init {
        require(level in 1..3)
        require(message.isNotBlank())
    }
}

data class MissionDefinition(
    val id: String,
    val campaignId: String,
    val chapterId: String,
    val title: String,
    val briefing: String,
    val kind: MissionKind,
    val evaluatorId: Int?,
    val rewardXp: Int,
    val requiredClearance: Int,
    val workspaceDirectory: String,
    val allowedFiles: Set<String>,
    val writableFiles: Set<String> = allowedFiles,
    val objectives: List<MissionObjective>,
    val hints: List<MissionHint>
) {
    init {
        require(id.isNotBlank())
        require(campaignId.isNotBlank())
        require(chapterId.isNotBlank())
        require(title.isNotBlank())
        require(briefing.isNotBlank())
        require(rewardXp >= 0)
        require(requiredClearance >= 0)
        require(workspaceDirectory.startsWith("/A.E.G.I.S/missions/"))
        require(allowedFiles.all { it.matches(Regex("[A-Za-z0-9_.-]{1,64}")) && it != "." && it != ".." })
        require(writableFiles.all { it in allowedFiles })
        require(objectives.isNotEmpty())
        require(objectives.map { it.id }.distinct().size == objectives.size)
        require(hints.map { it.level }.distinct().size == hints.size)
    }
}
