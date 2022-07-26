package com.barbarus.prosper.script.parser

import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.exception.UnknownSectionException
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.zeroOrMore
import com.github.h0tk3y.betterParse.lexer.DefaultTokenizer
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.parseToEnd

class GlobalBlockerConditionScriptParser {
    fun parse(scriptData: String): GlobalBlockerCondition {
        val leftBracket = literalToken("[")
        val rightBracket = literalToken("]")
        val identifier = regexToken("^[A-Za-z]+")
        val listSyntax = literalToken("-")
        val listElement = regexToken("^[a-z]+")
        val newLine = literalToken("\n", ignore = true)
        val space = regexToken("\\s+", ignore = true)

        val tokenMatches = DefaultTokenizer(
            listOf(
                space,
                newLine,
                leftBracket,
                listElement,
                identifier,
                rightBracket,
                listSyntax
            )
        ).tokenize(scriptData)

        val section = skip(leftBracket) and identifier and skip(rightBracket)
        val list = skip(listSyntax) and listElement

        val parser =
            section and zeroOrMore(list) map { (section, elements) -> mapToGlobalBlockerCondition(section, elements) }

        return parser.parseToEnd(tokenMatches)
    }

    private fun mapToGlobalBlockerCondition(section: TokenMatch, elements: List<TokenMatch>): GlobalBlockerCondition {
        if (section.text == "GlobalBlocker") {
            return GlobalBlockerCondition(elements.map(TokenMatch::text))
        } else {
            throw UnknownSectionException("Unknown section: ${section.text}")
        }
    }
}
