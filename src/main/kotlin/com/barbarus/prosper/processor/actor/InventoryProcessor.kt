package com.barbarus.prosper.processor.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.processor.Processor

class InventoryProcessor : Processor<Actor> {

    override fun process(actor: Actor) {
        val inventory = actor.inventory()
        inventory.removeIf { it.weight < 0.1 }
    }
}
