package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Behavior] controls the [Actor]'s behavior when it has no activity.
 */
class RestBehavior : Behavior {
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
