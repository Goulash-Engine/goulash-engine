package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.script.loader.ScriptLoader

/**
 * Responsible for choosing the most relevant [Activity] for an actor.
 */
class ActivitySelector(
    private val activityCatalog: List<Activity> = ScriptLoader.getActivityScripts()
) {
    fun select(actor: Actor): Activity? {
        if (hasGlobalBlockerCondition(actor)) return null

        val priorityActivity = activityCatalog.find { isPrioritizedActivity(it, actor) }
        if (priorityActivity != null) {
            return priorityActivity
        }

        val urgentActivity = findUrgentActivity(actor)
        if (urgentActivity != null) {
            return urgentActivity
        }
        return null
    }

    private fun isPrioritizedActivity(activity: Activity, context: Actor) = context.conditions.any {
        activity.priorityConditions().contains(it)
    }

    /**
     * Checks if the [Actor] has any global blocker condition applied that prevents
     * it from executing any [Activity].
     */
    private fun hasGlobalBlockerCondition(actor: Actor) = actor.conditions.any { ScriptLoader.getGlobalBlockingConditionsOrDefault().contains(it) }

    private fun findUrgentActivity(actor: Actor): Activity? {
        val highestUrgeValue = actor.urges.getAllUrges().maxByOrNull { it.value }?.value ?: 0
        val topUrges = actor.urges.getAllUrges().filter { it.value == highestUrgeValue }
        val urgentActivity = activityCatalog.filter { matchesUrge(it, topUrges) }.filterNot { isBlocked(it, actor) }.minByOrNull { it.priority() }
        val wildcardActivity = activityCatalog.filter { it.triggerUrges().contains("*") }.filterNot { isBlocked(it, actor) }.minByOrNull { it.priority() }

        if (urgentActivity != null) {
            return urgentActivity
        } else if (wildcardActivity != null) {
            return wildcardActivity
        }
        return null
    }

    private fun matchesUrge(activity: Activity, topUrges: Map<String, Double>) = activity.triggerUrges().any { triggerUrge -> topUrges.contains(triggerUrge) }

    private fun isBlocked(activity: Activity, actor: Actor): Boolean {
        return activity.blockerConditions().any { blockerCondition: String -> actor.conditions.contains(blockerCondition) }
    }
}
