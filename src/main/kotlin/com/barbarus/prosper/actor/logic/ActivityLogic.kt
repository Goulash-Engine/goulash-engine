package com.barbarus.prosper.actor.logic

import com.barbarus.prosper.actor.activity.IdleActivity
import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.logic.Logic
import org.slf4j.LoggerFactory

/**
 * This logic manages all the [Activity] objects an [Actor] owns. The urge level of an
 * actor is the driving factor of this logic. There can be only one [Activity] running for each [Actor].
 */
class ActivityLogic : Logic<Actor> {

    private val currentActivity = RunningActivity()

    override fun process(context: Actor) {
        if (hasGlobalBlockerCondition(context)) return

        val priorityActivity = context.activities.find { isPrioritizedActivity(it, context) }

        if (priorityActivity != null) {
            currentActivity.set(priorityActivity)
            currentActivity.act(context)
        } else {
            if (currentActivity.hasRunningActivity()) {
                currentActivity.act(context)
            } else {
                executeUrgentActivities(context)
            }
        }
    }

    private fun isPrioritizedActivity(activity: Activity, context: Actor) =
        context.conditions.any {
            activity.priorityConditions().contains(it)
        }

    private fun hasGlobalBlockerCondition(actor: Actor) =
        actor.conditions.any { ConditionLogic.GLOBAL_BLOCKING_CONDITION.contains(it) }

    private fun executeUrgentActivities(actor: Actor) {
        val highestUrgeValue = actor.urges.getUrges().maxByOrNull { it.value }?.value ?: 0
        val topUrges = actor.urges.getUrges().filter { it.value == highestUrgeValue }

        val urgentActivity = actor.activities
            .filter { matchesUrge(it, topUrges) }
            .filterNot { isBlocked(it, actor) }
            .minByOrNull { it.priority() }

        val wildcardActivity = actor.activities
            .filter { it.triggerUrges().contains("*") }
            .filterNot { isBlocked(it, actor) }
            .minByOrNull { it.priority() }

        if (urgentActivity != null) {
            this.currentActivity.set(urgentActivity)
            this.currentActivity.act(actor)
        } else if (wildcardActivity != null) {
            this.currentActivity.set(wildcardActivity)
            this.currentActivity.act(actor)
        }
    }

    private fun matchesUrge(activity: Activity, topUrges: Map<String, Double>) =
        activity.triggerUrges().any { triggerUrge -> topUrges.contains(triggerUrge) }

    private fun isBlocked(activity: Activity, actor: Actor): Boolean {
        return activity.blockerConditions()
            .any { blockerCondition: String -> actor.conditions.contains(blockerCondition) }
    }

    private class RunningActivity {
        private var activity: Activity = IdleActivity()
        private var duration: Int = 0

        fun act(actor: Actor) {
            if (hasRunningActivity()) {
                if (containsAbortCondition(actor)) {
                    activity.onAbort(actor)
                    set(IdleActivity())
                }
                activity.act(actor)
                actor.currentActivity = activity.activity()
                val hasFinished = countDown()
                if (hasFinished) {
                    activity.onFinish(actor)
                    set(IdleActivity())
                }
            }
        }

        private fun containsAbortCondition(actor: Actor) =
            actor.conditions.any { actorCondition -> activity.abortConditions().contains(actorCondition) }

        fun set(activity: Activity) {
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

        companion object {
            private val LOG = LoggerFactory.getLogger(this::class.java.name)
        }
    }
}
