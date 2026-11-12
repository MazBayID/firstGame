package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.MissionDefinition
import com.zeroday.cyberprotocol.core.model.GameLanguage
import org.json.JSONArray
import org.json.JSONObject

data class TerminalResponse(
    val output: String,
    val currentDirectory: String,
    val clearScreen: Boolean = false
)

/** Persistent game-owned filesystem. It never maps paths to Android storage. */
class VirtualFileSystem(context: Context) {
    private val preferences = context.getSharedPreferences("cp_virtual_fs_v1", Context.MODE_PRIVATE)
    private val root = "/A.E.G.I.S"
    private val directories = linkedSetOf(
        root, "$root/personal", "$root/missions", "$root/missions/first_contact",
        "$root/evidence", "$root/database", "$root/scripts"
    )
    private val files = linkedMapOf<String, String>()

    init {
        restore()
        if (!files.containsKey("$root/evidence/readme.txt")) {
            files["$root/evidence/readme.txt"] =
                "A.E.G.I.S. TRAINING ARCHIVE\nEvidence in this environment is fictional and mission-scoped.\n"
        }
        persist()
    }

    fun initialDirectory(): String = "$root/personal"

    /** Installs the approved First Contact source records without overwriting player data. */
    @Synchronized
    fun installEvidenceAssets(): Boolean {
        val evidenceDirectory = "$root/evidence/first_contact"
        directories.add(evidenceDirectory)
        var changed = false
        EvidenceAssetCatalog.assets.forEach { asset ->
            if (!files.containsKey(asset.path)) {
                files[asset.path] = asset.content
                changed = true
            }
        }
        if (changed) persist()
        return changed
    }

    @Synchronized
    fun readEvidenceAsset(assetId: String): String? {
        val asset = EvidenceAssetCatalog.find(assetId) ?: return null
        return files[asset.path]
    }

    @Synchronized
    fun execute(
        commandLine: String,
        currentDirectory: String,
        workspaceRoot: String = "$root/personal",
        allowedFiles: Set<String>? = null,
        writableFiles: Set<String>? = null,
        language: GameLanguage = GameLanguage.INDONESIAN
    ): TerminalResponse {
        if (commandLine.length > MAX_COMMAND_LENGTH) {
            return TerminalResponse(terminalText(language, "Command exceeds the 12000-character training limit.", "Perintah melebihi batas 12000 karakter untuk latihan."), currentDirectory)
        }
        fun canReadPath(path: String): Boolean =
            allowedFiles == null || (
                parent(path) == workspaceRoot &&
                    MissionWorkspacePolicy.canRead(path.substringAfterLast("/"), allowedFiles)
            )
        fun canWritePath(path: String, contentLength: Int): Boolean =
            writableFiles == null || (
                parent(path) == workspaceRoot &&
                    MissionWorkspacePolicy.canWrite(path.substringAfterLast("/"), contentLength, writableFiles)
            )

        val line = commandLine.trim()
        if (line.isEmpty()) return TerminalResponse("", currentDirectory)
        if (line == "clear") return TerminalResponse("", currentDirectory, clearScreen = true)

        val redirect = Regex("""^echo\s+(['"])(.*?)\1\s*>\s*(\S+)$""").matchEntire(line)
        if (redirect != null) {
            val target = resolve(redirect.groupValues[3], currentDirectory, workspaceRoot)
                ?: return TerminalResponse(terminalText(language, "Invalid path: access is restricted to the A.E.G.I.S. workspace.", "Jalur tidak valid: akses dibatasi ke ruang kerja A.E.G.I.S."), currentDirectory)
            if (redirect.groupValues[2].length > MAX_PERSONAL_FILE_LENGTH) return TerminalResponse(terminalText(language, "File content exceeds the workspace limit.", "Isi file melebihi batas ruang kerja."), currentDirectory)
            if (!canWritePath(target, redirect.groupValues[2].length + 1)) return TerminalResponse(terminalText(language, "Permission denied: this mission file is read-only or outside its allowlist.", "Akses ditolak: file misi hanya-baca atau berada di luar daftar izin."), currentDirectory)
            if (directories.contains(target)) return TerminalResponse(terminalText(language, "Cannot write to a directory.", "Tidak dapat menulis ke sebuah folder."), currentDirectory)
            if (!directories.contains(parent(target))) return TerminalResponse(terminalText(language, "Parent folder not found.", "Folder induk tidak ditemukan."), currentDirectory)
            files[target] = redirect.groupValues[2] + "\n"
            persist()
            return TerminalResponse("", currentDirectory)
        }

        val args = tokenize(line)
        if (args.isEmpty()) return TerminalResponse("", currentDirectory)
        val name = args.first()
        val rest = args.drop(1)
        var cwd = currentDirectory

        val output = when (name) {
            "help" -> if (language == GameLanguage.ENGLISH) """
                SUPPORTED COMMANDS
                pwd                 Show the current directory
                ls [path]           List files and folders
                cd <path>           Change directory
                cat <file>          Read a text file
                mkdir <name>        Create a folder
                touch <file>        Create an empty file
                rm <file>           Remove a file
                echo "text" > file  Write text to a file
                whoami              Show the in-game identity
                clear               Clear the terminal display
                python <file.py>    Run a saved Python script
                help                Show this guide

                This is an isolated A.E.G.I.S. virtual environment, not the Android shell.
            """.trimIndent() else """
                DAFTAR PERINTAH YANG DIDUKUNG
                pwd                 Tampilkan direktori saat ini
                ls [path]           Daftar file dan folder
                cd <path>           Pindah folder
                cat <file>          Baca file teks
                mkdir <name>        Buat folder
                touch <file>        Buat file kosong
                rm <file>           Hapus file
                echo "text" > file  Tulis teks ke file
                whoami              Tampilkan identitas dalam game
                clear               Bersihkan tampilan terminal
                python <file.py>    Jalankan script Python yang tersimpan
                help                Tampilkan panduan ini

                Ini adalah lingkungan virtual A.E.G.I.S. yang terisolasi, bukan shell Android.
            """.trimIndent()
            "pwd" -> cwd
            "whoami" -> if (language == GameLanguage.ENGLISH) "TRAINEE  ·  A.E.G.I.S. TRAINING ENVIRONMENT" else "REKRUT  ·  LINGKUNGAN PELATIHAN A.E.G.I.S."
            "ls" -> {
                val target = resolve(rest.firstOrNull() ?: ".", cwd, workspaceRoot)
                    ?: return TerminalResponse(terminalText(language, "Invalid path.", "Jalur tidak valid."), cwd)
                if (!directories.contains(target)) return TerminalResponse(terminalText(language, "Not a directory: $target", "Bukan folder: $target"), cwd)
                if (allowedFiles != null && target != workspaceRoot) return TerminalResponse(terminalText(language, "Mission workspace does not expose subdirectories.", "Ruang kerja misi tidak menyediakan subfolder."), cwd)
                val childDirs = if (allowedFiles == null) directories.filter { it != target && parent(it) == target }
                    .map { it.substringAfterLast("/") + "/" } else emptyList()
                val childFiles = files.keys.filter { parent(it) == target && canReadPath(it) }
                    .map { it.substringAfterLast("/") }
                (childDirs + childFiles).sorted().joinToString("\n").ifBlank { "(empty)" }
            }
            "cd" -> {
                val target = resolve(rest.firstOrNull() ?: workspaceRoot, cwd, workspaceRoot)
                    ?: return TerminalResponse(terminalText(language, "Invalid path: access outside the virtual workspace is denied.", "Jalur tidak valid: akses keluar dari ruang kerja virtual ditolak."), cwd)
                if (!directories.contains(target)) return TerminalResponse(terminalText(language, "Folder not found: $target", "Folder tidak ditemukan: $target"), cwd)
                if (allowedFiles != null && target != workspaceRoot) return TerminalResponse(terminalText(language, "Mission workspace does not expose subdirectories.", "Ruang kerja misi tidak menyediakan subfolder."), cwd)
                cwd = target
                ""
            }
            "cat" -> {
                val requestedPath = rest.firstOrNull() ?: return TerminalResponse(terminalText(language, "Usage: cat <file>", "Penggunaan: cat <file>"), cwd)
                val sharedEvidenceAsset = if (allowedFiles == null && workspaceRoot == "$root/personal") {
                    EvidenceAssetCatalog.assets.firstOrNull { it.path == requestedPath }
                } else null
                if (sharedEvidenceAsset != null) {
                    files[sharedEvidenceAsset.path]
                        ?: return TerminalResponse(terminalText(language, "Evidence file is not available yet: ${sharedEvidenceAsset.path}", "File bukti belum tersedia: ${sharedEvidenceAsset.path}"), cwd)
                } else {
                    val target = resolve(requestedPath, cwd, workspaceRoot)
                        ?: return TerminalResponse(terminalText(language, "Usage: cat <file>", "Penggunaan: cat <file>"), cwd)
                    if (!canReadPath(target)) return TerminalResponse(terminalText(language, "Permission denied: file is outside this mission workspace.", "Akses ditolak: file berada di luar ruang kerja misi ini."), cwd)
                    files[target] ?: return TerminalResponse(terminalText(language, "File not found: $target", "File tidak ditemukan: $target"), cwd)
                }
            }
            "mkdir" -> {
                if (allowedFiles != null) return TerminalResponse(terminalText(language, "Folder creation is disabled in mission workspaces.", "Pembuatan folder dinonaktifkan di ruang kerja misi."), cwd)
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse(terminalText(language, "Usage: mkdir <name>", "Penggunaan: mkdir <name>"), cwd)
                if (directories.contains(target) || files.containsKey(target)) return TerminalResponse(terminalText(language, "Already exists: $target", "Sudah ada: $target"), cwd)
                if (!directories.contains(parent(target))) return TerminalResponse(terminalText(language, "Parent directory does not exist.", "Folder induk tidak ditemukan."), cwd)
                directories.add(target)
                persist()
                ""
            }
            "touch" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse(terminalText(language, "Usage: touch <file>", "Penggunaan: touch <file>"), cwd)
                if (directories.contains(target)) return TerminalResponse(terminalText(language, "A directory already exists at that path.", "Folder sudah ada pada jalur tersebut."), cwd)
                if (!canWritePath(target, 0)) return TerminalResponse(terminalText(language, "Permission denied: file is read-only or outside its allowlist.", "Akses ditolak: file hanya-baca atau di luar daftar izin."), cwd)
                if (!directories.contains(parent(target))) return TerminalResponse(terminalText(language, "Parent directory does not exist.", "Folder induk tidak ditemukan."), cwd)
                if (!files.containsKey(target)) files[target] = ""
                persist()
                ""
            }
            "rm" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse(terminalText(language, "Usage: rm <file>", "Penggunaan: rm <file>"), cwd)
                if (directories.contains(target)) return TerminalResponse(terminalText(language, "Folder deletion is not available yet. Remove files one at a time.", "Penghapusan folder belum tersedia. Hapus file satu per satu."), cwd)
                if (!canWritePath(target, 0)) return TerminalResponse("Permission denied: file is read-only or outside its allowlist.", cwd)
                if (files.remove(target) == null) return TerminalResponse(terminalText(language, "File not found: $target", "File tidak ditemukan: $target"), cwd)
                persist()
                ""
            }
            "python" -> {
                val script = rest.firstOrNull() ?: return TerminalResponse(terminalText(language, "Usage: python <file.py>", "Penggunaan: python <file.py>"), cwd)
                val target = resolve(script, cwd, workspaceRoot) ?: return TerminalResponse(terminalText(language, "Invalid script path.", "Jalur skrip tidak valid."), cwd)
                if (!canReadPath(target)) return TerminalResponse(terminalText(language, "Permission denied: script is outside this workspace.", "Akses ditolak: skrip berada di luar ruang kerja ini."), cwd)
                if (!files.containsKey(target)) return TerminalResponse(terminalText(language, "File not found: $script", "File tidak ditemukan: $script"), cwd)
                "PYTHON_LAUNCH:" + script
            }
            else -> terminalText(language, "Unknown command: $name\nType 'help' to view the command guide.", "Perintah tidak dikenal: $name\nKetik 'help' untuk melihat daftar perintah.")
        }
        return TerminalResponse(output, cwd)
    }

    @Synchronized
    fun writeFile(
        path: String, content: String, currentDirectory: String,
        workspaceRoot: String = "$root/personal", writableFiles: Set<String>? = null
    ): Boolean {
        val resolved = resolve(path, currentDirectory, workspaceRoot) ?: return false
        if (content.length > MAX_PERSONAL_FILE_LENGTH) return false
        if (writableFiles != null && !MissionWorkspacePolicy.canWrite(resolved.substringAfterLast("/"), content.length, writableFiles)) return false
        if (directories.contains(resolved) || !directories.contains(parent(resolved))) return false
        files[resolved] = content
        persist()
        return true
    }

    @Synchronized
    fun readFile(
        path: String, currentDirectory: String, workspaceRoot: String = "$root/personal",
        allowedFiles: Set<String>? = null
    ): String? {
        val resolved = resolve(path, currentDirectory, workspaceRoot) ?: return null
        if (allowedFiles != null && (parent(resolved) != workspaceRoot || !MissionWorkspacePolicy.canRead(resolved.substringAfterLast("/"), allowedFiles))) return null
        return files[resolved]
    }

    /**
     * Opens only the files declared by an authored mission. The returned handle
     * never exposes arbitrary paths or the Android host filesystem.
     */
    @Synchronized
    fun openMissionWorkspace(definition: MissionDefinition): MissionWorkspace {
        val missionRoot = "$root/missions/"
        require(definition.workspaceDirectory.startsWith(missionRoot)) {
            "Mission workspace must remain inside the virtual missions directory."
        }
        val segments = definition.workspaceDirectory.removePrefix("/").split("/")
        require(segments.all { it.matches(Regex("[A-Za-z0-9_.-]{1,64}")) && it != "." && it != ".." }) {
            "Invalid mission workspace path."
        }
        var path = ""
        segments.forEach { segment ->
            path += "/$segment"
            directories.add(path)
        }
        val briefingPath = "${definition.workspaceDirectory}/briefing.txt"
        if ("briefing.txt" in definition.allowedFiles && !files.containsKey(briefingPath)) {
            files[briefingPath] = "${definition.title.uppercase()}\n\n${definition.briefing}\n"
        }
        persist()
        return MissionWorkspace(
            owner = this,
            directory = definition.workspaceDirectory,
            allowedFiles = definition.allowedFiles,
            writableFiles = definition.writableFiles,
            initialFiles = definition.initialFiles
        )
    }

    @Synchronized
    internal fun installMissionAsset(
        directory: String,
        allowedFiles: Set<String>,
        writableFiles: Set<String>,
        initialFiles: Map<String, String>,
        name: String
    ): Boolean {
        val content = initialFiles[name] ?: return false
        if (!MissionWorkspacePolicy.canInstallAsset(name, content.length, allowedFiles, writableFiles)) return false
        val path = "$directory/$name"
        if (files.containsKey(path)) return true
        files[path] = content
        persist()
        return true
    }

    @Synchronized
    internal fun listMissionFiles(directory: String, allowedFiles: Set<String>): List<String> =
        allowedFiles.filter { files.containsKey("$directory/$it") }.sorted()

    @Synchronized
    internal fun readMissionFile(directory: String, allowedFiles: Set<String>, name: String): String? {
        if (!MissionWorkspacePolicy.canRead(name, allowedFiles)) return null
        return files["$directory/$name"]
    }

    @Synchronized
    internal fun writeMissionFile(
        directory: String,
        writableFiles: Set<String>,
        name: String,
        content: String
    ): Boolean {
        if (!MissionWorkspacePolicy.canWrite(name, content.length, writableFiles)) return false
        files["$directory/$name"] = content
        persist()
        return true
    }

    private fun restore() {
        val raw = preferences.getString("files", null) ?: return
        try {
            val json = JSONObject(raw)
            val fileJson = json.optJSONObject("files") ?: JSONObject()
            val dirJson = json.optJSONArray("directories") ?: JSONArray()
            files.clear()
            fileJson.keys().forEach { key -> files[key] = fileJson.optString(key, "") }
            directories.clear()
            for (index in 0 until dirJson.length()) directories.add(dirJson.getString(index))
            if (!directories.contains(root)) directories.add(root)
            if (!directories.contains("$root/personal")) directories.add("$root/personal")
        } catch (_: Exception) {
            files.clear()
            directories.clear()
            directories.addAll(listOf(root, "$root/personal", "$root/missions",
                "$root/missions/first_contact", "$root/evidence", "$root/database", "$root/scripts"))
        }
    }

    private fun persist() {
        val fileJson = JSONObject()
        files.forEach { (path, content) -> fileJson.put(path, content) }
        val dirJson = JSONArray()
        directories.sorted().forEach(dirJson::put)
        preferences.edit().putString("files", JSONObject()
            .put("files", fileJson).put("directories", dirJson).toString()).apply()
    }

    private fun resolve(input: String, cwd: String, workspaceRoot: String): String? =
        VirtualPathPolicy.resolve(input, cwd, workspaceRoot)

    private fun parent(path: String): String = path.substringBeforeLast("/", "")

    companion object {
        private const val MAX_PERSONAL_FILE_LENGTH = 50_000
        private const val MAX_COMMAND_LENGTH = 12_000
    }
    private fun tokenize(line: String): List<String> =
        Regex(""""([^"]*)"|'([^']*)'|(\S+)""").findAll(line).map { match ->
            match.groups[1]?.value ?: match.groups[2]?.value ?: match.groups[3]?.value.orEmpty()
        }.toList()
    private fun terminalText(language: GameLanguage, english: String, indonesian: String): String =
        if (language == GameLanguage.ENGLISH) english else indonesian
}

/** Narrow capability handle for one mission workspace. */
class MissionWorkspace internal constructor(
    private val owner: VirtualFileSystem,
    val directory: String,
    private val allowedFiles: Set<String>,
    private val writableFiles: Set<String>,
    private val initialFiles: Map<String, String>
) {
    val allowedFileNames: Set<String> get() = allowedFiles.toSet()
    val writableFileNames: Set<String> get() = writableFiles.toSet()
    val downloadableFileNames: Set<String> get() = initialFiles.keys.toSet()

    fun downloadAsset(name: String): Boolean =
        owner.installMissionAsset(directory, allowedFiles, writableFiles, initialFiles, name)

    fun listFiles(): List<String> = owner.listMissionFiles(directory, allowedFiles)

    fun readFile(name: String): String? =
        owner.readMissionFile(directory, allowedFiles, name)

    fun writeFile(name: String, content: String): Boolean =
        owner.writeMissionFile(directory, writableFiles, name, content)
}
