package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.ResourceType
import com.barbarus.prosper.core.extension.toDuration

/**
 * This [Activity] controls the daily work of a clan.
 */
class EatActivity : Activity {
    private val foodConsumed: Double = 0.0

    override fun triggerUrges(): List<String> {
        return listOf("eat")
    }

    override fun blockerConditions(): List<String> {
        return listOf("sick", "sleeping")
    }

    override fun activity(): String {
        return "eating"
    }

    override fun duration(): Duration {
        return 40.toDuration()
    }

    override fun onFinish(actor: Actor) {
        // TODO: add logic to track food consumed
        // actor.conditions.add("full")
    }

    override fun act(actor: Actor) {
        val food = actor.inventory().find { it.type == ResourceType.FOOD }

        if (food == null) {
            actor.urges.increaseUrge("think", 5.0)
        } else {
            actor.urges.increaseUrge("rest", 0.3)
            actor.urges.decreaseUrge("eat", 1.0)
            food.weight -= 0.1
        }
    }
}
