package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * This logic will handle the "dead" state of an [Actor]
 */
class DeathLogic : Logic<Actor> {
    override fun process(context: Actor) {
        if (context.state.health < 0) {
            context.conditions.add("dead")
        }
    }
}