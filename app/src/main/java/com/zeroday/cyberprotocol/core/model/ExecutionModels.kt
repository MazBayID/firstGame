package com.zeroday.cyberprotocol.core.model

/** Result returned by the local Python execution adapter. */
data class ExecutionResult(
    val output: String,
    val error: String = ""
)

/** Result returned by a mission evaluator; execution and evaluation remain separate. */
data class EvaluationResult(
    val passed: Boolean,
    val output: String,
    val feedback: String,
    val error: String = ""
)
