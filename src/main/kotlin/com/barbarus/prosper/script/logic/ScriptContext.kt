package com.barbarus.prosper.script.logic

import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.grammar.LogicScriptFileGrammar

data class ScriptContext(
    val head: LogicScriptFileGrammar.ScriptHead,
    val statements: List<ScriptStatement>
)
