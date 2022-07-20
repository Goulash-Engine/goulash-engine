package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

/**
 * This logic decreases the health of an [Actor] according to the state of hunger.
 */
class StarvationLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.hunger > 90) {
            context.state.health = context.state.health.minus(5)
        } else if (context.state.hunger > 80) {
            context.state.health--
        }
    }
}
