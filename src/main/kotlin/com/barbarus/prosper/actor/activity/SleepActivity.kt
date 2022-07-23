package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s activity to sleep
 */
class SleepActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("sleep")
    }

    override fun blockerConditions(): List<String> {
        return listOf()
    }

    override fun activity(): String {
        return "sleeping"
    }

    override fun duration(): Int {
        return 200
    }

    override fun act(actor: Actor) {
        actor.urges.decreaseUrge("sleep", duration().toDouble())
    }
}
