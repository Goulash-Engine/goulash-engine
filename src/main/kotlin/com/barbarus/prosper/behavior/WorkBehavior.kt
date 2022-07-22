package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the daily work of a clan.
 */
class WorkBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("*")
    }

    override fun blocker(): List<String> {
        return listOf("tired", "sick", "exhausted")
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "working"
        actor.state.hunger += 0.5
        actor.urges.increaseUrge("rest", 1.0)
    }
}
