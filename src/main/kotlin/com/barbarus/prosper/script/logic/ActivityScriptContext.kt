package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.extension.toDuration

/**
 * Context of an activity loaded by script.
 * @param activity The name of the activity. This will be visible in the debugger
 * for actors having this activity active.
 * @param options Parsed by the [ActivityScriptGrammar]
 */
internal data class ActivityScriptContext(
    val activity: String,
    private val activityDuration: String,
    private val options: Map<String, List<String>>
) {
    val duration: Duration
        get() = activityDuration.toDouble().toDuration()

    val triggerUrges: List<String>
        get() = options["trigger"] ?: emptyList()
    val blockerConditions: List<String>
        get() = options["blocker"] ?: emptyList()

    val priorityConditions: List<String>
        get() = options["priority"] ?: emptyList()
}
