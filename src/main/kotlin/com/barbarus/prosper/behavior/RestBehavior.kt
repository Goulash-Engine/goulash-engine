package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the [Actor]'s behavior when it has no activity.
 */
class RestBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("tired")
    }

    override fun blocker(): List<String> {
        return listOf("*")
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "resting"
        actor.state.stamina += 2
        if (actor.state.stamina > 75) actor.conditions.remove("tired")
    }
}
