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
        executeFreeActivities(context)
        if (urges.isEmpty()) return
        executeUrgentActivities(urges, context)
    }

    private fun executeUrgentActivities(
        urges: Map<String, Double>,
        context: Actor
    ) {
        val topUrge = urges.maxBy { it.value }
        val urgentActivities = context.behaviors
            .filter { it.triggerUrge().contains(topUrge.key) }
            .filterNot {
                it.blockerCondition().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
        urgentActivities.forEach { it.act(context) }
    }

    /**
     * These [Activity] objects will be executed without any urge trigger requirements.
     */
    private fun executeFreeActivities(context: Actor) {
        context.behaviors
            .filter { it.triggerUrge().contains("*") }
            .filterNot {
                it.blockerCondition().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
            .forEach { it.act(context) }
    }
}
