package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class InventoryLogic : Logic<Actor> {

    override fun process(actor: Actor) {
        val inventory = actor.inventory()
        inventory.removeIf { it.weight < 0.1 }
    }
}
