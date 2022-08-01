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
    private val logics: Map<String, String>,
    private val options: Map<String, List<String>>
) {
    val actLogic: String
        get() = logics["act"] ?: ""
    val onFinish: String
        get() = logics["on_finish"] ?: ""
    val onAbort: String
        get() = logics["on_abort"] ?: ""
    val priority: Int
        get() = options["priority"]?.firstOrNull()?.toInt() ?: 0
    val duration: Duration
        get() = options["duration"]?.firstOrNull()?.toDouble()?.toDuration() ?: 1.0.toDuration()

    val triggerUrges: List<String>
        get() = options["trigger_urges"] ?: emptyList()
    val blockerConditions: List<String>
        get() = options["blocker_conditions"] ?: emptyList()

    val priorityConditions: List<String>
        get() = options["priority_conditions"] ?: emptyList()

    val abortConditions: List<String>
        get() = options["abort_conditions"] ?: emptyList()
}
