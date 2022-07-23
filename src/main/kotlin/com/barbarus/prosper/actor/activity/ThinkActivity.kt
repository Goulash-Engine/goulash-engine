package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.ResourceType

/**
 * This [Activity] controls the [Actor]'s activity when it has no activity.
 */
class ThinkActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("think")
    }

    override fun blockerConditions(): List<String> {
        return listOf()
    }

    override fun activity(): String {
        return "thinking"
    }

    override fun duration(): Int {
        return 5
    }

    override fun onFinish(actor: Actor) {
        actor.urges.stopUrge("think")
    }

    override fun act(actor: Actor) {
        actor.urges.decreaseUrge("think", duration().toDouble())
        val food = actor.inventory().count { it.type == ResourceType.FOOD }
        if (food < 4) {
            actor.urges.increaseUrge("work", 3.0)
        }
    }
}
