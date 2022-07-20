package com.barbarus.prosper.processor.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.processor.Processor

class ConditionProcessor : Processor<Actor> {
    override fun process(actor: Actor) {
        if (actor.state.health < 60) actor.conditions.add("sleep")
        if (actor.state.stamina < 30) actor.conditions.add("sleep")
        if (actor.state.hunger > 30) actor.conditions.add("hunger")
        if (actor.state.stamina > 60) actor.conditions.add("work")
    }
}
