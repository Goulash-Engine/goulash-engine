package com.goulash.core

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

/**
 * Executes activity logic and determines if an activity is either running, aborted or finished
 * and therefore executes lifecycle logic.
 */
class ActivityRunner {
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
