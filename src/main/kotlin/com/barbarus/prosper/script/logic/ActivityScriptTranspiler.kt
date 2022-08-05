package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.script.domain.ActivityScript
import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.extension.TranspilerExtensions.tryScriptFilter

/**
 * Transpiles [ActivityScriptContext] to [ActivityScript]
 */
class ActivityScriptTranspiler {
    fun transpile(scriptContext: ActivityScriptContext): ActivityScript {
        val actStatements = scriptContext.statements["act"] ?: emptyList()
        val onFinishStatements = scriptContext.statements["on_finish"] ?: emptyList()
        val onAbortStatements = scriptContext.statements["on_abort"] ?: emptyList()

        return ActivityScript(
            scriptContext.activity,
            scriptContext.configurations,
            { context -> transpileStatements(context, actStatements); true },
            { context -> transpileStatements(context, onFinishStatements) },
            { context -> transpileStatements(context, onAbortStatements) }
        )
    }

    private fun transpileStatements(context: Actor, statements: List<ScriptStatement>) {
        statements.forEach { statement ->
            if (statement.context == "actor") {
                if (statement.mutationType == "state") {
                    if (context.tryScriptFilter(statement.filter) != null) {
                        initStateIfMissing(context, statement.mutationTarget)
                        val stateProperty = statement.mutationTarget
                        val value = statement.mutationOperationArgument.toDouble()
                        when (statement.mutationOperation) {
                            "set" -> context.state[stateProperty] = value
                            "plus" -> context.state[stateProperty] = context.state[stateProperty]!!.plus(value)
                            "minus" -> context.state[stateProperty] = context.state[stateProperty]!!.minus(value)
                        }
                    }
                }
                if (statement.mutationType == "urge") {
                    if (context.tryScriptFilter(statement.filter) != null) {
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
        actor.urges.stopUrge("eat")
        actor.urges.increaseUrge(
            statement.mutationTarget,
            statement.mutationOperationArgument.toDouble()
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
