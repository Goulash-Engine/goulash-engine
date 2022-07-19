package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the consumption of resources that are consumable.
 */
class ConsumptionBehavior : Behavior {
    override fun act(actor: Actor) {
        val inventory = actor.inventory()
        val consumables = inventory.filter { it.traits.contains("consumable") }
        val nearestLeftover = consumables.minBy { it.quantity }
        nearestLeftover.quantity = nearestLeftover.quantity.minus(0.1)
    }
}
