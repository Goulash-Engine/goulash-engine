package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class InventoryLogic : Logic<Actor> {

    override fun process(context: Actor) {
        val inventory = context.inventory()
        inventory.removeIf { it.weight < 0.1 }
    }
}
