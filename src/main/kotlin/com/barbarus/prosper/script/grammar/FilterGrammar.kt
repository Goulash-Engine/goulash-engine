package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.domain.ContextFilter
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class FilterGrammar : Grammar<ContextFilter>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val identifier by regexToken("[a-zA-Z0-9_]+")
    private val dot by literalToken(".")
    private val operator by regexToken("[=<>!]+")

    override val rootParser by identifier * -dot * identifier * operator * identifier map { (type, attribute, operator, argument) ->
        ContextFilter(type.text, attribute.text, operator.text, argument.text)
    }
}
