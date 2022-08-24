package com.goulash.core

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

/**
 * Executes activity logic and determines if an activity is either running, aborted or finished
 * and therefore executes lifecycle logic.
 */
class ActivityRunner {
    private var duration: Double = 0.0
    private var shouldAbort: Boolean = false

    fun `continue`(actor: Actor) {
        // @formatter:off
        when {
            containsAbortCondition(actor) -> { abort(actor); return }
            hasFinished() -> { finish(actor); return }
            shouldAbort -> { abort(actor); return }
            hasUnfulfilledRequirement(actor) -> return
        }
        // @formatter:on

        shouldAbort = !actor.activity.act(actor)
        countDown()

        if (hasFinished()) {
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

        duration = actor.activity.duration().asDouble()
        if (hasUnfulfilledRequirement(actor)) {
            return
        }

        // skip activity if duration is 0
        if (duration == 0.0) {
            return
        }

        shouldAbort = !actor.activity.act(actor)
        countDown()

        if (hasFinished()) {
            finish(actor)
            return
        }
    }

    private fun hasUnfulfilledRequirement(actor: Actor): Boolean {
        return actor.activity.requirements().isNotEmpty()
    }

    private fun finish(actor: Actor) {
        duration = 0.0
        actor.activity.onFinish(actor)
        actor.activity = IdleActivity()
    }

    private fun abort(actor: Actor) {
        duration = 0.0
        actor.activity.onAbort(actor)
        actor.activity = IdleActivity()
    }

    private fun containsAbortCondition(actor: Actor) =
        actor.conditions.any { actorCondition -> actor.activity.abortConditions().contains(actorCondition) }

    private fun isFinished(): Boolean {
        return duration <= 0
    }

    fun isRunning(): Boolean {
        return !isFinished()
    }

    /**
     * Decrease duration
     */
    private fun countDown() {
        duration--
    }

    private fun hasFinished(): Boolean {
        return duration <= 0
    }
}
