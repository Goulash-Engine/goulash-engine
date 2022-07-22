package com.barbarus.prosper.logic.actor

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.logic.Logic

/**
 * This logic will check all [Activity] objects if they are executed by the required urge level.
 */
class ActivityLogic : Logic<Actor> {
    override fun process(context: Actor) {
        executeFreeActivities(context)
        if (context.urges.getUrges().isEmpty()) return
        executeUrgentActivities(context)
    }

    // TODO: set current activity
    private fun executeUrgentActivities(context: Actor) {
        val topUrge = context.urges.getUrges().maxBy { it.value }
        val urgentActivity = context.activities
            .filterNot {
                it.blockerCondition().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
            .find { it.triggerUrge().contains(topUrge.key) }
        urgentActivity?.act(context)
    }

    /**
     * These [Activity] objects will be executed without any urge trigger requirements.
     */
    private fun executeFreeActivities(context: Actor) {
        context.activities
            .filter { it.triggerUrge().contains("*") }
            .filterNot {
                it.blockerCondition().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
            .forEach { it.act(context) }
    }
}
