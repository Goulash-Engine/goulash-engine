package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the daily work of a clan.
 */
class WorkActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("work")
    }

    override fun blockerConditions(): List<String> {
        return listOf("tired", "sick", "exhausted")
    }

    override fun activity(): String {
        return "working"
    }

    override fun duration(): Int {
        return 30
    }

    override fun act(actor: Actor) {
        actor.state.hunger += 0.5
        actor.urges.increaseUrge("rest", 1.0)
        actor.urges.decreaseUrge("work", 1.0)
    }
}