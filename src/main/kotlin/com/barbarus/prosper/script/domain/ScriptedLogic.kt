package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.logic.Logic

class ScriptedLogic<T : Any>(
    val name: String,
    private val compiledScript: (context: T) -> Unit
) : Logic<T> {

    override fun process(context: T) {
        this.compiledScript(context)
    }
}
