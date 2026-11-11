package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MissionWorkspacePolicyTest {
    private val allowed = setOf("briefing.txt", "signal.py")
    private val writable = setOf("signal.py")

    @Test
    fun readAccessIsLimitedToDeclaredMissionFiles() {
        assertTrue(MissionWorkspacePolicy.canRead("briefing.txt", allowed))
        assertTrue(MissionWorkspacePolicy.canRead("signal.py", allowed))
        assertFalse(MissionWorkspacePolicy.canRead("private.txt", allowed))
        assertFalse(MissionWorkspacePolicy.canRead("../outside.txt", allowed))
    }

    @Test
    fun repositoryAssetsCanOnlyBeInstalledAsDeclaredReadOnlyFiles() {
        val allowed = setOf("briefing.txt", "message_fragment.txt", "signal.py")
        val writable = setOf("signal.py")

        assertTrue(MissionWorkspacePolicy.canInstallAsset("message_fragment.txt", 13, allowed, writable))
        assertFalse(MissionWorkspacePolicy.canInstallAsset("signal.py", 13, allowed, writable))
        assertFalse(MissionWorkspacePolicy.canInstallAsset("private.txt", 13, allowed, writable))
        assertFalse(MissionWorkspacePolicy.canInstallAsset("../outside.txt", 13, allowed, writable))
        assertFalse(MissionWorkspacePolicy.canInstallAsset("message_fragment.txt", 50_001, allowed, writable))
    }

    @Test
    fun writesRequireExplicitPermissionAndRespectSizeLimit() {
        assertTrue(MissionWorkspacePolicy.canWrite("signal.py", 0, writable))
        assertTrue(MissionWorkspacePolicy.canWrite("signal.py", 50_000, writable))
        assertFalse(MissionWorkspacePolicy.canWrite("signal.py", 50_001, writable))
        assertFalse(MissionWorkspacePolicy.canWrite("briefing.txt", 12, writable))
        assertFalse(MissionWorkspacePolicy.canWrite("../outside.py", 12, writable))
    }
}
