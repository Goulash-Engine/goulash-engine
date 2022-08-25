package com.goulash.core

import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor

class ActivityManager {
    private val actorActivities: MutableMap<Actor, Activity> = mutableMapOf()
    private val activityDurations: MutableMap<Actor, Double> = mutableMapOf()
    private val activityAbortStates: MutableMap<Actor, Boolean> = mutableMapOf()

    fun setActivity(actor: Actor, activity: Activity) {
        actorActivities[actor] = activity
        activityDurations[actor] = activity.duration().asDouble()
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
