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

    private val callParser by -operationLeftPar * (operationArgument or identifier) * -operationRightPar
    private val subjectParser by -startLogicBlock * identifier * -contextMutationOperator * identifier * callParser * -endLogicBlock
    private val statementParser by subjectParser map { (contextId, operationId, operationArgument) ->
        mapToStatement(contextId.text, operationId.text, operationArgument.text)
    }

    override val rootParser by separatedTerms(statementParser, endOfStatement)

    private fun mapToStatement(contextId: String, operationId: String, operationArgument: String) =
        LogicStatement(contextId, operationId, operationArgument)
}
