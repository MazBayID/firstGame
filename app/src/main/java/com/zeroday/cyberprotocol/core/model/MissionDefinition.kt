package com.zeroday.cyberprotocol.core.model

/** Authored mission content is data, not UI logic. */
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

data class MissionHint(val level: Int, val message: String) {
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
    val primaryArchetype: MissionArchetype = when (kind) {
        MissionKind.STORY_CODE -> MissionArchetype.TECHNICAL
        MissionKind.INVESTIGATION -> MissionArchetype.INVESTIGATION
        MissionKind.COMBINED -> MissionArchetype.INVESTIGATION
    },
    val secondaryArchetypes: Set<MissionArchetype> = emptySet(),
    val evaluatorId: Int?,
    val rewardXp: Int,
    val requiredClearance: Int,
    val workspaceDirectory: String,
    val allowedFiles: Set<String>,
    val writableFiles: Set<String> = allowedFiles,
    val runtimeVariables: Map<String, List<String>> = emptyMap(),
    val initialFiles: Map<String, String> = emptyMap(),
    val nextClue: String? = null,
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
        require(evaluatorId == null || evaluatorId > 0)
        if (kind == MissionKind.STORY_CODE) require(evaluatorId != null)
        require(requiredClearance >= 0)
        require(workspaceDirectory.startsWith("/A.E.G.I.S/missions/"))
        require(allowedFiles.isNotEmpty())
        require(allowedFiles.all { it.matches(Regex("[A-Za-z0-9_.-]{1,64}")) && it != "." && it != ".." })
        require(writableFiles.all { it in allowedFiles })
        require(initialFiles.keys.all { it in allowedFiles && it !in writableFiles })
        require(initialFiles.values.all { it.length <= 50_000 })
        require(runtimeVariables.all { (name, values) ->
            name.matches(Regex("[A-Za-z][A-Za-z0-9_]{0,63}")) &&
                !name.startsWith("_") && "__" !in name && values.size <= 1000 &&
                values.all { it.length <= 1000 }
        })
        require(nextClue == null || nextClue.isNotBlank())
        require(objectives.isNotEmpty())
        require(objectives.map { it.id }.distinct().size == objectives.size)
        require(hints.map { it.level }.distinct().size == hints.size)
    }
}
