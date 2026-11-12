package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.GameLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class VirtualFileSystemMissionAssetTest {
    private fun freshContext(): Context {
        val context = RuntimeEnvironment.getApplication()
        context.getSharedPreferences("cp_virtual_fs_v1", Context.MODE_PRIVATE)
            .edit().clear().commit()
        return context
    }

    @Test
    fun browserAndMessengerDownloadsShareOnePersistentReadOnlyMissionFile() {
        val context = freshContext()
        val mission = Campaign01Catalog.find("first_contact.signal")!!
        val firstWorkspace = VirtualFileSystem(context).openMissionWorkspace(mission)

        assertTrue(firstWorkspace.downloadAsset("message_fragment.txt"))
        assertEquals("ACCESS GRANTED\n", firstWorkspace.readFile("message_fragment.txt"))
        assertFalse(firstWorkspace.writeFile("message_fragment.txt", "TAMPERED"))
        assertTrue(firstWorkspace.writeFile("signal.py", "signal = \"ACCESS GRANTED\"\nprint(signal)"))

        val restoredWorkspace = VirtualFileSystem(context).openMissionWorkspace(mission)
        assertEquals("ACCESS GRANTED\n", restoredWorkspace.readFile("message_fragment.txt"))
        assertEquals("signal = \"ACCESS GRANTED\"\nprint(signal)", restoredWorkspace.readFile("signal.py"))
        assertEquals(
            "ACCESS GRANTED\n",
            VirtualFileSystem(context).execute(
                "cat message_fragment.txt",
                mission.workspaceDirectory,
                mission.workspaceDirectory,
                mission.allowedFiles,
                mission.writableFiles
            ).output
        )
        assertEquals(
            "PYTHON_LAUNCH:signal.py",
            VirtualFileSystem(context).execute(
                "python signal.py",
                mission.workspaceDirectory,
                mission.workspaceDirectory,
                mission.allowedFiles,
                mission.writableFiles
            ).output
        )
    }

    @Test
    fun identityVerificationMissionDownloadsAnExactReadOnlyMessageFragment() {
        val context = freshContext()
        val mission = Campaign01Catalog.find("first_contact.identity")!!
        val workspace = VirtualFileSystem(context).openMissionWorkspace(mission)

        assertTrue(workspace.downloadAsset("identity_fragment.txt"))
        assertEquals("IDENTITY VERIFIED\n", workspace.readFile("identity_fragment.txt"))
        assertFalse(workspace.writeFile("identity_fragment.txt", "TAMPERED"))
        assertEquals(
            "IDENTITY VERIFIED\n",
            VirtualFileSystem(context).execute(
                "cat identity_fragment.txt", mission.workspaceDirectory,
                mission.workspaceDirectory, mission.allowedFiles, mission.writableFiles
            ).output
        )
    }

    @Test
    fun firstContactSourceAssetsAreSharedByMessengerBrowserAndTerminalAndPersist() {
        val context = freshContext()
        val fileSystem = VirtualFileSystem(context)
        val aegis = EvidenceAssetCatalog.assets.first()
        val archive = EvidenceAssetCatalog.assets.last()

        assertEquals(null, fileSystem.readEvidenceAsset(aegis.id))
        fileSystem.installEvidenceAssets()

        assertEquals(aegis.content, fileSystem.readEvidenceAsset(aegis.id))
        assertEquals(archive.content, fileSystem.readEvidenceAsset(archive.id))
        assertEquals(
            aegis.content,
            VirtualFileSystem(context).execute(
                "cat ${aegis.path}", fileSystem.initialDirectory()
            ).output
        )
        assertEquals(aegis.content, VirtualFileSystem(context).readEvidenceAsset(aegis.id))
        assertEquals(
            aegis,
            EvidenceAssetCatalog.sourceAssetReadByTerminal("cat ${aegis.path}", aegis.content)
        )
        assertEquals(null, EvidenceAssetCatalog.sourceAssetReadByTerminal(
            "cat ${aegis.path}", "File tidak ditemukan"
        ))
        assertEquals(null, EvidenceAssetCatalog.sourceAssetReadByTerminal(
            "ls ${aegis.path}", aegis.content
        ))
    }

    @Test
    fun terminalHelpAndIdentityRespectSelectedLanguage() {
        val fileSystem = VirtualFileSystem(freshContext())
        val cwd = fileSystem.initialDirectory()

        val englishHelp = fileSystem.execute("help", cwd, language = GameLanguage.ENGLISH).output
        val indonesianHelp = fileSystem.execute("help", cwd, language = GameLanguage.INDONESIAN).output
        assertTrue(englishHelp.contains("SUPPORTED COMMANDS"))
        assertTrue(englishHelp.contains("isolated A.E.G.I.S. virtual environment"))
        assertTrue(indonesianHelp.contains("DAFTAR PERINTAH"))
        assertTrue(indonesianHelp.contains("lingkungan virtual A.E.G.I.S."))

        assertTrue(fileSystem.execute("whoami", cwd, language = GameLanguage.ENGLISH)
            .output.contains("TRAINEE"))
        assertTrue(fileSystem.execute("whoami", cwd, language = GameLanguage.INDONESIAN)
            .output.contains("REKRUT"))

        val unknownEnglish = fileSystem.execute("unknown-command", cwd, language = GameLanguage.ENGLISH).output
        val unknownIndonesian = fileSystem.execute("unknown-command", cwd, language = GameLanguage.INDONESIAN).output
        assertTrue(unknownEnglish.startsWith("Unknown command:"))
        assertTrue(unknownIndonesian.startsWith("Perintah tidak dikenal:"))
        assertEquals("(empty)", fileSystem.execute("ls", cwd, language = GameLanguage.ENGLISH).output)
        assertEquals("(kosong)", fileSystem.execute("ls", cwd, language = GameLanguage.INDONESIAN).output)

        val mission = Campaign01Catalog.find("first_contact.signal")!!
        val workspace = fileSystem.openMissionWorkspace(mission)
        workspace.downloadAsset("message_fragment.txt")
        val deniedEnglish = fileSystem.execute(
            "rm message_fragment.txt", mission.workspaceDirectory,
            mission.workspaceDirectory, mission.allowedFiles, mission.writableFiles,
            GameLanguage.ENGLISH
        ).output
        assertTrue(deniedEnglish.startsWith("Permission denied:"))
        val deniedIndonesian = fileSystem.execute(
            "rm message_fragment.txt", mission.workspaceDirectory,
            mission.workspaceDirectory, mission.allowedFiles, mission.writableFiles,
            GameLanguage.INDONESIAN
        ).output
        assertTrue(deniedIndonesian.startsWith("Akses ditolak:"))
    }

    @Test
    fun missionTerminalCannotDeleteOrReadOutsideAuthorizedFiles() {
        val context = freshContext()
        val mission = Campaign01Catalog.find("first_contact.signal")!!
        val workspace = VirtualFileSystem(context).openMissionWorkspace(mission)
        workspace.downloadAsset("message_fragment.txt")

        val removeReadOnly = VirtualFileSystem(context).execute(
            "rm message_fragment.txt",
            mission.workspaceDirectory,
            mission.workspaceDirectory,
            mission.allowedFiles,
            mission.writableFiles
        )
        val outsideRead = VirtualFileSystem(context).readFile(
            "../personal/private.txt",
            mission.workspaceDirectory,
            mission.workspaceDirectory,
            mission.allowedFiles
        )

        assertTrue(removeReadOnly.output.startsWith("Akses ditolak:"))
        assertEquals(null, outsideRead)
    }
}
