package com.barbarus.prosper.logic.clan

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

/**
 * This logic will control the behaviour of an [Actor] according to it conditions.
 */
class BehaviorLogic : Logic<Actor> {
    override fun process(context: Actor) {
        val desiredBehaviors =
            context.behaviors.filter { behavior -> context.conditions.contains(behavior.trigger()) || behavior.trigger() == "*" }
        val unblockedBehaviors =
            desiredBehaviors.filterNot { behavior -> context.conditions.contains(behavior.blocker()) }
        unblockedBehaviors.forEach { behavior -> behavior.act(context) }
    }
}
