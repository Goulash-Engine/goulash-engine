package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the daily work of a clan.
 */
class WorkBehavior : Behavior {
    override fun trigger(): String {
        return "ambitious"
    }

    override fun blocker(): String {
        return "tired"
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "working"
        actor.state.stamina -= 3
        actor.state.hunger += 0.5
        if (actor.state.stamina < 30) {
            actor.conditions.add(blocker())
            actor.conditions.remove(trigger())
        }
    }
}
