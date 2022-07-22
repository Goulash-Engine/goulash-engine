package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor
import org.slf4j.LoggerFactory

/**
 * This [Behavior] controls the consumption of resources that are consumable.
 */
class ConsumptionBehavior : Behavior {
    override fun triggerUrge(): List<String> {
        return listOf("hungry")
    }

    override fun blockerCondition(): List<String> {
        return listOf("sick")
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "consuming"
        val inventory = actor.inventory()
        val consumables = inventory.filter { it.traits.contains("consumable") }

        if (consumables.isNotEmpty()) {
            val nearestLeftover = consumables.minBy { it.weight }
            nearestLeftover.weight -= 0.1
            actor.state.hunger -= 5
        }

        if (actor.state.hunger < 20) actor.conditions.remove("hungry")
        if (actor.state.hunger > 80) actor.conditions.add("starving")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ConsumptionBehavior")
    }
}
