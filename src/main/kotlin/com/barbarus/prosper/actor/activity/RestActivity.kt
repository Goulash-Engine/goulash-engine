package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s activity has the urge to rest.
 */
class RestActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("rest")
    }

    override fun blockerCondition(): List<String> {
        return listOf("rested")
    }

    override fun activity(): String {
        return "resting"
    }

    override fun duration(): Int {
        return 30
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "resting"
        actor.urges.decreaseUrge("rest", 0.5)
    }
}
