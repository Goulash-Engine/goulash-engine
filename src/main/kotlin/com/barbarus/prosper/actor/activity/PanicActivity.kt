package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.ResourceType

/**
 * This [Activity] is an emergency behavior is the [Actor] is in a life threatening situation.
 */
class PanicActivity : Activity {
    override fun activity(): String {
        return "panic"
    }

    override fun priorityConditions(): List<String> {
        return listOf("panic")
    }

    override fun act(actor: Actor) {
        val food = actor.inventory().count { it.type == ResourceType.FOOD }

        when {
            food <= 0 -> actor.urges.increaseUrge("work", 10.0)
        }
    }
}
