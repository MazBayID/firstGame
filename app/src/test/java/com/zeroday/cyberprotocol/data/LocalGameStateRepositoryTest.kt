package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LocalGameStateRepositoryTest {
    private fun freshContext(): Context {
        val context = RuntimeEnvironment.getApplication()
        context.getSharedPreferences("cp_game_state_v1", Context.MODE_PRIVATE)
            .edit().clear().commit()
        context.getSharedPreferences("cyber_protocol_campaign_v2", Context.MODE_PRIVATE)
            .edit().clear().commit()
        return context
    }

    @Test
    fun saveAndLoadPreservesCampaignCodeAndEvidence() {
        val context = freshContext()
        val repository = LocalGameStateRepository(context)
        val state = GameState(
            storyBeat = 4,
            recruitmentNotificationOpened = true,
            activeMissionId = "first_contact.signal",
            unlockedApps = setOf("MESSENGER", "DATABASE", "DOSSIER", "BROWSER", "TERMINAL"),
            savedCode = mapOf("story_4" to "signal = \"ACCESS GRANTED\"\nprint(signal)"),
            evidence = listOf(
                EvidenceRecord(
                    id = "lead-1",
                    title = "Transmission Lead",
                    summary = "Two receipt records differ.",
                    source = "A.E.G.I.S. / Archive",
                    status = EvidenceStatus.UNVERIFIED,
                    missionId = "first_contact.signal",
                    recordedAtEpochMillis = 100L
                )
            ),
            lastCheckpoint = "signal_workspace_open"
        )

        repository.save(state)
        val loaded = repository.load()

        assertEquals(state.storyBeat, loaded.storyBeat)
        assertEquals(state.activeMissionId, loaded.activeMissionId)
        assertEquals(state.unlockedApps, loaded.unlockedApps)
        assertEquals(state.savedCode, loaded.savedCode)
        assertEquals(state.evidence, loaded.evidence)
        assertEquals(state.lastCheckpoint, loaded.lastCheckpoint)
        assertEquals(GameState.CURRENT_SCHEMA_VERSION, loaded.schemaVersion)
    }

    @Test
    fun damagedCurrentSnapshotRecoversPreviousGoodSnapshot() {
        val context = freshContext()
        val repository = LocalGameStateRepository(context)
        val preferences = context.getSharedPreferences("cp_game_state_v1", Context.MODE_PRIVATE)
        repository.save(GameState(storyBeat = 3, lastCheckpoint = "good_snapshot"))
        repository.save(GameState(storyBeat = 4, lastCheckpoint = "newer_snapshot"))
        preferences.edit().putString("snapshot", "{broken-json").commit()

        val recovered = repository.load()

        assertEquals(3, recovered.storyBeat)
        assertEquals("recovered_previous_snapshot", recovered.lastCheckpoint)
        assertEquals("{broken-json", preferences.getString("corrupt_snapshot_backup", null))
    }

    @Test
    fun missingSnapshotMigratesLegacyStoryStage() {
        val context = freshContext()
        context.getSharedPreferences("cyber_protocol_campaign_v2", Context.MODE_PRIVATE)
            .edit().putInt("story_stage", 5).commit()

        val migrated = LocalGameStateRepository(context).load()

        assertEquals(5, migrated.storyBeat)
        assertTrue("first_contact.signal" in migrated.completedMissionIds)
        assertEquals("legacy_migration_story_beat_5", migrated.lastCheckpoint)
    }

    @Test
    fun oldExpandedChapterSnapshotMigratesToCurrentStoryBeat() {
        val context = freshContext()
        val oldState = GameState(schemaVersion = 4, storyBeat = 14, lastCheckpoint = "old_chapter_complete")
        context.getSharedPreferences("cp_game_state_v1", Context.MODE_PRIVATE)
            .edit().putString("snapshot", GameStateCodec.encode(oldState).toString()).commit()

        val migrated = LocalGameStateRepository(context).load()

        assertEquals(GameState.CURRENT_SCHEMA_VERSION, migrated.schemaVersion)
        assertEquals(13, migrated.storyBeat)
        assertEquals("expanded_first_contact_missions_available", migrated.lastCheckpoint)
    }
}
