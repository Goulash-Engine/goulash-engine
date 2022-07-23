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

    override fun blockerConditions(): List<String> {
        return listOf()
    }

    override fun activity(): String {
        return "sleeping"
    }

    override fun duration(): Duration {
        return 6.times(60).toDuration()
    }

    override fun act(actor: Actor) {
        actor.urges.decreaseUrge("sleep", duration().getDuration().toDouble())
    }
}
