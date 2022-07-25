package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the condition of an [Actor] depending on various factors.
 */
class ConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        simulateHunger(context)
        simulateExhaustion(context)
        simulateHealth(context)
        simulateDeath(context)
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

    private fun panicConditions() = listOf("")

    private fun simulateHunger(actor: Actor) {
        cleanHungerConditions(actor)

        when {
            actor.state.nourishment <= 0.0 -> actor.conditions.add("starving")
            actor.state.nourishment > 80.0 -> actor.conditions.add("well nourished")
            actor.state.nourishment > 70.0 -> actor.conditions.add("hungry")
            actor.state.nourishment > 50.0 -> actor.conditions.add("very hungry")
            actor.state.nourishment > 20.0 -> actor.conditions.add("extremely hungry")
        }

        val eatUrge = actor.urges.getUrges()["eat"]
        if (eatUrge == null) actor.conditions.add("well fed")
    }

    private fun cleanHungerConditions(actor: Actor) {
        listOf(
            "starving",
            "underfed",
            "very hungry",
            "hungry",
            "well fed",
            "well nourished"
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
