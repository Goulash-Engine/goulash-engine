package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.actor.activity.Activity
import com.barbarus.prosper.actor.activity.IdleActivity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.exceptions.ActivityRedundancyException
import com.barbarus.prosper.core.logic.Logic

/**
 * This logic will check all [Activity] objects if they are executed by the required urge level.
 */
class ActivityLogic : Logic<Actor> {

    private val currentActivity = RunningActivity()

    override fun process(context: Actor) {
        if (currentActivity.hasRunningActivity()) {
            currentActivity.act(context)
        } else {
            executeFreeActivities(context)

            if (context.urges.getUrges().isNotEmpty()) {
                executeUrgentActivities(context)
            }
        }
    }

    private fun executeUrgentActivities(context: Actor) {
        val topUrge = context.urges.getUrges().maxBy { it.value }
        if (context.activities.count { it.triggerUrges().contains(topUrge.key) } > 1) {
            throw ActivityRedundancyException("More than one activity with urge trigger found")
        }
        val urgentActivity = context.activities
            .filterNot {
                it.blockerConditions().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }
            .find { it.triggerUrges().contains(topUrge.key) }

        urgentActivity?.let {
            currentActivity.activate(it)
            currentActivity.act(context)
        }
    }

    /**
     * These [Activity] objects will be executed without any urge trigger requirements.
     */
    private fun executeFreeActivities(context: Actor) {
        val freeActivities = context.activities
            .filter { it.triggerUrges().contains("*") }
            .filterNot {
                it.blockerConditions().any { blockerCondition: String -> context.conditions.contains(blockerCondition) }
            }

        freeActivities.forEach {
            currentActivity.activate(it)
            currentActivity.act(context)
        }
    }

    private class RunningActivity {
        private var activity: Activity = IdleActivity()
        private var duration: Int = 0

        fun act(context: Actor) {
            if (hasRunningActivity()) {
                activity.act(context)
                countDown()
                context.currentActivity = activity.activity()
            }
        }

        fun activate(activity: Activity) {
            this.activity = activity
            duration = activity.duration()
        }

        fun hasRunningActivity(): Boolean {
            return duration > 0
        }

        private fun countDown() {
            duration--
            if (duration <= 0) {
                activity = IdleActivity()
            }
        }
    }
}
