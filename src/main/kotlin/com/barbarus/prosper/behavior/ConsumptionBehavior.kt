package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor
import org.slf4j.LoggerFactory

/**
 * This [Behavior] controls the consumption of resources that are consumable.
 */
class ConsumptionBehavior(
    val trigger: String
) : Behavior {
    override fun trigger(): String {
        return trigger
    }

    override fun act(actor: Actor) {
        LOG.info("${actor.id} is consuming")
        val inventory = actor.inventory()
        val consumables = inventory.filter { it.traits.contains("consumable") }
        val nearestLeftover = consumables.minBy { it.weight }
        nearestLeftover.weight = nearestLeftover.weight.minus(0.1)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ConsumptionBehavior")
    }
}
