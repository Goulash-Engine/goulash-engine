package com.barbarus.prosper.script.grammar

import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class LogicScriptFileGrammar : Grammar<String>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val scriptKeyword by literalToken("logic")
    private val scriptName by regexToken("^[a-z_]+")

    private val scriptHeadParser by -scriptKeyword * scriptName
    // private val statementParser by scriptHeadParser

    /** ScriptStatementGrammar()*/

    override val rootParser by scriptHeadParser use { text } /*map { (scriptName, statements) ->
        ScriptedLogic<Civilisation>("foo") { context ->
            statements.forEach { statement ->
                if (statement.context == "actors") {
                    if (statement.mutationType == "urge") {
                        if (statement.mutationTarget == "eat") {
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
    }*/
}
