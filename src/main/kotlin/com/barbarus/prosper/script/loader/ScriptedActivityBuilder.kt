package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.domain.ParsedActivityContext
import com.barbarus.prosper.script.domain.ScriptedActivity
import com.barbarus.prosper.script.grammar.LogicScriptGrammar
import com.barbarus.prosper.script.logic.ActivityScriptContext

internal class ScriptedActivityBuilder {
    fun parse(context: ActivityScriptContext): ScriptedActivity {
        val logicGrammar = LogicScriptGrammar()

        val parsedContext = ParsedActivityContext(
            context.activity,
            context.triggerUrges,
            context.blockerConditions,
            context.abortConditions,
            context.priorityConditions,
            context.priority,
            context.duration,
            { actor -> true },
            { actor -> },
            { actor -> }
        )

        return ScriptedActivity(parsedContext)
    }
}
