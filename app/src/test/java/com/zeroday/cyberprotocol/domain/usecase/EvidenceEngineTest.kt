package com.zeroday.cyberprotocol.domain.usecase

import com.zeroday.cyberprotocol.core.model.*
import com.zeroday.cyberprotocol.domain.repository.GameStateRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class EvidenceEngineTest {
    private class MemoryRepository : GameStateRepository {
        private var state = GameState()
        override fun load() = state
        override fun save(state: GameState) { this.state = state }
        override fun clear() { state = GameState() }
    }
    private fun record(id: String, status: EvidenceStatus = EvidenceStatus.UNVERIFIED) =
        EvidenceRecord(id, "Evidence $id", "A fictional observation.", "A.E.G.I.S.", status, "case.test")

    @Test fun reviewRequiresReviewerAndReason() {
        val repo = MemoryRepository(); val useCase = RecordEvidenceUseCase(repo); useCase.record(record("a"))
        assertThrows(IllegalArgumentException::class.java) { useCase.review("a", EvidenceStatus.INFERRED, " ", "Reason") }
        assertThrows(IllegalArgumentException::class.java) { useCase.review("a", EvidenceStatus.INFERRED, "reviewer", " ") }
        assertEquals(EvidenceStatus.UNVERIFIED, repo.load().evidence.single().status)
    }
    @Test fun reviewsAppendImmutableHistory() {
        val repo = MemoryRepository(); val useCase = RecordEvidenceUseCase(repo); useCase.record(record("a"))
        useCase.review("a", EvidenceStatus.INFERRED, "analyst:one", "Pattern suggests a link.", 100L)
        useCase.review("a", EvidenceStatus.DISPUTED, "analyst:two", "Conflicting record found.", 200L)
        val saved = repo.load().evidence.single()
        assertEquals(EvidenceStatus.DISPUTED, saved.status)
        assertEquals(2, saved.reviewHistory.size)
        assertEquals(EvidenceStatus.UNVERIFIED, saved.reviewHistory.first().previousStatus)
        assertEquals(EvidenceStatus.INFERRED, saved.reviewHistory.last().previousStatus)
    }
    @Test fun retractedEvidenceCannotBeReactivated() {
        val repo = MemoryRepository(); val useCase = RecordEvidenceUseCase(repo)
        useCase.record(record("a", EvidenceStatus.DISPUTED))
        useCase.review("a", EvidenceStatus.RETRACTED, "reviewer", "Source invalidated.", 300L)
        assertThrows(IllegalArgumentException::class.java) {
            useCase.review("a", EvidenceStatus.CONFIRMED, "reviewer", "Attempt to revive.", 400L)
        }
    }
    @Test fun repeatedReviewIsIdempotent() {
        val repo = MemoryRepository(); val useCase = RecordEvidenceUseCase(repo); useCase.record(record("a"))
        useCase.review("a", EvidenceStatus.INFERRED, "reviewer", "Initial inference.", 100L)
        val same = useCase.review("a", EvidenceStatus.INFERRED, "reviewer", "Repeat.", 200L)
        assertEquals(1, same.evidence.single().reviewHistory.size)
    }
    @Test fun relationsRequireExistingDistinctRecords() {
        val repo = MemoryRepository(); val useCase = RecordEvidenceUseCase(repo)
        useCase.record(record("a")); useCase.record(record("b"))
        useCase.relate("a", "b", EvidenceRelationType.CONTRADICTS, "Timestamps differ.")
        assertEquals("b", repo.load().evidence.first { it.id == "a" }.relations.single().relatedEvidenceId)
        assertThrows(IllegalArgumentException::class.java) { useCase.relate("a", "missing", EvidenceRelationType.SUPPORTS) }
        assertThrows(IllegalArgumentException::class.java) { useCase.relate("a", "a", EvidenceRelationType.SUPPORTS) }
    }
    @Test fun reRecordingCannotEraseReviewOrOriginalProvenance() {
        val repo = MemoryRepository(); val useCase = RecordEvidenceUseCase(repo); useCase.record(record("a"))
        useCase.review("a", EvidenceStatus.INFERRED, "reviewer", "Reviewed.", 100L)
        val original = repo.load().evidence.single()
        useCase.record(original.copy(summary = "Updated summary.", source = "Different incoming label."))
        val saved = repo.load().evidence.single()
        assertEquals(original.provenance, saved.provenance)
        assertEquals(original.reviewHistory, saved.reviewHistory)
        assertEquals(EvidenceStatus.INFERRED, saved.status)
    }
}
