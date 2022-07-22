package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

/**
 * This [Activity] controls the [Actor]'s behavior when it is awake.
 */
class AwakeActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("*")
    }

    override fun blockerCondition(): List<String> {
        return listOf("sleeping")
    }

    override fun act(actor: Actor) {
        actor.state.hunger += 0.1
    }
}
