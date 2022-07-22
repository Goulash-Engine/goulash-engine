package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class UrgeLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.stamina < 60) {
            if (context.urges.containsKey("rest")) {
                context.urges["rest"] = context.urges["rest"]!!.plus(1.0)
            } else {
                context.urges["rest"] = 1.0
            }
        }
    }
}
