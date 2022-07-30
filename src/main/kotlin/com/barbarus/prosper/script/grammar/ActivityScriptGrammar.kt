package com.barbarus.prosper.script.grammar

import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

class ActivityScriptGrammar : Grammar<String>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val identifier by regexToken("^[a-z]+")

    override val rootParser: Parser<String> by identifier use { text }
}
