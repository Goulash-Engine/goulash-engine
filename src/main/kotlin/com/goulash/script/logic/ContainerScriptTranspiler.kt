package com.goulash.script.logic

import com.goulash.core.domain.Actor
import com.goulash.core.domain.Container
import com.goulash.script.domain.ContainerScript
import com.goulash.script.domain.ScriptStatement
import com.goulash.script.extension.TranspilerExtensions.tryScriptFilter

/**
 * Transpiles [ContainerScriptContext] to [ContainerScript]
 */
class ContainerScriptTranspiler {
    fun transpile(scriptContext: ContainerScriptContext): ContainerScript {
        val containerStatements = scriptContext.statements["container"] ?: emptyList()
        val initStatements = scriptContext.statements["init"] ?: emptyList()

        return ContainerScript(
            scriptContext.head.name,
            { container -> transpileStatements(container, containerStatements) },
            { container -> transpileStatements(container, initStatements) }
        )
    }

    private fun transpileStatements(container: Container, statements: List<ScriptStatement>) {
        statements.forEach { statement ->
            if (statement.context == "actors") {
                container.mutateActors { actors ->
                    val filteredActors = actors.tryScriptFilter(statement.filter)
                    if (statement.mutationType == "condition") {
                        val condition = statement.mutationTarget
                        when (statement.mutationOperation) {
                            "add" -> filteredActors.forEach { it.conditions.add(condition) }
                            "remove" -> filteredActors.forEach { it.conditions.remove(condition) }
                        }
                    }
                    if (statement.mutationType == "state") {
                        filteredActors.forEach { initStateIfMissing(it, statement.mutationTarget) }
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
        actor.urges.stopUrge(statement.mutationTarget)
        val argValue = statement.mutationOperationArgument.toDouble()
        if (argValue <= 0.0) {
            return
        }
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
