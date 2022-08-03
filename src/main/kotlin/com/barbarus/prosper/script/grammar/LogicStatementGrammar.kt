package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.logic.ContainerScriptContext
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

internal class LogicStatementGrammar : Grammar<List<ScriptStatement>>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val filter by regexToken("\\[(.*)\\]")
    private val contextMutationOperator by literalToken("::")
    private val identifier by regexToken("^[a-z]+")
    private val operationOperator by literalToken(".")
    private val digit by regexToken("^[\\d.\\d]+")
    private val rightPar by literalToken(")")
    private val leftPar by literalToken("(")
    private val endOfStatement by literalToken(";", ignore = true)

    private val operationParser by -operationOperator * identifier * -leftPar * (digit or identifier) * -rightPar map { (name, argument) ->
        ContainerScriptContext.Operation(name.text, argument.text)
    }
    private val mutationParser by -contextMutationOperator * identifier * -leftPar * identifier * -rightPar * operationParser map { (type, target, operation) ->
        ContainerScriptContext.ContextMutation(type.text, target.text, operation)
    }
    private val filterParser by filter use { text.removeSurrounding("[", "]") }
    private val contextCommandParser by identifier * optional(filterParser) * mutationParser
    private val statementParser by contextCommandParser map { (context, filter, mutation) ->
        ScriptStatement(
            context.text,
            filter ?: "",
            mutation.type,
            mutation.target,
            mutation.operation.name,
            mutation.operation.argument
        )
    }
    override val rootParser by separatedTerms(statementParser, endOfStatement)
}
