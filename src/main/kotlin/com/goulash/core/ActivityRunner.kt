package com.goulash.core

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

/**
 * Executes activity logic and determines if an activity is either running, aborted or finished
 * and therefore executes lifecycle logic.
 */
class ActivityRunner(
    private val activityManager: ActivityManager = ActivityManager()
) {
    fun start(actor: Actor, newActivity: Activity) {
        activityManager.setActivity(actor, newActivity)

        if (containsAbortCondition(actor)) {
            abort(actor)
            return
        }

        if (hasUnfulfilledRequirement(actor)) {
            return
        }

        // skip activity if duration is 0
        if (activityManager.getDuration(actor) == 0.0) {
            return
        }

        activityManager.act(actor)

        if (hasEnded(actor)) {
            finish(actor)
            return
        }
    }

    fun `continue`(actor: Actor) {
        // @formatter:off
        when {
            hasEnded(actor) ->  return
            containsAbortCondition(actor) -> { abort(actor); return }
            activityManager.isAborted(actor) -> { abort(actor); return }
            hasUnfulfilledRequirement(actor) -> return
        }
        // @formatter:on

        activityManager.act(actor)

        if (hasEnded(actor)) {
            finish(actor)
            return
        }
    }


    private fun hasUnfulfilledRequirement(actor: Actor): Boolean {
        return activityManager.getActivity(actor)?.requirements()?.isNotEmpty() ?: true
    }

    private fun finish(actor: Actor) {
        activityManager.getActivity(actor)?.onFinish(actor)
        activityManager.clear(actor)
    }

    private fun abort(actor: Actor) {
        activityManager.getActivity(actor)?.onAbort(actor)
        activityManager.clear(actor)
    }

    private fun containsAbortCondition(actor: Actor) =
        actor.conditions.any { actorCondition ->
            activityManager.getActivity(actor)?.abortConditions()?.contains(actorCondition)
                ?: throw IllegalStateException("No activity for actor $actor has been found")
        }

    fun hasEnded(actor: Actor): Boolean {
        return activityManager.getDuration(actor) <= 0.0
    }

    /**
     * Decrease duration
     */
    private fun countDown(actor: Actor) {
        activityManager.countDown(actor)
    }
}
