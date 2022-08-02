package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.logic.Logic

internal class ScriptedLogic<T : Any>(
    val name: String,
    private val compiledLogic: (context: T) -> Unit
) : Logic<T> {

    override fun process(context: T) {
        this.compiledLogic(context)
    }
}
