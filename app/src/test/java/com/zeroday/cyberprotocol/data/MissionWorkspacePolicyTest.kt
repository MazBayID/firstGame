package com.zeroday.cyberprotocol.data

import org.junit.Assert.assertEquals
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
    fun pythonInvocationAcceptsOnlySimpleMissionScriptNames() {
        assertEquals("python signal.py", MissionWorkspacePolicy.pythonCommand("signal.py"))
        assertEquals("python audit-v2.py", MissionWorkspacePolicy.pythonCommand("audit-v2.py"))
        assertFalse(MissionWorkspacePolicy.pythonCommand("../outside.py") != null)
        assertFalse(MissionWorkspacePolicy.pythonCommand("signal.py;rm") != null)
        assertFalse(MissionWorkspacePolicy.pythonCommand("my script.py") != null)
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
    fun requiredMissionAssetsMustBePresentBeforeTheExerciseCanRun() {
        val required = setOf("message_fragment.txt")
        assertFalse(MissionWorkspacePolicy.hasRequiredAssets(required, emptySet()))
        assertFalse(MissionWorkspacePolicy.hasRequiredAssets(required, setOf("signal.py")))
        assertTrue(MissionWorkspacePolicy.hasRequiredAssets(required, setOf("message_fragment.txt", "signal.py")))
        assertTrue(MissionWorkspacePolicy.hasRequiredAssets(emptySet(), emptySet()))
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
