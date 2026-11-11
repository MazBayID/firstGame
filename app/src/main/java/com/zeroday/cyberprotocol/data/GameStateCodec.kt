package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules
import org.json.JSONArray
import org.json.JSONObject

/**
 * Pure save-snapshot codec. Keeping serialization separate from Android storage
 * lets schema compatibility and evidence provenance be regression-tested.
 */
internal object GameStateCodec {
    fun encode(state: GameState): JSONObject = JSONObject().apply {
        put("schemaVersion", state.schemaVersion)
        put("campaignId", state.campaignId)
        put("storyBeat", state.storyBeat)
        put("recruitmentNotificationOpened", state.recruitmentNotificationOpened)
        put("freyaContactOpened", state.freyaContactOpened)
        put("nexusContactUnlocked", state.nexusContactUnlocked)
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
                    put("recordedAtEpochMillis", record.recordedAtEpochMillis)
                })
            }
        })
    }

    fun decode(json: JSONObject): GameState {
        val version = json.optInt("schemaVersion", 0)
        require(version in 1..GameState.CURRENT_SCHEMA_VERSION) {
            "Unsupported save schema: $version"
        }
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
                    missionId = item.optString("missionId"),
                    recordedAtEpochMillis = item.optLong("recordedAtEpochMillis", 0L)
                ))
            }
        }
        return GameState(
            schemaVersion = version,
            campaignId = json.optString("campaignId", "campaign_01_recruitment"),
            storyBeat = json.optInt("storyBeat", 0).coerceAtLeast(0),
            recruitmentNotificationOpened = json.optBoolean(
                "recruitmentNotificationOpened", json.optInt("storyBeat", 0) >= 2
            ),
            freyaContactOpened = json.optBoolean("freyaContactOpened", false),
            nexusContactUnlocked = json.optBoolean("nexusContactUnlocked", false),
            activeMissionId = json.optString("activeMissionId")
                .takeIf { it.isNotBlank() && it != "null" },
            completedMissionIds = json.stringSet("completedMissionIds"),
            rank = json.optString("rank", "CIVILIAN"),
            experience = json.optInt("experience", 0).coerceAtLeast(0),
            clearance = json.optInt("clearance", 0).coerceAtLeast(0),
            unlockedApps = json.stringSet("unlockedApps").ifEmpty {
                ProgressionRules.unlockedAppsForStoryBeat(json.optInt("storyBeat", 0))
            },
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
}
