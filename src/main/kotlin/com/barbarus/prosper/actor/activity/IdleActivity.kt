package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the daily work of a clan.
 */
class IdleActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("*")
    }

    override fun blockerConditions(): List<String> {
        return listOf("")
    }

    override fun activity(): String {
        return "idle"
    }

    override fun duration(): Int {
        return 0
    }

    override fun act(actor: Actor) = Unit
}
