package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the condition of an [Actor] depending on various factors.
 */
class ConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        simulateStarvation(context)
        simulateExhaustion(context)
        simulatePanic(context)
        simulateHealth(context)
        simulateDeath(context)
    }

    private fun simulatePanic(actor: Actor) {
        actor.conditions.remove("panic")
        if (actor.conditions.any { panicConditions().contains(it) }) {
            actor.conditions.add("panic")
        }
    }

    private fun simulateExhaustion(actor: Actor) {
        val urgeToRest: Double = actor.urges.getUrges()["rest"] ?: return
        when {
            urgeToRest >= 100 -> actor.conditions.add("unconscious")
            urgeToRest > 80 -> actor.conditions.add("blacking out")
            urgeToRest > 50 -> actor.conditions.add("exhausted")
            urgeToRest > 30 -> actor.conditions.add("tired")
            urgeToRest > 20 -> actor.conditions.add("weary")
            else -> clearExhaustionConditions(actor)
        }
    }

    private fun clearExhaustionConditions(actor: Actor) {
        listOf(
            "unconscious",
            "blacking out",
            "exhausted",
            "tired",
            "weary"
        ).forEach { actor.conditions.remove(it) }
    }

    private fun simulateDeath(actor: Actor) {
        if (actor.state.health < 0) {
            actor.conditions.add("dead")
        }
    }

    private fun simulateHealth(actor: Actor) {
        val health = actor.state.health
        clearHealthConditions(actor)

        when {
            health <= 0 -> actor.conditions.add("dead")
            health < 10 -> actor.conditions.add("dying")
            health < 20 -> actor.conditions.add("severely sick")
            health < 40 -> actor.conditions.add("very sick")
            health < 50 -> actor.conditions.add("sick")
            health < 70 -> actor.conditions.add("dizzy")
            health < 90 -> actor.conditions.add("unwell")
        }
    }

    private fun clearHealthConditions(actor: Actor) {
        listOf(
            "dying",
            "severely sick",
            "very sick",
            "sick",
            "dizzy",
            "unwell"
        ).forEach { actor.conditions.remove(it) }
    }

    private fun panicConditions() = listOf(
        "starving",
        "very sick",
        "dying",
        "severely sick"
    )

    private fun simulateStarvation(actor: Actor) {
        val eatUrge = actor.urges.getUrges()["eat"] ?: return
        clearStarvationConditions(actor)

        when {
            eatUrge == 100.0 -> actor.conditions.add("malnourished")
            eatUrge > 80.0 -> actor.conditions.add("very hungry")
            eatUrge > 60.0 -> actor.conditions.add("hungry")
            actor.state.nourishment <= 0.0 -> actor.conditions.add("starving")
        }
    }

    private fun clearStarvationConditions(actor: Actor) {
        listOf(
            "starving",
            "malnourished",
            "very hungry",
            "hungry"
        ).forEach { actor.conditions.remove(it) }
    }

    companion object {
        val GLOBAL_BLOCKING_CONDITION = listOf(
            "dying",
            "severely sick",
            "unconscious",
            "blacking out"
        )
    }
}
