package com.goulash.script.logic

import com.goulash.script.domain.ScriptStatement

data class ContainerScriptContext(
    val head: ScriptHead,
    val statements: Map<String, List<ScriptStatement>>
)
