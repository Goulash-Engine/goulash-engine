package com.barbarus.prosper.script.grammar

import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class PlaygroundGrammar : Grammar<String>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val activityKeyword by regexToken("^activity\\s+[a-z_]+")
    private val logicKeyword by regexToken("^logic\\s+[a-z_]+")
    private val logicBlock by regexToken("^[a-z0-9\\s.:;<>=\\[\\]()]+\\s*")

    // private val identifier by regexToken("^[a-z_]+\\s+$(?!:)")
    // private val identifier by regexToken("^[a-z_]+")
    private val openBraces by literalToken("{")
    private val closeBraces by literalToken("}")

    override val rootParser by -activityKeyword * -openBraces * -logicKeyword * -openBraces * logicBlock * -closeBraces * -closeBraces use { text }
}
