package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the [Actor]'s behavior when it is awake.
 */
class AwakeBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("*")
    }

    override fun blocker(): List<String> {
        return listOf("sleeping")
    }

    override fun act(actor: Actor) {
        actor.state.hunger += 0.1
    }
}
