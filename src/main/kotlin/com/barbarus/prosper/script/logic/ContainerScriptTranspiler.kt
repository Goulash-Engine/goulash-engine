package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.script.domain.ContainerScript
import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.extension.TranspilerExtensions.tryScriptFilter

/**
 * Transpiles [ContainerScriptContext] to [ContainerScript]
 */
class ContainerScriptTranspiler {
    fun transpile(scriptContext: ContainerScriptContext): ContainerScript {
        return ContainerScript(scriptContext.head.name) { context ->
            val statements = scriptContext.statements
            statements.forEach { statement ->
                if (statement.context == "actors") {
                    if (statement.mutationType == "state") {
                        val actors = context.actors.tryScriptFilter(statement.filter)
                        val stateProperty = statement.mutationTarget
                        val value = statement.mutationOperationArgument.toDouble()
                        when (statement.mutationOperation) {
                            "set" -> actors.forEach { it.state[stateProperty] = value }
                            "plus" -> actors.forEach { it.state[stateProperty] = it.state[stateProperty]!!.plus(value) }
                            "minus" -> actors.forEach { it.state[stateProperty] = it.state[stateProperty]!!.minus(value) }
                        }
                    }
                    if (statement.mutationType == "urge") {
                        val actors = context.actors.tryScriptFilter(statement.filter)
                        when (statement.mutationOperation) {
                            "plus" -> actors.forEach { increaseUrge(it, statement) }
                            "minus" -> actors.forEach { decreaseUrge(it, statement) }
                            "set" -> actors.forEach { setUrge(it, statement) }
                        }
                    }
                }
            }
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
