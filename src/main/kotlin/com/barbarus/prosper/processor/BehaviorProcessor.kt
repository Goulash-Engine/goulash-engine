package com.barbarus.prosper.processor

import com.barbarus.prosper.behavior.Behavior
import com.barbarus.prosper.core.domain.Actor

class BehaviorProcessor {
    fun process(actor: Actor, behaviors: List<Behavior>) {
        if (behaviors.any { behavior -> actor.conditions.contains(behavior.blocker()) }) {
            return
        }
        val desiredBehaviors = behaviors.filter { behavior -> actor.conditions.contains(behavior.trigger()) }
        desiredBehaviors.forEach { behavior -> behavior.act(actor) }
    }
}
