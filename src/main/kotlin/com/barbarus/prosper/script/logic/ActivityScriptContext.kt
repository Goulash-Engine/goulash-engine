package com.barbarus.prosper.script.logic

internal data class ActivityScriptContext(
    val head: ScriptHead,
    private val options: Map<String, List<String>>
) {
    val triggerUrges: List<String>
        get() = options["trigger"] ?: emptyList()
    val blockerConditions: List<String>
        get() = options["blocker"] ?: emptyList()
}
