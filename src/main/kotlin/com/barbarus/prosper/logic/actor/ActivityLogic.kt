package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

/**
 * This logic will check all [Activity] objects if they are executed by the required urge level.
 */
class ActivityLogic : Logic<Actor> {
    override fun process(context: Actor) {
        val behaviors = context.behaviors
        val conditions = context.conditions
        val urges = context.urges.getUrges()
        if (urges.isEmpty()) return

        val topUrge = urges.maxBy { it.value }
        val urgentActivities = context.behaviors
            .filter { it.triggerUrge().contains(topUrge.key) }
            .filterNot {
                it.blockerCondition().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
        urgentActivities.forEach { it.act(context) }
    }
}
