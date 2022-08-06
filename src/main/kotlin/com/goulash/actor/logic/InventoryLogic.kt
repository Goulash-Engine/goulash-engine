package com.goulash.actor.logic

import com.goulash.core.domain.Actor
import com.goulash.core.logic.Logic

/**
 * Removes [Resource]s that have no relevant weight and therefore disappear.
 */
class InventoryLogic : Logic<Actor> {

    override fun process(context: Actor) {
        val inventory = context.inventory()
        inventory.removeIf { it.weight < 0.1 }
    }
}
