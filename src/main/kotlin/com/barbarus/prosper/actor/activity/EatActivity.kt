package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.ResourceType
import com.barbarus.prosper.core.extension.toDuration

/**
 * This [Activity] controls the eat behavior work of a actor.
 */
class EatActivity : Activity {

    override fun triggerUrges(): List<String> {
        return listOf("eat")
    }

    override fun blockerConditions(): List<String> {
        return listOf("very sick", "sleeping", "well fed")
    }

    override fun priorityConditions(): List<String> {
        return listOf("very hungry", "extremely hungry", "starving")
    }

    override fun activity(): String {
        return "eating"
    }

    override fun duration(): Duration {
        return 40.toDuration()
    }

    override fun act(actor: Actor): Boolean {
        val food = actor.inventory().find { it.type == ResourceType.FOOD }

        if (actor.state.nourishment >= 100.0) return false

        if (food == null) {
            actor.urges.increaseUrge("think", 5.0)
            return false
        } else if (actor.state.nourishment < 100.0) {
            actor.urges.increaseUrge("rest", 0.3)
            actor.urges.decreaseUrge("eat", 5.0)
            actor.state.nourishment += 3.0
            food.weight -= 0.1
        }
        return true
    }
}
