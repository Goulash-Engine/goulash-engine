package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class ConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.health < 60) context.conditions.add("sleep")
        if (context.state.stamina < 30) context.conditions.add("sleep")
        if (context.state.hunger > 30) context.conditions.add("hunger")
        if (context.state.stamina > 60) context.conditions.add("work")
    }
}
