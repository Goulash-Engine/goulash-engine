package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s activity has the urge to rest.
 */
class RestActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("rest")
    }

    override fun blockerConditions(): List<String> {
        return listOf("rested")
    }

    override fun activity(): String {
        return "resting"
    }

    override fun duration(): Int {
        return 10
    }

    override fun act(actor: Actor) {
        actor.urges.decreaseUrge("rest", duration().toDouble())
        actor.urges.increaseUrge("think", 5.0)
    }
}
