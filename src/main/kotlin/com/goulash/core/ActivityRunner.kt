package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

/**
 * Executes activity logic and determines if an activity is either running, aborted or finished
 * and therefore executes lifecycle logic.
 */
class ActivityRunner(
    private val activity: Activity,
    private var duration: Double = 0.0
) {
    fun run(actor: Actor) {
        if (isRunning()) {
            if (containsAbortCondition(actor, activity)) {
                abort(actor)
                return
            }

            val shouldContinue = activity.act(actor)
            if (!shouldContinue) {
                abort(actor)
                return
            }

            actor.currentActivity = activity.activity()
            val hasFinished = countDown()
            if (hasFinished) {
                activity.onFinish(actor)
                return
            }
        }
    }

    private fun abort(actor: Actor) {
        duration = 0.0
        activity.onAbort(actor)
    }

    private fun containsAbortCondition(actor: Actor, activity: Activity) =
        actor.conditions.any { actorCondition -> activity.abortConditions().contains(actorCondition) }

    private fun isFinished(): Boolean {
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
