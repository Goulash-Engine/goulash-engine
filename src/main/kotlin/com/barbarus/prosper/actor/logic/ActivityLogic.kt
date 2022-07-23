package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.actor.activity.IdleActivity
import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.exceptions.ActivityRedundancyException
import com.barbarus.prosper.core.logic.Logic

/**
 * This logic manages all the [Activity] objects an [Actor] owns. The urge level of an
 * actor is the driving factor of this logic. There can be only one [Activity] running for each [Actor].
 */
class ActivityLogic : Logic<Actor> {

    private val currentActivity = RunningActivity()

    override fun process(context: Actor) {
        if (hasGlobalBlockerCondition(context)) return

        if (currentActivity.hasRunningActivity()) {
            currentActivity.act(context)
        } else {
            executeFreeActivities(context)

            if (context.urges.getUrges().isEmpty()) return
            executeUrgentActivities(context)
        }
    }

    private fun hasGlobalBlockerCondition(context: Actor) =
        context.conditions.any { ConditionLogic.GOBAL_BLOCKING_CONDITION.contains(it) }

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

        fun act(actor: Actor) {
            if (hasRunningActivity()) {
                if (containsAbortCondition(actor)) activity = IdleActivity()
                activity.act(actor)
                actor.currentActivity = activity.activity()
                val hasFinished = countDown()
                if (hasFinished) {
                    activity.onFinish(actor)
                    activity = IdleActivity()
                }
            }
        }

        private fun containsAbortCondition(actor: Actor) =
            actor.conditions.any { actorCondition -> activity.abortConditions().contains(actorCondition) }

        fun activate(activity: Activity) {
            this.activity = activity
            duration = activity.duration().getDuration()
        }

        fun hasRunningActivity(): Boolean {
            return duration > 0
        }

        /**
         * Decrease duration
         * @return true if duration has finished
         */
        private fun countDown(): Boolean {
            duration--
            return duration <= 0
        }
    }
}
