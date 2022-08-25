package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

class ActivityManager {
    private val actorActivities: MutableMap<Actor, Activity> = mutableMapOf()
    private val activityDurations: MutableMap<Actor, Double> = mutableMapOf()
    private val activityAbortStates: MutableMap<Actor, Boolean> = mutableMapOf()

    fun clear(actor: Actor) {
        actorActivities.remove(actor)
        activityDurations.remove(actor)
        activityAbortStates.remove(actor)
    }

    fun act(actor: Actor) {
        val shouldContinue = getActivity(actor)?.act(actor)
        if (shouldContinue == false) {
            abortActivity(actor)
        }
        countDown(actor)
    }

    fun setActivity(actor: Actor, activity: Activity) {
        actor.activity = activity.activity()
        actorActivities[actor] = activity
        activityDurations[actor] = activity.duration().asDouble()
        activityAbortStates[actor] = false
    }

    fun getActivity(actor: Actor): Activity? {
        return actorActivities[actor]
    }

    fun getDuration(actor: Actor): Double {
        return activityDurations[actor] ?: 0.0
    }

    fun countDown(actor: Actor) {
        activityDurations[actor] = activityDurations[actor]?.minus(1) ?: throw IllegalStateException("No mapped duration for actor $actor")
    }

    fun abortActivity(actor: Actor) {
        activityAbortStates[actor] = true
    }

    fun isAborted(actor: Actor): Boolean {
        return activityAbortStates[actor] ?: true
    }

}
