package com.zeroday.cyberprotocol.execution

import com.chaquo.python.Python
import com.zeroday.cyberprotocol.data.MissionWorkspace
import com.zeroday.cyberprotocol.core.model.*
import org.json.JSONArray
import org.json.JSONObject

/** Language adapter boundary. Other runtimes can be added without coupling UI to Chaquopy. */
class PythonExecutionService {
    fun run(
        source: String,
        workspace: MissionWorkspace? = null,
        variables: Map<String, List<String>> = emptyMap()
    ): ExecutionResult = try {
        val initialFiles = linkedMapOf<String, String>()
        workspace?.allowedFileNames?.sorted()?.forEach { name ->
            workspace.readFile(name)?.let { initialFiles[name] = it }
        }
        val workspaceJson = JSONObject().apply {
            initialFiles.forEach { (name, content) -> put(name, content) }
        }
        val writableNames = workspace?.writableFileNames.orEmpty()
        val writableJson = JSONArray().apply { writableNames.sorted().forEach { put(it) } }
        val variablesJson = JSONObject().apply {
            variables.forEach { (name, values) -> put(name, JSONArray(values)) }
        }
        val raw = Python.getInstance().getModule("runner")
            .callAttr("run_code_json", source, workspaceJson.toString(), writableJson.toString(), variablesJson.toString()).toString()
        val json = JSONObject(raw)
        val returnedFiles = json.optJSONObject("files") ?: JSONObject()
        if (json.optString("error").isBlank()) {
            returnedFiles.keys().forEach { name ->
                val updated = returnedFiles.optString(name, "")
                if (name in writableNames && initialFiles[name] != updated) {
                    workspace?.writeFile(name, updated)
                }
            }
        }
        ExecutionResult(json.optString("output"), json.optString("error"))
    } catch (error: Exception) {
        ExecutionResult("", error.message ?: "Python runtime error")
    }

    fun submit(
        missionId: Int,
        source: String,
        language: GameLanguage = GameLanguage.ENGLISH
    ): EvaluationResult = try {
        val raw = Python.getInstance().getModule("runner")
            .callAttr("evaluate_mission", source, missionId, language.localeTag).toString()
        val json = JSONObject(raw)
        EvaluationResult(json.optBoolean("passed"), json.optString("output"),
            json.optString("feedback"), json.optString("error"))
    } catch (error: Exception) {
        EvaluationResult(false, "", "Evaluation could not complete.", error.message ?: "Python runtime error")
    }
}
