package com.zeroday.cyberprotocol.core.model

data class Lesson(
    val id: String, val title: String, val concept: String,
    val explanation: String, val example: String, val expectedOutput: String
)
data class Mission(
    val id: Int, val title: String, val briefing: String, val objective: String,
    val concepts: List<String>, val starterCode: String, val hints: List<String>
)
data class ChapterContent(
    val id: String, val title: String, val subtitle: String, val opening: String,
    val lessons: List<Lesson>, val missions: List<Mission>
)
data class ExecutionResult(val output: String, val error: String = "")
data class EvaluationResult(
    val passed: Boolean, val output: String, val feedback: String, val error: String = ""
)

class StoryEngine {
    fun firstAvailableMission(chapter: ChapterContent, completed: Set<Int>): Mission? =
        chapter.missions.firstOrNull { it.id == 1 || (it.id - 1) in completed }
    fun isChapterComplete(chapter: ChapterContent, completed: Set<Int>): Boolean =
        chapter.missions.all { it.id in completed }
    fun chapterProgress(chapter: ChapterContent, completed: Set<Int>): Float =
        if (chapter.missions.isEmpty()) 0f
        else chapter.missions.count { it.id in completed }.toFloat() / chapter.missions.size
}

class LearningEngine {
    fun lessonFor(mission: Mission, chapter: ChapterContent): Lesson? =
        chapter.lessons.firstOrNull { it.id == "lesson_%02d".format(mission.id) }
}
