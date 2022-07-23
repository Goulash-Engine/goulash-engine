package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the condition of an [Actor] depending on factors.
 */
class ConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        simulateMalnourishment(context)
        simulateHealth(context)
        simulateDeath(context)
    }

    private fun simulateDeath(context: Actor) {
        if (context.state.health < 0) {
            context.conditions.add("dead")
        }
    }

    private fun simulateHealth(context: Actor) {
        val health = context.state.health
        clearHealthConditions(context)
        when {
            health <= 0 -> context.conditions.add("dead")
            health < 10 -> context.conditions.add("dying")
            health < 20 -> context.conditions.add("severely sick")
            health < 40 -> context.conditions.add("very sick")
            health < 50 -> context.conditions.add("sick")
            health < 70 -> context.conditions.add("dizzy")
            health < 90 -> context.conditions.add("unwell")
        }
    }

    private fun clearHealthConditions(context: Actor) {
        listOf(
            "dying",
            "severely sick",
            "very sick",
            "sick",
            "dizzy",
            "unwell"
        ).forEach { context.conditions.remove(it) }
    }

    private fun simulateMalnourishment(context: Actor) {
        val eatUrge = context.urges.getUrges()["eat"]

        if (eatUrge != null) {
            if (eatUrge < 100.0) {
                context.conditions.remove("malnourished")
            } else {
                context.conditions.add("malnourished")
            }
        }
    }
}
