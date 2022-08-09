package com.goulash.script.grammar

import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.goulash.script.domain.ScriptStatement

class LogicStatementGrammar : Grammar<List<ScriptStatement>>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val filter by regexToken("\\[(.*)\\]")
    private val contextMutationOperator by literalToken("::")
    private val identifier by regexToken("^[a-z]+")
    private val operationOperator by literalToken(".")
    private val digit by regexToken("^[\\d.\\d]+")
    private val rightPar by literalToken(")")
    private val leftPar by literalToken("(")
    private val endOfStatement by regexToken(";\\s*", ignore = true)

    private val operationParser by -optional(operationOperator) * optional(identifier) * -optional(leftPar) * optional((digit or identifier)) * -optional(rightPar) map { (name, argument) ->
        Operation(name?.text ?: "", argument?.text ?: "")
    }
    private val mutationParser by -contextMutationOperator * identifier * -optional(leftPar) * optional(identifier) * -optional(rightPar) * operationParser map { (type, target, operation) ->
        ContextMutation(type.text, target?.text ?: "", operation)
    }
    private val filterParser by optional(filter) use { this?.text?.removeSurrounding("[", "]") ?: "" }
    private val contextCommandParser by identifier * filterParser * mutationParser
    private val statementParser by separatedTerms(contextCommandParser, endOfStatement) map { statements ->
        statements.map { (context, filter, mutation) ->
            ScriptStatement(
                context.text,
                filter,
                mutation.type,
                mutation.target,
                mutation.operation.name,
                mutation.operation.argument
            )
        }
    }
    override val rootParser by statementParser

    internal data class ContextMutation(
        val type: String,
        val target: String,
        val operation: Operation
    )

    internal data class Operation(
        val name: String,
        val argument: String
    )
}
