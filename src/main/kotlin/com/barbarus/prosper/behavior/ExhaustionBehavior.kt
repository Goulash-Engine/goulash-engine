package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

class ExhaustionBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("*")
    }

    override fun blocker(): List<String> {
        return listOf("exhausted")
    }

    override fun act(actor: Actor) {
    }
}
