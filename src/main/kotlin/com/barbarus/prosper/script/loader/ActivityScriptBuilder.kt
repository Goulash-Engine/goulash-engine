package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.domain.ActivityScript
import com.barbarus.prosper.script.domain.ParsedActivityContext
import com.barbarus.prosper.script.grammar.LogicScriptGrammar
import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.github.h0tk3y.betterParse.grammar.parseToEnd

internal class ActivityScriptBuilder {
    fun parse(context: ActivityScriptContext): ActivityScript {
        val logicGrammar = LogicScriptGrammar()

        val actLogic = logicGrammar.parseToEnd(context.actLogic)
        val onFinishLogic = logicGrammar.parseToEnd(context.onFinish)
        val onAbortLogic = logicGrammar.parseToEnd(context.onAbort)

        // val scriptedAct = transpiler.transpile(actLogic)
        // val scriptedOnFinish = transpiler.transpile(onFinishLogic)
        // val scriptedOnAbort = transpiler.transpile(onAbortLogic)

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

        return ActivityScript(parsedContext)
    }
}
