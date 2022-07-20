package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

/**
 * Changes the condition of an [Actor] depending on the state of the [Actor].
 */
class StateConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.health < 60) context.conditions.add("tired")
        if (context.state.stamina < 30) context.conditions.add("tired")
        if (context.state.hunger > 30) context.conditions.add("hunger")
        if (context.state.stamina > 60) context.conditions.add("work")
    }
}
