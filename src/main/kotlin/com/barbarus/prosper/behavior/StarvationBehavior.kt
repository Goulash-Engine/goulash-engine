package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * Represents the [Behavior] an [Actor] is executing when the "starving" condition occurs.
 */
class StarvationBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("starving")
    }

    override fun blocker(): List<String> {
        return listOf()
    }

    override fun act(actor: Actor) {
        if (actor.state.hunger < 80) {
            actor.conditions.remove("starving")
            return
        }

        if (actor.state.hunger > 90) {
            actor.conditions.remove("hungry")
            actor.conditions.add("dying")
            actor.state.health -= 4
        }

        actor.state.health--
    }
}
