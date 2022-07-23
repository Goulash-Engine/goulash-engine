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

    override fun act(actor: Actor) {
        actor.urges.decreaseUrge("think", duration().toDouble())
        val woodenMaterial = actor.inventory().count { it.type == ResourceType.WOODEN_MATERIAL }
        if (woodenMaterial < 5) {
            actor.urges.increaseUrge("work", 3.0)
        }
    }
}
