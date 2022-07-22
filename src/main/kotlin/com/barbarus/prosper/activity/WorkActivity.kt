package com.barbarus.prosper.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the daily work of a clan.
 */
class WorkActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("work")
    }

    override fun blockerCondition(): List<String> {
        return listOf("tired", "sick", "exhausted")
    }

    override fun activity(): String {
        return "working"
    }

    override fun act(actor: Actor) {
        actor.state.hunger += 0.5
        actor.urges.increaseUrge("rest", 1.0)
    }
}
