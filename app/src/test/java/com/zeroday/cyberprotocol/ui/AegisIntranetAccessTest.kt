package com.zeroday.cyberprotocol.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AegisIntranetAccessTest {
    @Test
    fun acceptsOnlyTheFictionalRecruitmentCredentials() {
        assertTrue(AegisIntranetAccess.authenticate("RECRUIT-014", "FIRSTCONTACT"))
        assertTrue(AegisIntranetAccess.authenticate(" recruit-014 ", "firstcontact"))
        assertFalse(AegisIntranetAccess.authenticate("RECRUIT-014", "wrong"))
        assertFalse(AegisIntranetAccess.authenticate("other-agent", "FIRSTCONTACT"))
        assertFalse(AegisIntranetAccess.authenticate("", ""))
    }
}
