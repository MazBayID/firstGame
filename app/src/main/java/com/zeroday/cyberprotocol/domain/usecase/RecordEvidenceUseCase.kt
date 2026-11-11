package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.EvidenceRecord
import com.zeroday.cyberprotocol.core.model.EvidenceStatus
import com.zeroday.cyberprotocol.core.model.GameState
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository

/** Stores evidence with an explicit source and mission provenance. */
class RecordEvidenceUseCase(private val repository: GameStateRepository) {
    fun record(record: EvidenceRecord): GameState {
        require(record.id.isNotBlank())
        require(record.title.isNotBlank())
        require(record.summary.isNotBlank())
        require(record.source.isNotBlank())
        require(record.missionId.isNotBlank())

        val current = repository.load()
        val existing = current.evidence.firstOrNull { it.id == record.id }
        require(existing == null || existing.missionId == record.missionId) {
            "Evidence identifiers cannot be reassigned to another mission."
        }
        val normalizedRecord = existing?.let {
            record.copy(recordedAtEpochMillis = it.recordedAtEpochMillis)
        } ?: record
        val updated = current.copy(
            evidence = current.evidence.filterNot { it.id == record.id } + normalizedRecord,
            lastCheckpoint = "evidence_recorded:" + record.id
        )
        repository.save(updated)
        return repository.load()
    }

    fun updateStatus(evidenceId: String, status: EvidenceStatus): GameState {
        require(evidenceId.isNotBlank())
        val current = repository.load()
        require(current.evidence.any { it.id == evidenceId }) { "Evidence record not found." }
        val updated = current.copy(
            evidence = current.evidence.map {
                if (it.id == evidenceId) it.copy(status = status) else it
            },
            lastCheckpoint = "evidence_status_updated:" + evidenceId
        )
        repository.save(updated)
        return repository.load()
    }
}
