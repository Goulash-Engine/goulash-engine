package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * Represents the [Activity] an [Actor] is executing when the "starving" condition occurs.
 */
class StarvationActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("starving")
    }

    override fun blockerCondition(): List<String> {
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
