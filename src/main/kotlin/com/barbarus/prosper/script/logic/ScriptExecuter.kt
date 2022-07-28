package com.barbarus.prosper.script.logic

import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.script.domain.ScriptStatement

class ScriptExecuter {
    fun execute(context: Civilisation, statements: List<ScriptStatement>) {
        statements.forEach { statement ->
            if (statement.context == "actors") {
                if (statement.mutationType == "urge") {
                    if (statement.mutationOperation == "plus") {
                        context.actors.forEach {
                            it.urges.increaseUrge(
                                statement.mutationTarget,
                                statement.mutationOperationArgument.toDouble()
                            )
                        }
                    }
                }
            }
        }
    }
}
