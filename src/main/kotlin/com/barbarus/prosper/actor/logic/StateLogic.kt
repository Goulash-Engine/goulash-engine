package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the state of an [Actor] depending on various factors.
 */
class StateLogic : Logic<Actor> {
    override fun process(context: Actor) {
        decreaseHealth(context)
    }

    private fun decreaseHealth(context: Actor) {
        if (context.conditions.contains("malnourished")) {
            context.state.health -= 0.1
        }
    }
}
