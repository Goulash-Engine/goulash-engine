package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.script.loader.ScriptLoader

/**
 * Responsible for choosing the most relevant [Activity] for an actor.
 */
class ActivityManager {

    private val runningActivity = ActivityRunner()

    fun tick(actor: Actor) {
        if (hasGlobalBlockerCondition(actor)) return

        val priorityActivity = actor.activities.find { isPrioritizedActivity(it, actor) }

        if (priorityActivity != null) {
            priorityActivity.init(actor)
            runningActivity.start(priorityActivity)
            actor.activity = priorityActivity
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
            actor.activity = urgentActivity
        } else if (wildcardActivity != null) {
            wildcardActivity.init(actor)
            this.runningActivity.start(wildcardActivity)
            actor.activity = wildcardActivity
        }
    }

    private fun matchesUrge(activity: Activity, topUrges: Map<String, Double>) =
        activity.triggerUrges().any { triggerUrge -> topUrges.contains(triggerUrge) }

    private fun isBlocked(activity: Activity, actor: Actor): Boolean {
        return activity.blockerConditions()
            .any { blockerCondition: String -> actor.conditions.contains(blockerCondition) }
    }
}
