package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the condition of an [Actor] depending on the state of the [Actor].
 */
class StateConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.health < 60) context.conditions.add("tired")
    }
}
