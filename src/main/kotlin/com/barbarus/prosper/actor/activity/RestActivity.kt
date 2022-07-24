package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration

/**
 * This [Activity] controls the [Actor]'s activity has the urge to rest.
 */
class RestActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("rest")
    }

    override fun blockerConditions(): List<String> {
        return listOf("rested")
    }

    override fun activity(): String {
        return "resting"
    }

    override fun duration(): Duration {
        return 30.toDuration()
    }

    override fun act(actor: Actor): Boolean {
        actor.urges.decreaseUrge("rest", duration().getDuration().toDouble())
        actor.urges.increaseUrge("think", 5.0)
        return true
    }
}
