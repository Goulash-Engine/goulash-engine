package com.barbarus.prosper.processor.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.processor.Processor

class BehaviorProcessor : Processor<Actor> {
    override fun process(actor: Actor) {
        val desiredBehaviors =
            actor.behaviors.filter { behavior -> actor.conditions.contains(behavior.trigger()) || behavior.trigger() == "*" }
        val unblockedBehaviors =
            desiredBehaviors.filterNot { behavior -> actor.conditions.contains(behavior.blocker()) }
        unblockedBehaviors.forEach { behavior -> behavior.act(actor) }
    }
}
