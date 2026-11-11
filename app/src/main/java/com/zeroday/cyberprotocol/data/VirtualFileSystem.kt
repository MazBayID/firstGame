package com.zeroday.cyberprotocol.data

import android.content.Context
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
    fun execute(commandLine: String, currentDirectory: String): TerminalResponse {
        val line = commandLine.trim()
        if (line.isEmpty()) return TerminalResponse("", currentDirectory)
        if (line == "clear") return TerminalResponse("", currentDirectory, clearScreen = true)

        val redirect = Regex("""^echo\s+(['"])(.*?)\1\s*>\s*(\S+)$""").matchEntire(line)
        if (redirect != null) {
            val target = resolve(redirect.groupValues[3], currentDirectory)
                ?: return TerminalResponse("Invalid path: access is restricted to the A.E.G.I.S. workspace.", currentDirectory)
            if (directories.contains(target)) return TerminalResponse("Cannot write to a directory.", currentDirectory)
            if (!directories.contains(parent(target))) return TerminalResponse("Parent directory does not exist.", currentDirectory)
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
                SUPPORTED COMMANDS
                pwd                 Show current directory
                ls [path]           List files and directories
                cd <path>           Change directory
                cat <file>          Read a text file
                mkdir <name>        Create a directory
                touch <file>        Create an empty file
                rm <file>           Delete a file
                echo "text" > file  Write text to a file
                whoami              Show current in-game identity
                clear               Clear terminal output
                python <file.py>    Run a saved Python script
                help                Show this reference

                This is an isolated A.E.G.I.S. virtual environment, not an Android shell.
            """.trimIndent()
            "pwd" -> cwd
            "whoami" -> "RECRUIT  ·  A.E.G.I.S. TRAINING ENVIRONMENT"
            "ls" -> {
                val target = resolve(rest.firstOrNull() ?: ".", cwd)
                    ?: return TerminalResponse("Invalid path.", cwd)
                if (!directories.contains(target)) return TerminalResponse("Not a directory: $target", cwd)
                val childDirs = directories.filter { it != target && parent(it) == target }
                    .map { it.substringAfterLast("/") + "/" }
                val childFiles = files.keys.filter { parent(it) == target }
                    .map { it.substringAfterLast("/") }
                (childDirs + childFiles).sorted().joinToString("\n").ifBlank { "(empty)" }
            }
            "cd" -> {
                val target = resolve(rest.firstOrNull() ?: initialDirectory(), cwd)
                    ?: return TerminalResponse("Invalid path: access outside the virtual workspace is denied.", cwd)
                if (!directories.contains(target)) return TerminalResponse("Directory not found: $target", cwd)
                cwd = target
                ""
            }
            "cat" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd)
                    ?: return TerminalResponse("Usage: cat <file>", cwd)
                files[target] ?: return TerminalResponse("File not found: $target", cwd)
            }
            "mkdir" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd)
                    ?: return TerminalResponse("Usage: mkdir <name>", cwd)
                if (directories.contains(target) || files.containsKey(target)) return TerminalResponse("Already exists: $target", cwd)
                if (!directories.contains(parent(target))) return TerminalResponse("Parent directory does not exist.", cwd)
                directories.add(target)
                persist()
                ""
            }
            "touch" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd)
                    ?: return TerminalResponse("Usage: touch <file>", cwd)
                if (directories.contains(target)) return TerminalResponse("A directory already exists at that path.", cwd)
                if (!directories.contains(parent(target))) return TerminalResponse("Parent directory does not exist.", cwd)
                if (!files.containsKey(target)) files[target] = ""
                persist()
                ""
            }
            "rm" -> {
                val target = resolve(rest.firstOrNull() ?: "", cwd)
                    ?: return TerminalResponse("Usage: rm <file>", cwd)
                if (directories.contains(target)) return TerminalResponse("Directory deletion is not enabled. Remove files individually.", cwd)
                if (files.remove(target) == null) return TerminalResponse("File not found: $target", cwd)
                persist()
                ""
            }
            "python" -> "PYTHON_LAUNCH:" + (rest.firstOrNull() ?: "")
            else -> "Command not found: $name\nType 'help' to see supported commands."
        }
        return TerminalResponse(output, cwd)
    }

    @Synchronized
    fun writeFile(path: String, content: String, currentDirectory: String): Boolean {
        val resolved = resolve(path, currentDirectory) ?: return false
        if (directories.contains(resolved) || !directories.contains(parent(resolved))) return false
        files[resolved] = content
        persist()
        return true
    }

    @Synchronized
    fun readFile(path: String, currentDirectory: String): String? {
        val resolved = resolve(path, currentDirectory) ?: return null
        return files[resolved]
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

    private fun resolve(input: String, cwd: String): String? {
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
        return "/" + parts.joinToString("/")
    }

    private fun parent(path: String): String = path.substringBeforeLast("/", "")
    private fun tokenize(line: String): List<String> =
        Regex(""""([^"]*)"|'([^']*)'|(\S+)""").findAll(line).map { match ->
            match.groups[1]?.value ?: match.groups[2]?.value ?: match.groups[3]?.value.orEmpty()
        }.toList()
}
