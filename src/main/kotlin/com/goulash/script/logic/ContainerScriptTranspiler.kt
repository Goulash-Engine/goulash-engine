package com.goulash.script.logic

import com.goulash.core.domain.Actor
import com.goulash.script.domain.ContainerScript
import com.goulash.script.domain.ScriptStatement
import com.goulash.script.extension.TranspilerExtensions.tryScriptFilter

/**
 * Transpiles [ContainerScriptContext] to [ContainerScript]
 */
class ContainerScriptTranspiler {
    fun transpile(scriptContext: ContainerScriptContext): ContainerScript {
        return ContainerScript(scriptContext.head.name) { context ->
            val statements = scriptContext.statements
            statements.forEach { statement ->
                if (statement.context == "actors") {
                    val filteredActors = context.actors.tryScriptFilter(statement.filter)
                    if (statement.mutationType == "condition") {
                        val condition = statement.mutationTarget
                        when (statement.mutationOperation) {
                            "add" -> filteredActors.forEach { it.conditions.add(condition) }
                            "remove" -> filteredActors.forEach { it.conditions.remove(condition) }
                        }
                    }
                    if (statement.mutationType == "state") {
                        context.actors.forEach { initStateIfMissing(it, statement.mutationTarget) }
                        val stateProperty = statement.mutationTarget
                        val value = statement.mutationOperationArgument.toDouble()
                        when (statement.mutationOperation) {
                            "set" -> filteredActors.forEach { it.state[stateProperty] = value }
                            "plus" -> filteredActors.forEach { it.state[stateProperty] = it.state[stateProperty]!!.plus(value) }
                            "minus" -> filteredActors.forEach { it.state[stateProperty] = it.state[stateProperty]!!.minus(value) }
                        }
                    }
                    if (statement.mutationType == "urge") {
                        when (statement.mutationOperation) {
                            "plus" -> filteredActors.forEach { increaseUrge(it, statement) }
                            "minus" -> filteredActors.forEach { decreaseUrge(it, statement) }
                            "set" -> filteredActors.forEach { setUrge(it, statement) }
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
}
