package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration

class ActivityScript(
    private val activity: String,
    private val configurations: Map<String, List<String>>,
    private val actLogic: (context: Actor) -> Boolean,
    private val onFinishLogic: (context: Actor) -> Unit,
    private val onAbortLogic: (context: Actor) -> Unit
) : Activity {

    override fun activity(): String {
        return activity
    }

    override fun triggerUrges(): List<String> {
        return configurations["trigger_urges"] ?: emptyList()
    }

    override fun blockerConditions(): List<String> {
        return configurations["blocker_conditions"] ?: emptyList()
    }

    override fun abortConditions(): List<String> {
        return configurations["abort_conditions"] ?: emptyList()
    }

    override fun priorityConditions(): List<String> {
        return configurations["priority_conditions"] ?: emptyList()
    }

    override fun priority(): Int {
        return configurations["priority"]?.firstOrNull()?.toInt() ?: 0
    }

    override fun duration(): Duration {
        return configurations["duration"]?.firstOrNull()?.toDouble()?.toDuration() ?: 1.0.toDuration()
    }

    override fun onFinish(actor: Actor) {
        onFinishLogic(actor)
    }

    override fun onAbort(actor: Actor) {
        onAbortLogic(actor)
    }

    override fun act(actor: Actor): Boolean {
        return actLogic(actor)
    }
}
