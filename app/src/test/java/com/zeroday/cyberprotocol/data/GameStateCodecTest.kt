package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateCodecTest {
    @Test
    fun roundTripPreservesCampaignAndEvidenceState() {
        val state = GameState(
            storyBeat = 16,
            recruitmentNotificationOpened = true,
            freyaContactOpened = true,
            nexusContactUnlocked = true,
            activeMissionId = "first_contact.validate_delta",
            completedMissionIds = setOf("first_contact.signal", "first_contact.validate_delta"),
            rank = "ANALYST",
            experience = 420,
            clearance = 1,
            unlockedApps = setOf("MESSENGER", "TERMINAL", "DATABASE"),
            evidence = listOf(
                EvidenceRecord(
                    id = "rx14_time_delta",
                    title = "Perbedaan Waktu RX-14",
                    summary = "Dua catatan berbeda empat detik.",
                    source = "A.E.G.I.S. / ARCHIVE",
                    status = EvidenceStatus.UNVERIFIED,
                    missionId = "first_contact.validate_delta",
                    recordedAtEpochMillis = 1_800_000_000_000L
                )
            ),
            savedCode = mapOf("story_16" to "difference = abs(first_time - second_time)"),
            lastCheckpoint = "first_contact_report",
            updatedAtEpochMillis = 1_800_000_000_123L
        )

        val decoded = GameStateCodec.decode(GameStateCodec.encode(state))

        assertEquals(state, decoded)
    }

    @Test
    fun legacySnapshotUsesSafeDefaultsForNewFields() {
        val legacy = JSONObject()
            .put("schemaVersion", 1)
            .put("storyBeat", 3)
            .put("experience", -10)
            .put("clearance", -1)

        val decoded = GameStateCodec.decode(legacy)

        assertTrue(decoded.recruitmentNotificationOpened)
        assertFalse(decoded.freyaContactOpened)
        assertFalse(decoded.nexusContactUnlocked)
        assertEquals(0, decoded.experience)
        assertEquals(0, decoded.clearance)
        assertTrue(decoded.unlockedApps.isNotEmpty())
    }

    @Test
    fun unsupportedFutureSchemaIsRejected() {
        val future = JSONObject().put(
            "schemaVersion",
            GameState.CURRENT_SCHEMA_VERSION + 1
        )

        val error = runCatching { GameStateCodec.decode(future) }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun unknownEvidenceStatusFallsBackToUnverified() {
        val evidence = JSONObject()
            .put("id", "evidence-1")
            .put("title", "Record")
            .put("summary", "A fictional record.")
            .put("source", "A.E.G.I.S.")
            .put("status", "UNKNOWN_STATUS")
            .put("missionId", "first_contact.signal")
            .put("recordedAtEpochMillis", 10L)
        val snapshot = JSONObject()
            .put("schemaVersion", GameState.CURRENT_SCHEMA_VERSION)
            .put("evidence", org.json.JSONArray().put(evidence))

        val decoded = GameStateCodec.decode(snapshot)

        assertEquals(EvidenceStatus.UNVERIFIED, decoded.evidence.single().status)
        assertEquals(10L, decoded.evidence.single().recordedAtEpochMillis)
    }
}
