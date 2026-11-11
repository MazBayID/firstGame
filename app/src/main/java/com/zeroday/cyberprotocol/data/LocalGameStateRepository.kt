package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
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

        val current = runCatching { GameStateCodec.decode(JSONObject(raw)) }.getOrNull()
        if (current != null) {
            if (current.schemaVersion < GameState.CURRENT_SCHEMA_VERSION) {
                val migratedBeat = LegacyProgressMigration.migrateExpandedChapterBeat(
                    current.schemaVersion, current.storyBeat
                )
                val upgraded = current.copy(
                    schemaVersion = GameState.CURRENT_SCHEMA_VERSION,
                    storyBeat = migratedBeat,
                    lastCheckpoint = if (migratedBeat != current.storyBeat) {
                        "expanded_first_contact_missions_available"
                    } else current.lastCheckpoint
                )
                save(upgraded)
                return upgraded
            }
            return current
        }

        // Preserve the damaged snapshot for diagnostics before attempting recovery.
        preferences.edit().putString(KEY_CORRUPT_BACKUP, raw).apply()
        val previousRaw = preferences.getString(KEY_PREVIOUS_SNAPSHOT, null)
        val previous = previousRaw?.let { runCatching { GameStateCodec.decode(JSONObject(it)) }.getOrNull() }
        if (previous != null) {
            val recovered = previous.copy(
                schemaVersion = GameState.CURRENT_SCHEMA_VERSION,
                lastCheckpoint = "recovered_previous_snapshot"
            )
            preferences.edit().putString(KEY_SNAPSHOT, GameStateCodec.encode(recovered).toString()).apply()
            return recovered
        }

        val migrated = migrateLegacyProgress().copy(lastCheckpoint = "recovered_legacy_progress")
        preferences.edit().putString(KEY_SNAPSHOT, GameStateCodec.encode(migrated).toString()).apply()
        return migrated
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
        val encoded = GameStateCodec.encode(normalized).toString()
        val previous = preferences.getString(KEY_SNAPSHOT, null)
        val editor = preferences.edit()
        if (!previous.isNullOrBlank() && previous != encoded) {
            editor.putString(KEY_PREVIOUS_SNAPSHOT, previous)
        }
        editor.putString(KEY_SNAPSHOT, encoded).apply()
    }

    @Synchronized
    override fun clear() {
        preferences.edit()
            .remove(KEY_SNAPSHOT)
            .remove(KEY_PREVIOUS_SNAPSHOT)
            .remove(KEY_CORRUPT_BACKUP)
            .apply()
    }

    private fun migrateLegacyProgress(): GameState {
        val legacy = runCatching {
            contextPreferencesLegacy()
        }.getOrDefault(0)
        return LegacyProgressMigration.fromStoryBeat(legacy)
    }

    private fun contextPreferencesLegacy(): Int {
        // Legacy key used by the original opening-slice implementation.
        return appContext.getSharedPreferences("cyber_protocol_campaign_v2", Context.MODE_PRIVATE)
            .getInt("story_stage", 0)
    }

    companion object {
        private const val STORE_NAME = "cp_game_state_v1"
        private const val KEY_SNAPSHOT = "snapshot"
        private const val KEY_PREVIOUS_SNAPSHOT = "previous_snapshot"
        private const val KEY_CORRUPT_BACKUP = "corrupt_snapshot_backup"
    }
}
