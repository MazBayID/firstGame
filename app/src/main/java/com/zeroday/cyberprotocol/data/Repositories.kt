package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.*
import org.json.JSONObject

class ContentRepository(private val context: Context) {
    fun loadChapterOne(): ChapterContent {
        val raw = context.assets.open("chapter_01.json").bufferedReader().use { it.readText() }
        val root = JSONObject(raw)
        val lessonsJson = root.getJSONArray("lessons")
        val lessons = (0 until lessonsJson.length()).map { index ->
            val item = lessonsJson.getJSONObject(index)
            Lesson(
                item.getString("id"), item.getString("title"), item.getString("concept"),
                item.getString("explanation"), item.getString("example"), item.getString("expectedOutput")
            )
        }
        val missionsJson = root.getJSONArray("missions")
        val missions = (0 until missionsJson.length()).map { index ->
            val item = missionsJson.getJSONObject(index)
            val concepts = item.getJSONArray("concepts")
            val hints = item.getJSONArray("hints")
            Mission(
                item.getInt("id"), item.getString("title"), item.getString("briefing"),
                item.getString("objective"),
                (0 until concepts.length()).map { concepts.getString(it) },
                item.getString("starterCode"),
                (0 until hints.length()).map { hints.getString(it) }
            )
        }
        return ChapterContent(
            root.getString("id"), root.getString("title"), root.getString("subtitle"),
            root.getString("opening"), lessons, missions
        )
    }
}

data class PlayerProgress(val xp: Int, val credits: Int, val completedMissions: Set<Int>)

class ProgressRepository(context: Context) {
    private val prefs = context.getSharedPreferences("cyber_protocol_player_v1", Context.MODE_PRIVATE)
    fun load(): PlayerProgress {
        val completed = (1..8).filter { prefs.getBoolean("mission_$it", false) }.toSet()
        return PlayerProgress(prefs.getInt("xp", 0), prefs.getInt("credits", 0), completed)
    }
    /** Rewards are granted once; replaying a completed mission never farms XP. */
    fun completeMissionOnce(missionId: Int): PlayerProgress {
        if (!prefs.getBoolean("mission_$missionId", false)) {
            prefs.edit().putBoolean("mission_$missionId", true)
                .putInt("xp", prefs.getInt("xp", 0) + 100)
                .putInt("credits", prefs.getInt("credits", 0) + 50).apply()
        }
        return load()
    }
}
