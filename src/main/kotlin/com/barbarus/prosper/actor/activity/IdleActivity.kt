package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration

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

    override fun duration(): Duration {
        return 0.toDuration()
    }

    override fun act(actor: Actor) = Unit
}
