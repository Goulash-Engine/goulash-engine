package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.logic.Logic

class ScriptedLogic<T : Any>(/*private val processFunction: ((context: T) -> Unit)?*/) : Logic<T> {
    override fun process(context: T) {
        // this.processFunction?.let { it(context) }
    }
}
