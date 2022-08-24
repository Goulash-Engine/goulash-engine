package com.goulash.core

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

/**
 * Executes activity logic and determines if an activity is either running, aborted or finished
 * and therefore executes lifecycle logic.
 */
class ActivityRunner {
    private val actorActivities: MutableMap<Actor, Activity> = mutableMapOf()
    private val activityDurations: MutableMap<Actor, Double> = mutableMapOf()
    private val activityAbortStates: MutableMap<Actor, Boolean> = mutableMapOf()
    fun start(actor: Actor, newActivity: Activity) {
        actorActivities[actor] = newActivity
        actor.activity = newActivity.activity()

        if (containsAbortCondition(actor)) {
            abort(actor)
            return
        }

        activityDurations[actor] = actorActivities[actor]!!.duration().asDouble()
        if (hasUnfulfilledRequirement(actor)) {
            return
        }

        // skip activity if duration is 0
        if (activityDurations[actor] == 0.0) {
            return
        }

        activityAbortStates[actor] = !actorActivities[actor]!!.act(actor)
        countDown(actor)

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
            activityAbortStates[actor]?: false -> { abort(actor); return }
            hasUnfulfilledRequirement(actor) -> return
        }
        // @formatter:on

        activityAbortStates[actor] = !actorActivities[actor]!!.act(actor)
        countDown(actor)

        if (hasEnded(actor)) {
            finish(actor)
            return
        }
    }


    private fun hasUnfulfilledRequirement(actor: Actor): Boolean {
        return actorActivities[actor]!!.requirements().isNotEmpty()
    }

    private fun finish(actor: Actor) {
        activityDurations[actor] = 0.0
        actorActivities[actor]!!.onFinish(actor)
        actorActivities[actor] = IdleActivity()
    }

    private fun abort(actor: Actor) {
        activityDurations[actor] = 0.0
        actorActivities[actor]!!.onAbort(actor)
        actorActivities[actor] = IdleActivity()
    }

    private fun containsAbortCondition(actor: Actor) =
        actor.conditions.any { actorCondition -> actorActivities[actor]!!.abortConditions().contains(actorCondition) }

    fun hasEnded(actor: Actor): Boolean {
        if (!activityDurations.containsKey(actor)) {
            return true
        }
        return activityDurations[actor]!! <= 0
    }

    /**
     * Decrease duration
     */
    private fun countDown(actor: Actor) {
        activityDurations[actor] = activityDurations[actor]!!.minus(1)
    }
}
