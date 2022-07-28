package com.barbarus.prosper.script.grammar

import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class PlaygroundGrammar : Grammar<String>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val identifier by regexToken("\\[(.*)\\]")
    private val contextMutationOperator by literalToken("::")

    override val rootParser by (identifier map { it.text.removeSurrounding("[", "]") }) * -contextMutationOperator
}
