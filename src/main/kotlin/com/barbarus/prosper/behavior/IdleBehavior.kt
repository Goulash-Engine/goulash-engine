package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the [Actor]'s behavior when it has no activity.
 */
class IdleBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("*")
    }

    override fun blocker(): List<String> {
        return listOf()
    }

    override fun act(actor: Actor) {
        if (actor.currentActivity.isBlank()) {
            actor.state.stamina += 1
        }
    }
}
