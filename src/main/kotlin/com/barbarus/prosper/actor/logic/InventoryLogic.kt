package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Removes [Resource]s that have no relevant weight and therefore disappear.
 */
class InventoryLogic : Logic<Actor> {

    override fun process(context: Actor) {
        val inventory = context.inventory()
        inventory.removeIf { it.weight < 0.1 }
    }
}
