package com.goulash.core

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

/**
 * Executes activity logic and determines if an activity is either running, aborted or finished
 * and therefore executes lifecycle logic.
 */
class ActivityRunner {
    // TODO: create map to associate activity with actor
    private val activityDurations: MutableMap<Actor, Double> = mutableMapOf()
    private val activityAbortStates: MutableMap<Actor, Boolean> = mutableMapOf()

    fun `continue`(actor: Actor) {
        // @formatter:off
        when {
            hasEnded(actor) ->  return
            containsAbortCondition(actor) -> { abort(actor); return }
            activityAbortStates[actor]?: false -> { abort(actor); return }
            hasUnfulfilledRequirement(actor) -> return
        }
        // @formatter:on

        activityAbortStates[actor] = !actor.activity.act(actor)
        countDown(actor)

        if (hasEnded(actor)) {
            finish(actor)
            return
        }
    }

    fun start(actor: Actor, newActivity: Activity) {
        actor.activity = newActivity

        if (containsAbortCondition(actor)) {
            abort(actor)
            return
        }

        activityDurations[actor] = actor.activity.duration().asDouble()
        if (hasUnfulfilledRequirement(actor)) {
            return
        }

        // skip activity if duration is 0
        if (activityDurations[actor] == 0.0) {
            return
        }

        activityAbortStates[actor] = !actor.activity.act(actor)
        countDown(actor)

        if (hasEnded(actor)) {
            finish(actor)
            return
        }
    }

    private fun hasUnfulfilledRequirement(actor: Actor): Boolean {
        return actor.activity.requirements().isNotEmpty()
    }

    private fun finish(actor: Actor) {
        activityDurations[actor] = 0.0
        actor.activity.onFinish(actor)
        actor.activity = IdleActivity()
    }

    private fun abort(actor: Actor) {
        activityDurations[actor] = 0.0
        actor.activity.onAbort(actor)
        actor.activity = IdleActivity()
    }

    private fun containsAbortCondition(actor: Actor) =
        actor.conditions.any { actorCondition -> actor.activity.abortConditions().contains(actorCondition) }

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
