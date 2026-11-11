package com.zeroday.cyberprotocol.execution

import com.chaquo.python.Python
import com.zeroday.cyberprotocol.core.model.*
import org.json.JSONObject

/** Language adapter boundary. Other runtimes can be added without coupling UI to Chaquopy. */
class PythonExecutionService {
    fun run(source: String): ExecutionResult = try {
        val raw = Python.getInstance().getModule("runner").callAttr("run_code_json", source).toString()
        val json = JSONObject(raw)
        ExecutionResult(json.optString("output"), json.optString("error"))
    } catch (error: Exception) {
        ExecutionResult("", error.message ?: "Python runtime error")
    }

    fun submit(missionId: Int, source: String): EvaluationResult = try {
        val raw = Python.getInstance().getModule("runner")
            .callAttr("evaluate_mission", source, missionId).toString()
        val json = JSONObject(raw)
        EvaluationResult(json.optBoolean("passed"), json.optString("output"),
            json.optString("feedback"), json.optString("error"))
    } catch (error: Exception) {
        EvaluationResult(false, "", "Evaluation could not complete.", error.message ?: "Python runtime error")
    }
}
