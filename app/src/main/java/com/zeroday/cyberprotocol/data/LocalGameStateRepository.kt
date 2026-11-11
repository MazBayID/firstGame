package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.json.JSONArray
import org.json.JSONObject

/**
 * Local, versioned campaign persistence.
 *
 * Writes are atomic at the SharedPreferences value level. Callers should save only
 * meaningful checkpoints, not every UI recomposition or keystroke.
 */
class LocalGameStateRepository(context: Context) : GameStateRepository {
    private val appContext = context.applicationContext
    private val preferences = appContext.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    @Synchronized
    override fun load(): GameState {
        val raw = preferences.getString(KEY_SNAPSHOT, null)
        if (raw.isNullOrBlank()) return migrateLegacyProgress()
        return runCatching { decode(JSONObject(raw)) }.getOrElse { migrateLegacyProgress() }
    }

    @Synchronized
    override fun save(state: GameState) {
        val normalized = state.copy(
            schemaVersion = GameState.CURRENT_SCHEMA_VERSION,
            storyBeat = state.storyBeat.coerceAtLeast(0),
            experience = state.experience.coerceAtLeast(0),
            clearance = state.clearance.coerceAtLeast(0),
            updatedAtEpochMillis = System.currentTimeMillis()
        )
        preferences.edit().putString(KEY_SNAPSHOT, encode(normalized).toString()).apply()
    }

    @Synchronized
    override fun clear() {
        preferences.edit().remove(KEY_SNAPSHOT).apply()
    }

    private fun migrateLegacyProgress(): GameState {
        val legacy = runCatching {
            contextPreferencesLegacy()
        }.getOrDefault(0)
        return GameState(storyBeat = legacy.coerceIn(0, 7))
    }

    private fun contextPreferencesLegacy(): Int {
        // Legacy key used by the original opening-slice implementation.
        return appContext.getSharedPreferences("cyber_protocol_campaign_v2", Context.MODE_PRIVATE)
            .getInt("story_stage", 0)
    }

    private fun encode(state: GameState): JSONObject = JSONObject().apply {
        put("schemaVersion", state.schemaVersion)
        put("campaignId", state.campaignId)
        put("storyBeat", state.storyBeat)
        put("activeMissionId", state.activeMissionId)
        put("completedMissionIds", JSONArray(state.completedMissionIds.sorted()))
        put("rank", state.rank)
        put("experience", state.experience)
        put("clearance", state.clearance)
        put("unlockedApps", JSONArray(state.unlockedApps.sorted()))
        put("lastCheckpoint", state.lastCheckpoint)
        put("updatedAtEpochMillis", state.updatedAtEpochMillis)
        put("savedCode", JSONObject().apply {
            state.savedCode.forEach { (key, value) -> put(key, value) }
        })
        put("evidence", JSONArray().apply {
            state.evidence.forEach { record ->
                put(JSONObject().apply {
                    put("id", record.id)
                    put("title", record.title)
                    put("summary", record.summary)
                    put("source", record.source)
                    put("status", record.status.name)
                    put("missionId", record.missionId)
                })
            }
        })
    }

    private fun decode(json: JSONObject): GameState {
        val version = json.optInt("schemaVersion", 0)
        require(version in 1..GameState.CURRENT_SCHEMA_VERSION) { "Unsupported save schema: $version" }
        val codeJson = json.optJSONObject("savedCode") ?: JSONObject()
        val code = linkedMapOf<String, String>()
        codeJson.keys().forEach { key -> code[key] = codeJson.optString(key, "") }
        val evidenceJson = json.optJSONArray("evidence") ?: JSONArray()
        val evidence = buildList {
            for (index in 0 until evidenceJson.length()) {
                val item = evidenceJson.optJSONObject(index) ?: continue
                val status = runCatching {
                    EvidenceStatus.valueOf(item.optString("status", "UNVERIFIED"))
                }.getOrDefault(EvidenceStatus.UNVERIFIED)
                add(EvidenceRecord(
                    id = item.optString("id"),
                    title = item.optString("title"),
                    summary = item.optString("summary"),
                    source = item.optString("source"),
                    status = status,
                    missionId = item.optString("missionId")
                ))
            }
        }
        return GameState(
            schemaVersion = version,
            campaignId = json.optString("campaignId", "campaign_01_recruitment"),
            storyBeat = json.optInt("storyBeat", 0).coerceAtLeast(0),
            activeMissionId = json.optString("activeMissionId").takeIf { it.isNotBlank() && it != "null" },
            completedMissionIds = json.stringSet("completedMissionIds"),
            rank = json.optString("rank", "CIVILIAN"),
            experience = json.optInt("experience", 0).coerceAtLeast(0),
            clearance = json.optInt("clearance", 0).coerceAtLeast(0),
            unlockedApps = json.stringSet("unlockedApps").ifEmpty { setOf("MESSENGER") },
            evidence = evidence,
            savedCode = code,
            lastCheckpoint = json.optString("lastCheckpoint", "recovered"),
            updatedAtEpochMillis = json.optLong("updatedAtEpochMillis", 0L)
        )
    }

    private fun JSONObject.stringSet(key: String): Set<String> {
        val array = optJSONArray(key) ?: return emptySet()
        return buildSet { for (index in 0 until array.length()) add(array.optString(index)) }
    }

    companion object {
        private const val STORE_NAME = "cp_game_state_v1"
        private const val KEY_SNAPSHOT = "snapshot"
    }
}
