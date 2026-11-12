package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.*
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository

class RecordEvidenceUseCase(private val repository: GameStateRepository) {
    fun record(record: EvidenceRecord): GameState {
        val current = repository.load()
        val existing = current.evidence.firstOrNull { it.id == record.id }
        require(existing == null || existing.missionId == record.missionId) {
            "Evidence identifiers cannot be reassigned to another mission."
        }
        val normalized = existing?.let {
            record.copy(
                recordedAtEpochMillis = it.recordedAtEpochMillis,
                status = it.status,
                provenance = it.provenance,
                relations = (it.relations + record.relations).distinct(),
                reviewHistory = it.reviewHistory
            )
        } ?: record
        repository.save(current.copy(
            evidence = current.evidence.filterNot { it.id == record.id } + normalized,
            lastCheckpoint = "evidence_recorded:" + record.id
        ))
        return repository.load()
    }

    fun review(
        evidenceId: String,
        status: EvidenceStatus,
        reviewerId: String,
        reason: String,
        reviewedAtEpochMillis: Long = System.currentTimeMillis()
    ): GameState {
        require(evidenceId.isNotBlank() && reviewerId.isNotBlank() && reason.isNotBlank())
        require(reviewedAtEpochMillis >= 0L)
        val current = repository.load()
        val record = current.evidence.firstOrNull { it.id == evidenceId }
            ?: throw IllegalArgumentException("Evidence record not found.")
        if (record.status == status) return current
        require(status in EvidenceReviewPolicy.allowedTransitions(record.status)) {
            "Unsupported evidence status transition: ${record.status} -> $status"
        }
        val review = EvidenceReviewEntry(record.status, status, reviewerId, reason, reviewedAtEpochMillis)
        repository.save(current.copy(
            evidence = current.evidence.map {
                if (it.id == evidenceId) it.copy(status = status, reviewHistory = it.reviewHistory + review)
                else it
            },
            lastCheckpoint = "evidence_reviewed:" + evidenceId
        ))
        return repository.load()
    }

    fun relate(
        evidenceId: String,
        relatedEvidenceId: String,
        type: EvidenceRelationType,
        note: String = ""
    ): GameState {
        require(evidenceId.isNotBlank() && relatedEvidenceId.isNotBlank() && evidenceId != relatedEvidenceId)
        val current = repository.load()
        val record = current.evidence.firstOrNull { it.id == evidenceId }
            ?: throw IllegalArgumentException("Evidence record not found.")
        require(current.evidence.any { it.id == relatedEvidenceId }) {
            "Related evidence record not found."
        }
        val relation = EvidenceRelation(relatedEvidenceId, type, note)
        if (relation in record.relations) return current
        repository.save(current.copy(
            evidence = current.evidence.map {
                if (it.id == evidenceId) it.copy(relations = it.relations + relation) else it
            },
            lastCheckpoint = "evidence_related:" + evidenceId
        ))
        return repository.load()
    }

}

object EvidenceReviewPolicy {
    fun allowedTransitions(from: EvidenceStatus): Set<EvidenceStatus> = when (from) {
        EvidenceStatus.UNVERIFIED -> setOf(EvidenceStatus.INFERRED, EvidenceStatus.CORROBORATED, EvidenceStatus.CONFIRMED, EvidenceStatus.DISPUTED)
        EvidenceStatus.INFERRED -> setOf(EvidenceStatus.CORROBORATED, EvidenceStatus.CONFIRMED, EvidenceStatus.DISPUTED, EvidenceStatus.RETRACTED)
        EvidenceStatus.CORROBORATED -> setOf(EvidenceStatus.INFERRED, EvidenceStatus.CONFIRMED, EvidenceStatus.DISPUTED, EvidenceStatus.RETRACTED)
        EvidenceStatus.CONFIRMED -> setOf(EvidenceStatus.DISPUTED, EvidenceStatus.RETRACTED)
        EvidenceStatus.DISPUTED -> setOf(EvidenceStatus.INFERRED, EvidenceStatus.CORROBORATED, EvidenceStatus.CONFIRMED, EvidenceStatus.RETRACTED)
        EvidenceStatus.RETRACTED -> emptySet()
    }
}
