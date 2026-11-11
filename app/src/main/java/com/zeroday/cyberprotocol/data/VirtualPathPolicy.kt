package com.zeroday.cyberprotocol.data

/** Pure path policy shared by terminal commands and unit tests. */
internal object VirtualPathPolicy {
    fun resolve(input: String, currentDirectory: String, workspaceRoot: String): String? {
        if (input.isBlank() || input == "/") return null
        if (!workspaceRoot.startsWith("/A.E.G.I.S/")) return null
        if (currentDirectory != workspaceRoot && !currentDirectory.startsWith("$workspaceRoot/")) return null

        val parts = if (input.startsWith("/")) mutableListOf<String>() else
            currentDirectory.trim('/').split('/').filter { it.isNotEmpty() }.toMutableList()
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
}
