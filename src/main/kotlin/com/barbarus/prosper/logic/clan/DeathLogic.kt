package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

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
