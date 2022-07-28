package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.barbarus.prosper.script.grammar.FilterGrammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd

/**
 * Transpiles [ScriptContext] to [ScriptedLogic]
 */
class ScriptTranspiler {
    private val filterGrammar = FilterGrammar()
    fun transpile(scriptContext: ScriptContext): ScriptedLogic<Civilisation> {
        return ScriptedLogic<Civilisation>(scriptContext.head.name) { context ->
            val statements = scriptContext.statements
            statements.forEach { statement ->
                if (statement.context == "actors") {
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

    private fun <T : Actor> List<T>.tryScriptFilter(filterStatement: String): List<T> {
        if (filterStatement.isEmpty()) return this
        val contextFilter = filterGrammar.parseToEnd(filterStatement)
        if (contextFilter.type == "state") {
            if (contextFilter.attribute == "urge") {
                if (contextFilter.operator == ">") {
                    return this.filter { it.state.health > contextFilter.argument.toDouble() }
                }
            }
        }
        return this
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
