package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

class ExhaustionBehavior : Behavior {
    override fun trigger(): List<String> {
        return listOf("*")
    }

    override fun blocker(): List<String> {
        return listOf("*")
    }

    override fun act(actor: Actor) {
        if (actor.state.stamina < 10) actor.conditions.add("exhausted")
        else if (actor.state.stamina < 30) actor.conditions.add("tired")
        else if (actor.state.stamina < 90) actor.conditions.remove("rested")
    }
}
