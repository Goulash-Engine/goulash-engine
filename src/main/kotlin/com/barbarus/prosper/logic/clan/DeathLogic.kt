package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class DeathLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.health < 0) {
            context.conditions.add("dead")
        }
    }
}
