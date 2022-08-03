package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.activity.Duration
import com.barbarus.prosper.core.extension.toDuration
import com.barbarus.prosper.script.domain.ScriptStatement

/**
 * Context of an activity loaded by script.
 * @param activity The name of the activity. This will be visible in the debugger
 * for actors having this activity active.
 * @param configurations Parsed by the [ActivityScriptGrammar]
 */
internal data class ActivityScriptContext(
    val activity: String,
    private val statements: Map<String, List<ScriptStatement>>,
    private val configurations: Map<String, List<String>>
) {
    val actLogic: List<ScriptStatement>
        get() = statements["act"] ?: listOf()
    val onFinish: List<ScriptStatement>
        get() = statements["on_finish"] ?: listOf()
    val onAbort: List<ScriptStatement>
        get() = statements["on_abort"] ?: listOf()
    val priority: Int
        get() = configurations["priority"]?.firstOrNull()?.toInt() ?: 0
    val duration: Duration
        get() = configurations["duration"]?.firstOrNull()?.toDouble()?.toDuration() ?: 1.0.toDuration()

    val triggerUrges: List<String>
        get() = configurations["trigger_urges"] ?: emptyList()
    val blockerConditions: List<String>
        get() = configurations["blocker_conditions"] ?: emptyList()

    val priorityConditions: List<String>
        get() = configurations["priority_conditions"] ?: emptyList()

    val abortConditions: List<String>
        get() = configurations["abort_conditions"] ?: emptyList()
}
