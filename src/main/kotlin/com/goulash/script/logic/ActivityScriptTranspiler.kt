package com.goulash.script.logic

import com.goulash.core.domain.Actor
import com.goulash.script.domain.ActivityScript
import com.goulash.script.domain.ScriptStatement
import com.goulash.script.extension.TranspilerExtensions.tryScriptFilter

/**
 * Transpiles [ActivityScriptContext] to [ActivityScript]
 */
class ActivityScriptTranspiler {
    fun transpile(scriptContext: ActivityScriptContext): ActivityScript {
        val actStatements = scriptContext.statements["act"] ?: emptyList()
        val initStatements = scriptContext.statements["init"] ?: emptyList()
        val onFinishStatements = scriptContext.statements["on_finish"] ?: emptyList()
        val onAbortStatements = scriptContext.statements["on_abort"] ?: emptyList()

        return ActivityScript(
            scriptContext.activity,
            scriptContext.configurations,
            { context -> transpileStatements(context, initStatements)},
            { context -> transpileStatements(context, actStatements); true },
            { context -> transpileStatements(context, onFinishStatements) },
            { context -> transpileStatements(context, onAbortStatements) }
        )
    }

    private fun transpileStatements(context: Actor, statements: List<ScriptStatement>) {
        statements.forEach { statement ->
            if (statement.context == "actor") {
                if (context.tryScriptFilter(statement.filter) != null) {
                    if (statement.mutationType == "condition") {
                        val condition = statement.mutationTarget
                        when (statement.mutationOperation) {
                            "add" -> context.conditions.add(condition)
                            "remove" -> context.conditions.remove(condition)
                        }
                    }
                    if (statement.mutationType == "state") {
                        initStateIfMissing(context, statement.mutationTarget)
                        val stateProperty = statement.mutationTarget
                        val value = statement.mutationOperationArgument.toDouble()
                        when (statement.mutationOperation) {
                            "set" -> context.state[stateProperty] = value
                            "plus" -> context.state[stateProperty] = context.state[stateProperty]!!.plus(value)
                            "minus" -> context.state[stateProperty] = context.state[stateProperty]!!.minus(value)
                        }
                    }
                    if (statement.mutationType == "urge") {
                        when (statement.mutationOperation) {
                            "plus" -> increaseUrge(context, statement)
                            "minus" -> decreaseUrge(context, statement)
                            "set" -> setUrge(context, statement)
                        }
                    }
                }
            }
        }
    }

    private fun initStateIfMissing(context: Actor, mutationTarget: String) {
        if (!context.state.containsKey(mutationTarget)) {
            context.state[mutationTarget] = 0.0
        }
    }

    private fun setUrge(actor: Actor, statement: ScriptStatement) {
        actor.urges.stopUrge(statement.mutationTarget)
        val argValue = statement.mutationOperationArgument.toDouble()
        if (argValue <= 0.0) {
            return
        }
        actor.urges.increaseUrge(
            statement.mutationTarget,
            argValue
        )
    }

    private fun increaseUrge(actor: Actor, statement: ScriptStatement) {
        actor.urges.increaseUrge(
            statement.mutationTarget,
            statement.mutationOperationArgument.toDouble()
        )
    }

    private fun decreaseUrge(actor: Actor, statement: ScriptStatement) {
        actor.urges.decreaseUrge(
            statement.mutationTarget,
            statement.mutationOperationArgument.toDouble()
        )
    }

    companion object {
        private const val SETTER_PARAM = 1
    }
}
