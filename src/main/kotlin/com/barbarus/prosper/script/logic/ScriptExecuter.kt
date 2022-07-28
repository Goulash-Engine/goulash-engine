package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.script.domain.ScriptStatement

class ScriptExecuter {
    fun execute(context: Civilisation, statements: List<ScriptStatement>) {
        statements.forEach { statement ->
            if (statement.context == "actors") {
                if (statement.mutationType == "urge") {
                    when (statement.mutationOperation) {
                        "plus" -> context.actors.forEach { increaseUrge(it, statement) }
                        "minus" -> context.actors.forEach { decreaseUrge(it, statement) }
                    }
                }
            }
        }
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
