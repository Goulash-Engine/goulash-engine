package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.exceptions.ActivityRedundancyException
import com.barbarus.prosper.core.logic.Logic

/**
 * This logic will check all [Activity] objects if they are executed by the required urge level.
 */
class ActivityLogic : Logic<Actor> {
    override fun process(context: Actor) {
        executeFreeActivities(context)
        if (context.urges.getUrges().isEmpty()) return
        executeUrgentActivities(context)
    }

    private fun executeUrgentActivities(context: Actor) {
        val topUrge = context.urges.getUrges().maxBy { it.value }
        if (context.activities.count { it.triggerUrge().contains(topUrge.key) } > 1) {
            throw ActivityRedundancyException("More than one activity with urge trigger found")
        }
        val urgentActivity = context.activities
            .filterNot {
                it.blockerCondition().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
            .find { it.triggerUrge().contains(topUrge.key) }

        urgentActivity?.let {
            it.act(context)
            context.currentActivity = it.activity()
        }
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
            .forEach {
                it.act(context)
                context.currentActivity = it.activity()
            }
    }
}
