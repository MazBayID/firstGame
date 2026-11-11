package com.zeroday.cyberprotocol.domain.usecase

/** Submission is allowed only for the exact source that most recently completed RUN successfully. */
object MissionSubmissionPolicy {
    fun canSubmit(currentSource: String, lastSuccessfulRunSource: String?): Boolean =
        currentSource.isNotBlank() && lastSuccessfulRunSource == currentSource
}
