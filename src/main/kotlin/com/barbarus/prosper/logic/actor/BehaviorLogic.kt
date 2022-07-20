package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

class BehaviorLogic : Logic<Actor> {
    override fun process(actor: Actor) {
        val desiredBehaviors =
            actor.behaviors.filter { behavior -> actor.conditions.contains(behavior.trigger()) || behavior.trigger() == "*" }
        val unblockedBehaviors =
            desiredBehaviors.filterNot { behavior -> actor.conditions.contains(behavior.blocker()) }
        unblockedBehaviors.forEach { behavior -> behavior.act(actor) }
    }
}
