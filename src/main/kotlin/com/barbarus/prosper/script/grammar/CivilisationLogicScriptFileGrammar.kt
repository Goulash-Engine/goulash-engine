package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.barbarus.prosper.script.exception.SyntaxException
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

class CivilisationLogicScriptFileGrammar : Grammar<List<ScriptedLogic<Civilisation>>>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)
    private val leftBracket by literalToken("[")
    private val identifierTypeOperator by literalToken(":")
    private val identifier by regexToken("^[A-Za-z]+")
    private val rightBracket by literalToken("]")

    // private val listElement by regexToken("^[a-z]+")
    // private val listSyntax by literalToken("-")
    private val separator by literalToken("###", ignore = true)

    private val logicTypeParser by (identifier and skip(identifierTypeOperator) and identifier)
    private val sectionParser by (skip(leftBracket) and logicTypeParser and skip(rightBracket))
    private val logicParser by sectionParser map { (logicIdentifier, logicType) ->
        mapToScriptedLogic(logicIdentifier, logicType)
    }
    private val final: Parser<List<ScriptedLogic<Civilisation>>> by separatedTerms(logicParser, separator)

    override
    val rootParser: Parser<List<ScriptedLogic<Civilisation>>> by final

    private fun mapToScriptedLogic(identifier: TokenMatch, logicType: TokenMatch): ScriptedLogic<Civilisation> {
        if (identifier.text == "Logic") {
            if (logicType.text == "Civilisation") {
                return ScriptedLogic<Civilisation>()
            } else {
                throw SyntaxException("Unknown logic type: [<identifier>:${logicType.text}]")
            }
        } else {
            throw SyntaxException("Unknown section: [${identifier.text}:<type>]")
        }
    }
}
