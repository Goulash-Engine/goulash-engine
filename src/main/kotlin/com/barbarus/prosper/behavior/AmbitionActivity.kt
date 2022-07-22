package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.ResourceType

/**
 * This [Activity] controls the condition of an [Actor]s ambition.
 */
class AmbitionActivity : Activity {
    override fun triggerUrge(): List<String> {
        return listOf("*")
    }

    override fun blockerCondition(): List<String> {
        return listOf("sick", "exhausted")
    }

    /**
     * An [Actor] will become ambitios if his stamina is on peak performance or it
     * realises that no food is in it's inventory anymore
     */
    override fun act(actor: Actor) {
        if (actor.inventory().none { it.type == ResourceType.FOOD }) {
            actor.conditions.add("ambitious")
        }
    }
}
