package com.barbarus.prosper.activity

import com.barbarus.prosper.core.domain.Actor

class ExhaustionActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("*")
    }

    override fun blockerCondition(): List<String> {
        return listOf("exhausted")
    }

    override fun act(actor: Actor) {
    }
}
