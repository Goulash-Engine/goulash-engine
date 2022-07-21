package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the daily work of a clan.
 */
class WorkBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("ambitious")
    }

    override fun blocker(): List<String> {
        return listOf("tired", "sick")
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "working"
        actor.state.stamina -= 3
        actor.state.hunger += 0.5
        if (actor.state.stamina < 60) {
            actor.conditions.remove("ambitious")
        }
    }
}
