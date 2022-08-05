package com.barbarus.prosper.script.logic

import com.barbarus.prosper.script.domain.ScriptStatement

/**
 * Context of an activity loaded by script.
 * @param activity The name of the activity. This will be visible in the debugger
 * for actors having this activity active.
 * @param configurations Parsed by the [ActivityScriptGrammar]
 * @param statements Parsed by the [ActivityScriptGrammar]
 */
data class ActivityScriptContext(
    val activity: String,
    val statements: Map<String, List<ScriptStatement>>,
    val configurations: Map<String, List<String>>
)
