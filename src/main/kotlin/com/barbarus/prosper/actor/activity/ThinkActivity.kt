package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.ResourceType
import com.barbarus.prosper.core.extension.toDuration
import com.barbarus.prosper.simulation.Simulation

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
        return 10.toDuration()
    }

    override fun priorityConditions(): List<String> {
        return listOf("panic")
    }

    override fun onFinish(actor: Actor) {
        actor.urges.stopUrge("think")
    }

    override fun act(actor: Actor) {
        actor.urges.decreaseUrge("think", duration().getDuration().toDouble())

        if (Simulation.WORLD_TIME.isNight()) {
            actor.urges.increaseUrge("sleep", 5.0)
            return
        }

        val food = actor.inventory().count { it.type == ResourceType.FOOD }
        if (food < 4) {
            actor.urges.increaseUrge("work", 3.0)
            return
        }
    }
}
