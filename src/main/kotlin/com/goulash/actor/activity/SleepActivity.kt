package com.goulash.actor.activity

import com.goulash.core.activity.Activity
import com.goulash.core.activity.Duration
import com.goulash.core.domain.Actor
import com.goulash.core.extension.toDuration

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
