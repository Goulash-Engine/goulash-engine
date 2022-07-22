package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

/**
 * This logic will control the behaviour of an [Actor] according to it conditions.
 */
class BehaviorLogic : Logic<Actor> {
    override fun process(context: Actor) {
        val desiredBehaviors =
            context.activities.filter { behavior ->
                (
                    context.conditions.any {
                        behavior.triggerUrge().contains(it)
                    } || behavior.triggerUrge() == listOf("*")
                    )
            }
        val unblockedBehaviors =
            desiredBehaviors.filterNot { behavior -> context.conditions.any { behavior.blockerCondition().contains(it) } }
        unblockedBehaviors.forEach { behavior -> behavior.act(context) }
    }
}
