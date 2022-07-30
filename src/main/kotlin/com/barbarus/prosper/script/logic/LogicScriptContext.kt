package com.barbarus.prosper.script.logic

import com.barbarus.prosper.script.domain.ScriptStatement

internal data class LogicScriptContext(
    val head: ScriptHead,
    val statements: List<ScriptStatement>
)

internal data class ScriptHead(
    val name: String
)

internal data class ContextMutation(
    val type: String,
    val target: String,
    val operation: Operation
)

internal data class Operation(
    val name: String,
    val argument: String
)
