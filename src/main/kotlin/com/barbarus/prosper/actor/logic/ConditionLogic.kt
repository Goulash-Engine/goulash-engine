package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the condition of an [Actor] depending on various factors.
 */
class ConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        simulateMalnourishment(context)
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
        clearExhaustionConditions(actor)
        when {
            urgeToRest >= 100 -> actor.conditions.add("unconscious")
            urgeToRest > 80 -> actor.conditions.add("blacking out")
            urgeToRest > 50 -> actor.conditions.add("exhausted")
            urgeToRest > 30 -> actor.conditions.add("tired")
            urgeToRest > 20 -> actor.conditions.add("weary")
        }
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

    private fun clearExhaustionConditions(actor: Actor) {
        listOf(
            "unconscious",
            "blacking out",
            "exhausted",
            "tired",
            "weary"
        ).forEach { actor.conditions.remove(it) }
    }

    private fun panicConditions() = listOf(
        "malnourished",
        "very sick",
        "dying",
        "severely sick"
    )

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

    private fun simulateMalnourishment(actor: Actor) {
        val eatUrge = actor.urges.getUrges()["eat"] ?: return
        if (eatUrge < 100.0) {
            actor.conditions.remove("malnourished")
        } else {
            actor.conditions.add("malnourished")
        }
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
