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

class CivilisationScriptLogicGrammar : Grammar<List<LogicStatement>>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val startLogicBlock by literalToken("{")
    private val contextMutationOperator by literalToken("::")
    private val identifier by regexToken("^[a-z]+")
    private val operationLeftPar by literalToken("(")
    private val operationRightPar by literalToken(")")
    private val endLogicBlock by literalToken("}")
    private val endOfStatement by literalToken(";", ignore = true)
    private val operationArgument by regexToken(".*")

    private val mutationParser by -contextMutationOperator * identifier * -operationLeftPar * (operationArgument or identifier) * -operationRightPar map { (identifier, argument) ->
        ContextMutation(identifier.text, argument.text)
    }

    private val subjectParser by -startLogicBlock * identifier * mutationParser * -endLogicBlock
    private val statementParser by subjectParser map { (contextId, mutation) ->
        LogicStatement(contextId.text, mutation.type, mutation.target)
    }

    override val rootParser by separatedTerms(statementParser, endOfStatement)

    internal data class ContextMutation(
        val type: String,
        val target: String
    )
}
