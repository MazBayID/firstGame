package com.zeroday.cyberprotocol.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MissionSubmissionPolicyTest {
    @Test
    fun acceptsOnlyTheExactSourceThatRanSuccessfully() {
        val source = "signal = \"ACCESS GRANTED\"\nprint(signal)"

        assertTrue(MissionSubmissionPolicy.canSubmit(source, source))
        assertFalse(MissionSubmissionPolicy.canSubmit("", source))
        assertFalse(MissionSubmissionPolicy.canSubmit(source, null))
        assertFalse(MissionSubmissionPolicy.canSubmit(source + "\n", source))
        assertFalse(MissionSubmissionPolicy.canSubmit(source, "print('different code')"))
    }
}
