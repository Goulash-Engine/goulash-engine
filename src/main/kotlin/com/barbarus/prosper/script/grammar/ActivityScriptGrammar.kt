package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.barbarus.prosper.script.logic.ScriptHead
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

internal class ActivityScriptGrammar : Grammar<ActivityScriptContext>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val identifier by regexToken("^[a-z]+")

    private val activityNameParser by -identifier * identifier use { ScriptHead(text) }

    override val rootParser by activityNameParser map { scriptHead ->
       ActivityScriptContext(scriptHead)
    }

}
