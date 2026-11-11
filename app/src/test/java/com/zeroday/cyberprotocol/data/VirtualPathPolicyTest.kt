package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VirtualPathPolicyTest {
    private val personal = "/A.E.G.I.S/personal"

    @Test
    fun resolvesPathsInsidePersonalWorkspace() {
        assertEquals("$personal/notes.txt", VirtualPathPolicy.resolve("notes.txt", personal, personal))
        assertEquals("$personal/scripts", VirtualPathPolicy.resolve("scripts", personal, personal))
        assertEquals("$personal/logs/today.txt", VirtualPathPolicy.resolve("logs/today.txt", personal, personal))
    }

    @Test
    fun rejectsRootAndMissionDirectoryAccess() {
        assertNull(VirtualPathPolicy.resolve("/", personal, personal))
        assertNull(VirtualPathPolicy.resolve("..", personal, personal))
        assertNull(VirtualPathPolicy.resolve("../missions/first_contact", personal, personal))
        assertNull(
            VirtualPathPolicy.resolve(
                "/A.E.G.I.S/missions/first_contact/signal/signal.py",
                personal,
                personal
            )
        )
    }

    @Test
    fun rejectsTraversalAndInvalidWorkspaceRoots() {
        assertNull(VirtualPathPolicy.resolve("../../outside", personal, personal))
        assertNull(VirtualPathPolicy.resolve("notes.txt", personal, "/"))
        assertNull(VirtualPathPolicy.resolve("notes.txt", "/A.E.G.I.S/missions", personal))
    }
}
