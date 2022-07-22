package com.barbarus.prosper.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s behavior when it has no activity.
 */
class RestActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("tired", "exhausted")
    }

    override fun blockerCondition(): List<String> {
        return listOf("rested")
    }

    override fun act(actor: Actor) {
        actor.currentActivity = "resting"
    }
}
