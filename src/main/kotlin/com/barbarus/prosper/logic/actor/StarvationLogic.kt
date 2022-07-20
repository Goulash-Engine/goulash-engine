package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class StarvationLogic : Logic<Actor> {

    override fun process(actor: Actor) {
        if (actor.state.hunger < 5) {
            actor.state.health = actor.state.health.minus(5)
        } else if (actor.state.hunger < 10) {
            actor.state.health--
        }
    }
}
