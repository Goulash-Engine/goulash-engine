package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration

/**
 * This [Activity] controls the [Actor]'s activity to sleep
 */
class SleepActivity : Activity {
    override fun triggerUrges(): List<String> {
        return listOf("sleep")
    }

    override fun activity(): String {
        return "sleeping"
    }

    override fun priority(): Int {
        return 1
    }

    override fun duration(): Duration {
        return 6.0.times(60).toDuration()
    }

    override fun act(actor: Actor): Boolean {
        actor.urges.decreaseUrge("sleep", duration().asDouble())
        return true
    }
}
