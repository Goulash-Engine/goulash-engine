package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.domain.ParsedActivityContext
import com.barbarus.prosper.script.domain.ScriptedActivity
import com.barbarus.prosper.script.grammar.LogicScriptGrammar
import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.barbarus.prosper.script.logic.ContainerScriptTranspiler
import com.github.h0tk3y.betterParse.grammar.parseToEnd

internal class ScriptedActivityBuilder {
    fun parse(context: ActivityScriptContext): ScriptedActivity {
        val logicGrammar = LogicScriptGrammar()
        val transpiler = ContainerScriptTranspiler()

        val actLogic = logicGrammar.parseToEnd(context.actLogic)
        val onFinishLogic = logicGrammar.parseToEnd(context.onFinish)
        val onAbortLogic = logicGrammar.parseToEnd(context.onAbort)

        val scriptedAct = transpiler.transpile(actLogic)
        val scriptedOnFinish = transpiler.transpile(onFinishLogic)
        val scriptedOnAbort = transpiler.transpile(onAbortLogic)

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
