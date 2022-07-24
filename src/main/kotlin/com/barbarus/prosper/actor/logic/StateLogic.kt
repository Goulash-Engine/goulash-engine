package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the state of an [Actor] depending on various factors.
 */
class StateLogic : Logic<Actor> {
    override fun process(context: Actor) {
        val conditions = context.conditions

        when {
            conditions.contains("malnourished") -> context.state.nourishment -= 0.1
        }

        val state = context.state

        when {
            state.nourishment == 100.0 -> context.state.health += 0.5
            state.nourishment <= 0.0 -> context.state.health -= 1
            state.nourishment < 20.0 -> context.state.health -= 0.5
            state.nourishment < 60.0 -> context.state.health -= 0.1
            state.nourishment < 100.0 -> context.state.health += 0.1
        }

        if (state.health > 100) {
            state.health = 100.0
        }
    }
}
