package com.goulash.script.logic

import com.goulash.script.domain.ScriptStatement

data class ContainerScriptContext(
    val head: ScriptHead,
    val statements: List<ScriptStatement>
) {
    internal data class ContextMutation(
        val type: String,
        val target: String,
        val operation: Operation
    )

    internal data class Operation(
        val name: String,
        val argument: String
    )
}
