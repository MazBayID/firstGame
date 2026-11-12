package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.*
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules
import org.json.JSONArray
import org.json.JSONObject

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
        put("savedCode", JSONObject().apply { state.savedCode.forEach { (k, v) -> put(k, v) } })
        put("caseProgress", JSONObject().apply {
            state.caseProgress.toSortedMap().forEach { (caseId, progress) ->
                put(caseId, JSONObject().apply {
                    put("caseId", progress.caseId)
                    put("briefingAcknowledged", progress.briefingAcknowledged)
                    put("discoveredEvidenceIds", JSONArray(progress.discoveredEvidenceIds.sorted()))
                    put("analyzedEvidenceIds", JSONArray(progress.analyzedEvidenceIds.sorted()))
                    put("selectedDecisionId", progress.selectedDecisionId)
                    put("debriefViewed", progress.debriefViewed)
                })
            }
        })
        put("evidence", JSONArray().apply {
            state.evidence.forEach { e ->
                put(JSONObject().apply {
                    put("id", e.id); put("title", e.title); put("summary", e.summary)
                    put("source", e.source); put("status", e.status.name); put("missionId", e.missionId)
                    put("recordedAtEpochMillis", e.recordedAtEpochMillis)
                    put("provenance", JSONObject().apply {
                        put("origin", e.provenance.origin.name); put("sourceLabel", e.provenance.sourceLabel)
                        put("sourceAssetId", e.provenance.sourceAssetId); put("sourcePath", e.provenance.sourcePath)
                        put("collectedAtEpochMillis", e.provenance.collectedAtEpochMillis)
                        put("collectorId", e.provenance.collectorId)
                    })
                    put("relations", JSONArray().apply {
                        e.relations.forEach { r -> put(JSONObject().apply {
                            put("relatedEvidenceId", r.relatedEvidenceId); put("type", r.type.name); put("note", r.note)
                        }) }
                    })
                    put("reviewHistory", JSONArray().apply {
                        e.reviewHistory.forEach { r -> put(JSONObject().apply {
                            put("previousStatus", r.previousStatus.name); put("newStatus", r.newStatus.name)
                            put("reviewerId", r.reviewerId); put("reason", r.reason)
                            put("reviewedAtEpochMillis", r.reviewedAtEpochMillis)
                        }) }
                    })
                })
            }
        })
    }

    fun decode(json: JSONObject): GameState {
        val version = json.optInt("schemaVersion", 0)
        require(version in 1..GameState.CURRENT_SCHEMA_VERSION) { "Unsupported save schema: $version" }
        val codeJson = json.optJSONObject("savedCode") ?: JSONObject()
        val code = linkedMapOf<String, String>()
        codeJson.keys().forEach { code[it] = codeJson.optString(it, "") }
        val caseProgressJson = json.optJSONObject("caseProgress") ?: JSONObject()
        val caseProgress = linkedMapOf<String, CaseProgress>()
        caseProgressJson.keys().forEach { key ->
            val item = caseProgressJson.optJSONObject(key) ?: return@forEach
            val caseId = item.optString("caseId", key)
            if (caseId.isBlank() || caseId != key) return@forEach
            caseProgress[caseId] = CaseProgress(
                caseId = caseId,
                briefingAcknowledged = item.optBoolean("briefingAcknowledged", false),
                discoveredEvidenceIds = item.stringSet("discoveredEvidenceIds"),
                analyzedEvidenceIds = item.stringSet("analyzedEvidenceIds"),
                selectedDecisionId = item.optString("selectedDecisionId").takeIf { it.isNotBlank() && it != "null" },
                debriefViewed = item.optBoolean("debriefViewed", false)
            )
        }
        val evidenceJson = json.optJSONArray("evidence") ?: JSONArray()
        val evidence = buildList {
            for (i in 0 until evidenceJson.length()) {
                val item = evidenceJson.optJSONObject(i) ?: continue
                val id = item.optString("id"); val title = item.optString("title")
                val summary = item.optString("summary"); val source = item.optString("source")
                val mission = item.optString("missionId")
                if (listOf(id, title, summary, source, mission).any { it.isBlank() }) continue
                val recorded = item.optLong("recordedAtEpochMillis", 0L).coerceAtLeast(0L)
                val provenanceJson = item.optJSONObject("provenance")
                val provenance = EvidenceProvenance(
                    origin = provenanceJson?.optString("origin")?.toEnum(EvidenceOrigin.LEGACY) ?: EvidenceOrigin.LEGACY,
                    sourceLabel = provenanceJson?.optString("sourceLabel").orEmpty().ifBlank { source },
                    sourceAssetId = provenanceJson?.nullableString("sourceAssetId"),
                    sourcePath = provenanceJson?.nullableString("sourcePath"),
                    collectedAtEpochMillis = (provenanceJson?.optLong("collectedAtEpochMillis", recorded) ?: recorded).coerceAtLeast(0L),
                    collectorId = provenanceJson?.nullableString("collectorId")
                )
                val relations = buildList {
                    val a = item.optJSONArray("relations") ?: JSONArray()
                    for (j in 0 until a.length()) {
                        val r = a.optJSONObject(j) ?: continue
                        val target = r.optString("relatedEvidenceId")
                        if (target.isNotBlank() && target != id) add(EvidenceRelation(
                            target, r.optString("type").toEnum(EvidenceRelationType.DERIVED_FROM), r.optString("note")
                        ))
                    }
                }.distinct()
                val reviews = buildList {
                    val a = item.optJSONArray("reviewHistory") ?: JSONArray()
                    for (j in 0 until a.length()) {
                        val r = a.optJSONObject(j) ?: continue
                        val previous = r.optString("previousStatus").toEnumOrNull<EvidenceStatus>() ?: continue
                        val next = r.optString("newStatus").toEnumOrNull<EvidenceStatus>() ?: continue
                        val reviewer = r.optString("reviewerId"); val reason = r.optString("reason")
                        if (reviewer.isBlank() || reason.isBlank() || previous == next) continue
                        add(EvidenceReviewEntry(previous, next, reviewer, reason, r.optLong("reviewedAtEpochMillis", 0L).coerceAtLeast(0L)))
                    }
                }
                add(EvidenceRecord(
                    id, title, summary, source,
                    item.optString("status").toEnum(EvidenceStatus.UNVERIFIED),
                    mission, recorded, provenance, relations, reviews
                ))
            }
        }
        return GameState(
            schemaVersion = version,
            campaignId = json.optString("campaignId", "campaign_01_recruitment"),
            storyBeat = json.optInt("storyBeat", 0).coerceAtLeast(0),
            recruitmentNotificationOpened = json.optBoolean("recruitmentNotificationOpened", json.optInt("storyBeat", 0) >= 2),
            freyaContactOpened = json.optBoolean("freyaContactOpened", false),
            nexusContactUnlocked = json.optBoolean("nexusContactUnlocked", false),
            activeMissionId = json.optString("activeMissionId").takeIf { it.isNotBlank() && it != "null" },
            completedMissionIds = json.stringSet("completedMissionIds"),
            rank = json.optString("rank", "CIVILIAN"),
            experience = json.optInt("experience", 0).coerceAtLeast(0),
            clearance = json.optInt("clearance", 0).coerceAtLeast(0),
            unlockedApps = json.stringSet("unlockedApps") + ProgressionRules.unlockedAppsForStoryBeat(json.optInt("storyBeat", 0)),
            evidence = evidence, savedCode = code,
            lastCheckpoint = json.optString("lastCheckpoint", "recovered"),
            updatedAtEpochMillis = json.optLong("updatedAtEpochMillis", 0L)
        )
    }

    private fun JSONObject.stringSet(key: String): Set<String> {
        val a = optJSONArray(key) ?: return emptySet()
        return buildSet { for (i in 0 until a.length()) add(a.optString(i)) }
    }
    private fun JSONObject.nullableString(key: String): String? =
        optString(key).takeIf { it.isNotBlank() && it != "null" }
    private inline fun <reified T : Enum<T>> String.toEnum(fallback: T): T =
        runCatching { enumValueOf<T>(this) }.getOrDefault(fallback)
    private inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? =
        runCatching { enumValueOf<T>(this) }.getOrNull()
}
