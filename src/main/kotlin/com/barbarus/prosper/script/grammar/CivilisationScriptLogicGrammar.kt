package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.domain.LogicStatement
import com.github.h0tk3y.betterParse.combinators.map
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
    private val callOperator by literalToken(".")
    private val identifier by regexToken("^[a-z]+")
    private val stringArgument by regexToken("\"+.*\"+")
    private val endLogicBlock by literalToken("}")
    private val startCallParenthesis by literalToken("(")
    private val endCallParenthesis by literalToken(")")
    private val endOfStatement by literalToken(";")

    private val callParser by -startCallParenthesis * stringArgument * -endCallParenthesis
    private val subjectParser by -startLogicBlock * identifier * -callOperator * identifier * callParser * -endLogicBlock
    private val statementParser by subjectParser map { (subject, property, callArgument) ->
        mapToStatement(subject.text, property.text, callArgument.text)
    }

    override val rootParser by separatedTerms(statementParser, endOfStatement)

    private fun mapToStatement(subject: String, property: String, callArgument: String) =
        LogicStatement(subject, property, callArgument)
}
