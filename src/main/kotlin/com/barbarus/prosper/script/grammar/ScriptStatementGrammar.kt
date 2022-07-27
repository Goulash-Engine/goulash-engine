package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.domain.LogicStatement
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class ScriptStatementGrammar : Grammar<List<LogicStatement>>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val startLogicBlock by literalToken("{")
    private val contextMutationOperator by literalToken("::")
    private val operationOperator by literalToken(".")
    private val digit by regexToken("^[\\d.\\d]+")
    private val identifier by regexToken("^[a-z]+")
    private val rightPar by literalToken(")")
    private val leftPar by literalToken("(")
    private val endLogicBlock by literalToken("}")
    private val endOfStatement by literalToken(";", ignore = true)

    /**
     * .plus(1)
     */
    private val operationParser by -operationOperator * identifier * -leftPar * (digit or identifier) * -rightPar map { (name, argument) ->
        Operation(name.text, argument.text)
    }

    /**
     * ::urge(eat)[.plus(1)]
     */
    private val mutationParser by -contextMutationOperator * identifier * -leftPar * identifier * -rightPar * operationParser map { (type, target, operation) ->
        ContextMutation(type.text, target.text, operation)
    }

    /**
     * { actors[::urge(eat)[.plus(1)]]]; }
     */
    private val contextCommandParser by identifier * mutationParser
    private val statementParser by contextCommandParser map { (context, mutation) ->
        LogicStatement(
            context.text,
            mutation.type,
            mutation.target,
            mutation.operation.name,
            mutation.operation.argument
        )
    }

    override val rootParser by -startLogicBlock * separatedTerms(statementParser, endOfStatement) * -endLogicBlock

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
