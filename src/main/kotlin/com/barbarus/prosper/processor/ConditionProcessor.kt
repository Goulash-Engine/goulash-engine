package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Actor

class ConditionProcessor {
    fun process(actor: Actor) {
        if (actor.state.health < 60) actor.conditions.add("sleep")
        if (actor.state.stamina < 30) actor.conditions.add("sleep")
        if (actor.state.hunger > 30) actor.conditions.add("hunger")
        if (actor.state.stamina > 60) actor.conditions.add("work")
    }
}
