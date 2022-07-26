package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.domain.ListConfiguration
import com.barbarus.prosper.script.exception.UnknownSectionException
import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.combinators.zeroOrMore
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.TokenMatch
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

class ListConfigurationGrammar : Grammar<List<ListConfiguration>>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)
    private val leftBracket by literalToken("[")
    private val listElement by regexToken("^[a-z]+")
    private val identifier by regexToken("^[A-Za-z]+")
    private val rightBracket by literalToken("]")
    private val listSyntax by literalToken("-")
    private val separator by literalToken("###", ignore = true)

    private val sectionParser by skip(leftBracket) and identifier and skip(rightBracket)
    private val elementParser by skip(listSyntax) and listElement
    private val listConfigurationParser by sectionParser and zeroOrMore(elementParser) map { (section, elements) ->
        mapToListConfiguration(section, elements)
    }
    private val final by separatedTerms(listConfigurationParser, separator)

    override val rootParser: Parser<List<ListConfiguration>> by final

    private fun mapToListConfiguration(section: TokenMatch, elements: List<TokenMatch>): ListConfiguration {
        if (section.text == "GlobalBlocker") {
            return GlobalBlockerCondition(elements.map(TokenMatch::text))
        } else {
            throw UnknownSectionException("Unknown section: [${section.text}]")
        }
    }
}
