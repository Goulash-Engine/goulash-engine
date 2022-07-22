package com.barbarus.prosper.actor.activity

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s activity when it has no activity.
 */
class IdleActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("*")
    }

    override fun blockerCondition(): List<String> {
        return listOf()
    }

    override fun activity(): String {
        return "idle"
    }

    override fun act(actor: Actor) {
        if (actor.currentActivity.isBlank()) {
            actor.urges.increaseUrge("work", 0.5)
        }
    }
}
