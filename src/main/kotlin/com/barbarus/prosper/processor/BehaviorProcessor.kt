package com.barbarus.prosper.processor

import com.barbarus.prosper.behavior.Behavior
import com.barbarus.prosper.core.domain.Actor

class BehaviorProcessor {
    fun process(actor: Actor, behaviors: List<Behavior>) {
        val desiredBehaviors = behaviors.filter { behavior -> actor.desires.contains(behavior.trigger()) }
        desiredBehaviors.forEach { behavior -> behavior.act(actor) }
    }
}
