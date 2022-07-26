package com.goulash.script.grammar

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
import com.goulash.script.domain.GlobalBlockerCondition
import com.goulash.script.domain.ListConfiguration
import com.goulash.script.exception.SyntaxException

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

    override val rootParser: Parser<List<ListConfiguration>> by separatedTerms(listConfigurationParser, separator)

    private fun mapToListConfiguration(section: TokenMatch, elements: List<TokenMatch>): ListConfiguration {
        if (section.text == "GlobalBlocker") {
            return GlobalBlockerCondition(elements.map(TokenMatch::text))
        } else {
            throw SyntaxException("Unknown section: [${section.text}]")
        }
    }
}
