package com.barbarus.prosper.script.domain

import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.domain.Actor

internal class ScriptedActivity(
    private val parsedContext: ParsedActivityContext
) : Activity {

    override fun activity(): String {
        return parsedContext.activity
    }

    override fun triggerUrges(): List<String> {
        return parsedContext.triggerUrges
    }

    override fun blockerConditions(): List<String> {
        return parsedContext.blockerConditions
    }

    override fun abortConditions(): List<String> {
        return parsedContext.abortConditions
    }

    override fun priorityConditions(): List<String> {
        return parsedContext.priorityConditions
    }

    override fun priority(): Int {
        return parsedContext.priority
    }

    override fun duration(): Duration {
        return parsedContext.duration
    }

    override fun onFinish(actor: Actor) {
        parsedContext.onFinishLogic(actor)
    }

    override fun onAbort(actor: Actor) {
        parsedContext.onAbortLogic(actor)
    }

    override fun act(actor: Actor): Boolean {
        return parsedContext.actLogic(actor)
    }
}
