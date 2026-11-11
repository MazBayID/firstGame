package com.zeroday.cyberprotocol.data

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.usecase.ProgressionRules

/** Converts the previous story_stage preference into the versioned campaign model. */
internal object LegacyProgressMigration {
    /** Reopens the two new follow-up missions for players who completed the previous 12-beat chapter. */
    fun migrateExpandedChapterBeat(schemaVersion: Int, storyBeat: Int): Int =
        if (schemaVersion < 5 && storyBeat >= 14) 13 else storyBeat

    fun fromStoryBeat(rawStoryBeat: Int): GameState {
        val beat = rawStoryBeat.coerceIn(0, 7)
        val completed = buildSet {
            if (beat >= 5) add("first_contact.signal")
            if (beat >= 6) add("first_contact.identity")
        }
        val experience = (if (beat >= 5) 25 else 0) + (if (beat >= 6) 35 else 0)
        val evidence = buildList {
            if (beat >= 5) add(
                EvidenceRecord(
                    id = "evidence_first_contact.signal",
                    title = "Sinyal Pertama",
                    summary = "Hasil tugas latihan berhasil diverifikasi oleh evaluator A.E.G.I.S.",
                    source = "NEXUS / Python evaluator 9",
                    status = EvidenceStatus.CONFIRMED,
                    missionId = "first_contact.signal"
                )
            )
            if (beat >= 6) add(
                EvidenceRecord(
                    id = "evidence_first_contact.identity",
                    title = "Verifikasi Identitas",
                    summary = "Hasil tugas latihan berhasil diverifikasi oleh evaluator A.E.G.I.S.",
                    source = "NEXUS / Python evaluator 10",
                    status = EvidenceStatus.CONFIRMED,
                    missionId = "first_contact.identity"
                )
            )
            if (beat >= 7) add(
                EvidenceRecord(
                    id = "transmission_timestamp_discrepancy",
                    title = "Perbedaan Waktu Penerimaan Transmisi",
                    summary = "Dua sistem internal mencatat waktu penerimaan pesan yang berbeda. Perbedaan teramati, tetapi penyebabnya belum diketahui.",
                    source = "NEXUS / perbandingan metadata tanda terima",
                    status = EvidenceStatus.UNVERIFIED,
                    missionId = "first_contact.recruitment"
                )
            )
        }
        return GameState(
            storyBeat = beat,
            recruitmentNotificationOpened = beat >= 2,
            freyaContactOpened = false,
            nexusContactUnlocked = false,
            completedMissionIds = completed,
            rank = ProgressionRules.rankForExperience(experience),
            experience = experience,
            clearance = if (beat >= 7) 1 else 0,
            unlockedApps = ProgressionRules.unlockedAppsForStoryBeat(beat),
            evidence = evidence,
            lastCheckpoint = "legacy_migration_story_beat_$beat"
        )
    }
}
