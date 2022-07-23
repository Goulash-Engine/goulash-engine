package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic

/**
 * Changes the condition of an [Actor] depending on factors.
 */
class ConditionLogic : Logic<Actor> {
    override fun process(context: Actor) {
        simulateMalnourishment(context)
    }

    private fun simulateMalnourishment(context: Actor) {
        val eatUrge = context.urges.getUrges()["eat"]

        if (eatUrge != null) {
            if (eatUrge < 100.0) {
                context.conditions.remove("malnourished")
            } else {
                context.conditions.add("malnourished")
            }
        }
    }
}
