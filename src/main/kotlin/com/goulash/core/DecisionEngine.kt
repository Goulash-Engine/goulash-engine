package com.goulash.core

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.script.loader.ScriptLoader

/**
 * This logic manages all the [Activity] objects an [Actor] owns. The urge level of an
 * actor is the driving factor of this logic. There can be only one [Activity] running for each [Actor].
 */
// TODO: ActivityEngine
class DecisionEngine {

    private val runningActivity = ActivityRunner()

    fun tick(actor: Actor) {
        if (hasGlobalBlockerCondition(actor)) return

        val priorityActivity = actor.activities.find { isPrioritizedActivity(it, actor) }

        if (priorityActivity != null) {
            priorityActivity.init(actor)
            runningActivity.start(priorityActivity)
        } else if (runningActivity.isFinished()) {
            setUrgentActivities(actor)
        }
        runningActivity.tick(actor)
    }

    private fun isPrioritizedActivity(activity: Activity, context: Actor) =
        context.conditions.any {
            activity.priorityConditions().contains(it)
        }

    private fun hasGlobalBlockerCondition(actor: Actor) =
        actor.conditions.any { ScriptLoader.getGlobalBlockingConditionsOrDefault().contains(it) }

    private fun setUrgentActivities(actor: Actor) {
        val highestUrgeValue = actor.urges.getAllUrges().maxByOrNull { it.value }?.value ?: 0
        val topUrges = actor.urges.getAllUrges().filter { it.value == highestUrgeValue }

        val urgentActivity = actor.activities
            .filter { matchesUrge(it, topUrges) }
            .filterNot { isBlocked(it, actor) }
            .minByOrNull { it.priority() }

        val wildcardActivity = actor.activities
            .filter { it.triggerUrges().contains("*") }
            .filterNot { isBlocked(it, actor) }
            .minByOrNull { it.priority() }

        if (urgentActivity != null) {
            urgentActivity.init(actor)
            this.runningActivity.start(urgentActivity)
        } else if (wildcardActivity != null) {
            wildcardActivity.init(actor)
            this.runningActivity.start(wildcardActivity)
        }
    }

    private fun matchesUrge(activity: Activity, topUrges: Map<String, Double>) =
        activity.triggerUrges().any { triggerUrge -> topUrges.contains(triggerUrge) }

    private fun isBlocked(activity: Activity, actor: Actor): Boolean {
        return activity.blockerConditions()
            .any { blockerCondition: String -> actor.conditions.contains(blockerCondition) }
    }

    private class ActivityRunner {
        private var activity: Activity = IdleActivity()
        private var duration: Int = 0

        fun tick(actor: Actor) {
            if (isRunning()) {
                if (containsAbortCondition(actor)) {
                    activity.onAbort(actor)
                    start(IdleActivity())
                    return
                }

                val shouldContinue = activity.act(actor)
                if (!shouldContinue) {
                    activity.onAbort(actor)
                    start(IdleActivity())
                    return
                }

                actor.currentActivity = activity.activity()
                val hasFinished = countDown()
                if (hasFinished) {
                    activity.onFinish(actor)
                    start(IdleActivity())
                }
            }
        }

        private fun containsAbortCondition(actor: Actor) =
            actor.conditions.any { actorCondition -> activity.abortConditions().contains(actorCondition) }

        fun start(activity: Activity) {
            this.activity = activity
            duration = activity.duration().asDouble().toInt()
        }

        fun isFinished(): Boolean {
            return duration <= 0
        }

        fun isRunning(): Boolean {
            return !isFinished()
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
