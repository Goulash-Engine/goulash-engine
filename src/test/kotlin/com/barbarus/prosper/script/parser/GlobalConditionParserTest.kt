package com.barbarus.prosper.script.parser

import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.skip
import com.github.h0tk3y.betterParse.lexer.DefaultTokenizer
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import org.junit.jupiter.api.Test

internal class GlobalConditionParserTest {
    @Test
    fun `should match foo`() {
        val leftBracket = literalToken("[")
        val rightBracket = literalToken("]")
        val identifier = regexToken("^[A-Za-z]+")
        // val listSyntax = regexToken("^- [A-Za-z]+$")
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
        ).tokenize(
            """
            [GlobalBlocker]
            - foo
            - bar
            d
            """.trimIndent()
        )

        val section = skip(leftBracket) and identifier and skip(rightBracket)
        val list = skip(listSyntax) and listElement

        val combo = section and oneOrMore(list)

        // val lbracket: ParseResult<TokenMatch> = leftBracket.tryParse(tokenMatches, 0) // contains tb
        // val rbracket: ParseResult<TokenMatch> = rightBracket.tryParse(tokenMatches, lbracket.toParsedOrThrow().nextPosition) // contains tb
        // val identifiermatch: ParseResult<TokenMatch> = identifier.tryParse(tokenMatches, rbracket.toParsedOrThrow().nextPosition) // contains tb

        // val gbcGrammar = object : Grammar<GlobalBlockingCondition>() {
        //     override val rootParser: Parser<GlobalBlockingCondition>
        //         get() = TODO("Not yet implemented")
        // }
    }
}
