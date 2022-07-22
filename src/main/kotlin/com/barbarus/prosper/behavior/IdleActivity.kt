package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s behavior when it has no activity.
 */
class IdleActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("*")
    }

    override fun blockerCondition(): List<String> {
        return listOf()
    }

    override fun act(actor: Actor) {
        if (actor.currentActivity.isBlank()) {
            actor.urges.increaseUrge("work", 0.5)
        }
    }
}
