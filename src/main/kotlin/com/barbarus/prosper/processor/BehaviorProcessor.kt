package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Actor

class BehaviorProcessor {
    fun process(actor: Actor) {
        val desiredBehaviors =
            actor.behaviors.filter { behavior -> actor.conditions.contains(behavior.trigger()) || behavior.trigger() == "*" }
        val unblockedBehaviors =
            desiredBehaviors.filterNot { behavior -> actor.conditions.contains(behavior.blocker()) }
        unblockedBehaviors.forEach { behavior -> behavior.act(actor) }
    }
}
