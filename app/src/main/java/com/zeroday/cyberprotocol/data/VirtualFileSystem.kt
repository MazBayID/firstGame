package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.MissionDefinition
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

    @Synchronized
    fun execute(commandLine: String, currentDirectory: String, workspaceRoot: String = "$root/personal"): TerminalResponse {
        val line = commandLine.trim()
        if (line.isEmpty()) return TerminalResponse("", currentDirectory)
        if (line == "clear") return TerminalResponse("", currentDirectory, clearScreen = true)

        val redirect = Regex("""^echo\s+(['"])(.*?)\1\s*>\s*(\S+)$""").matchEntire(line)
        if (redirect != null) {
            val target = resolve(redirect.groupValues[3], currentDirectory, workspaceRoot)
                ?: return TerminalResponse("Invalid path: access is restricted to the A.E.G.I.S. workspace.", currentDirectory)
            if (directories.contains(target)) return TerminalResponse("Cannot write to a directory.", currentDirectory)
            if (!directories.contains(parent(target))) return TerminalResponse("Folder induk tidak ditemukan.", currentDirectory)
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
            "help" -> """
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
            "whoami" -> "REKRUT  ·  LINGKUNGAN PELATIHAN A.E.G.I.S."
            "ls" -> {
                val target = resolve(rest.firstOrNull() ?: ".", cwd, workspaceRoot)
                    ?: return TerminalResponse("Jalur tidak valid.", cwd)
                if (!directories.contains(target)) return TerminalResponse("Not a directory: $target", cwd)
                val childDirs = directories.filter { it != target && parent(it) == target }
                    .map { it.substringAfterLast("/") + "/" }
                val childFiles = files.keys.filter { parent(it) == target }
                    .map { it.substringAfterLast("/") }
                (childDirs + childFiles).sorted().joinToString("\n").ifBlank { "(empty)" }
            }
            "cd" -> {
                val target = resolve(rest.firstOrNull() ?: initialDirectory(), cwd, workspaceRoot)
                    ?: return TerminalResponse("Jalur tidak valid: akses keluar dari ruang kerja virtual ditolak.", cwd)
                if (!directories.contains(target)) return TerminalResponse("Folder tidak ditemukan: $target", cwd)
                cwd = target
                ""
            }
            "cat" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse("Usage: cat <file>", cwd)
                files[target] ?: return TerminalResponse("File tidak ditemukan: $target", cwd)
            }
            "mkdir" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse("Usage: mkdir <name>", cwd)
                if (directories.contains(target) || files.containsKey(target)) return TerminalResponse("Already exists: $target", cwd)
                if (!directories.contains(parent(target))) return TerminalResponse("Parent directory does not exist.", cwd)
                directories.add(target)
                persist()
                ""
            }
            "touch" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse("Usage: touch <file>", cwd)
                if (directories.contains(target)) return TerminalResponse("A directory already exists at that path.", cwd)
                if (!directories.contains(parent(target))) return TerminalResponse("Parent directory does not exist.", cwd)
                if (!files.containsKey(target)) files[target] = ""
                persist()
                ""
            }
            "rm" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd, workspaceRoot)
                    ?: return TerminalResponse("Usage: rm <file>", cwd)
                if (directories.contains(target)) return TerminalResponse("Penghapusan folder belum tersedia. Hapus file satu per satu.", cwd)
                if (files.remove(target) == null) return TerminalResponse("File not found: $target", cwd)
                persist()
                ""
            }
            "python" -> "PYTHON_LAUNCH:" + (rest.firstOrNull() ?: "")
            else -> "Perintah tidak dikenal: $name\nKetik 'help' untuk melihat daftar perintah."
        }
        return TerminalResponse(output, cwd)
    }

    @Synchronized
    fun writeFile(path: String, content: String, currentDirectory: String, workspaceRoot: String = "$root/personal"): Boolean {
        val resolved = resolve(path, currentDirectory, workspaceRoot) ?: return false
        if (directories.contains(resolved) || !directories.contains(parent(resolved))) return false
        files[resolved] = content
        persist()
        return true
    }

    @Synchronized
    fun readFile(path: String, currentDirectory: String, workspaceRoot: String = "$root/personal"): String? {
        val resolved = resolve(path, currentDirectory, workspaceRoot) ?: return null
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
            files[briefingPath] = "${definition.title.uppercase()}\\n\\n${definition.briefing}\\n"
        }
        persist()
        return MissionWorkspace(
            owner = this,
            directory = definition.workspaceDirectory,
            allowedFiles = definition.allowedFiles,
            writableFiles = definition.writableFiles
        )
    }

    @Synchronized
    internal fun listMissionFiles(directory: String, allowedFiles: Set<String>): List<String> =
        allowedFiles.filter { files.containsKey("$directory/$it") }.sorted()

    @Synchronized
    internal fun readMissionFile(directory: String, allowedFiles: Set<String>, name: String): String? {
        if (name !in allowedFiles) return null
        return files["$directory/$name"]
    }

    @Synchronized
    internal fun writeMissionFile(
        directory: String,
        writableFiles: Set<String>,
        name: String,
        content: String
    ): Boolean {
        if (name !in writableFiles || content.length > MAX_MISSION_FILE_LENGTH) return false
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

    private fun resolve(input: String, cwd: String, workspaceRoot: String): String? {
        if (input.isBlank()) return null
        if (input == "/") return root
        val parts = if (input.startsWith("/")) mutableListOf<String>() else
            cwd.trim('/').split('/').filter { it.isNotEmpty() }.toMutableList()
        input.split('/').forEach { part ->
            when (part) {
                "", "." -> Unit
                ".." -> if (parts.isNotEmpty()) parts.removeAt(parts.lastIndex) else return null
                else -> parts.add(part)
            }
        }
        if (parts.firstOrNull() != "A.E.G.I.S") return null
        val resolved = "/" + parts.joinToString("/")
        if (resolved != workspaceRoot && !resolved.startsWith("$workspaceRoot/")) return null
        return resolved
    }

    private fun parent(path: String): String = path.substringBeforeLast("/", "")

    companion object { private const val MAX_MISSION_FILE_LENGTH = 50_000 }
    private fun tokenize(line: String): List<String> =
        Regex(""""([^"]*)"|'([^']*)'|(\S+)""").findAll(line).map { match ->
            match.groups[1]?.value ?: match.groups[2]?.value ?: match.groups[3]?.value.orEmpty()
        }.toList()
}


/** Narrow capability handle for one mission workspace. */
class MissionWorkspace internal constructor(
    private val owner: VirtualFileSystem,
    val directory: String,
    private val allowedFiles: Set<String>,
    private val writableFiles: Set<String>
) {
    val allowedFileNames: Set<String> get() = allowedFiles.toSet()
    val writableFileNames: Set<String> get() = writableFiles.toSet()

    fun listFiles(): List<String> = owner.listMissionFiles(directory, allowedFiles)

    fun readFile(name: String): String? =
        owner.readMissionFile(directory, allowedFiles, name)

    fun writeFile(name: String, content: String): Boolean =
        owner.writeMissionFile(directory, writableFiles, name, content)
}
