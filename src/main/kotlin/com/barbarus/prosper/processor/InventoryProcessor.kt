package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Actor

class InventoryProcessor {

    fun process(actor: Actor) {
        val inventory = actor.inventory()
        inventory.removeIf { it.weight < 0.1 }
    }
}
