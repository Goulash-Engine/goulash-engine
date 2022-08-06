package com.goulash.actor.activity

import com.goulash.core.activity.Activity
import com.goulash.core.activity.Duration
import com.goulash.core.domain.Actor
import com.goulash.core.domain.ResourceType
import com.goulash.core.extension.toDuration
import com.goulash.simulation.Simulation

/**
 * This [Activity] controls the [Actor]'s activity when it has no activity.
 */
class ThinkActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("think")
    }

    override fun activity(): String {
        return "thinking"
    }

    override fun duration(): Duration {
        return 10.0.toDuration()
    }

    override fun onFinish(actor: Actor) {
        actor.urges.stopUrge("think")
    }

    override fun act(actor: Actor): Boolean {
        actor.urges.decreaseUrge("think", duration().asDouble())

        if (Simulation.WORLD_TIME.isNight()) {
            actor.urges.increaseUrge("sleep", 5.0)
        }

        val food = actor.inventory().count { it.type == ResourceType.FOOD }
        if (food < 4) {
            actor.urges.increaseUrge("work", 3.0)
        }

        return true
    }
}
