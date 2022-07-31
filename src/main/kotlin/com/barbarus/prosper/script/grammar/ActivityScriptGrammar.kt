package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.barbarus.prosper.script.logic.ScriptHead
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.combinators.zeroOrMore
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

internal class ActivityScriptGrammar : Grammar<ActivityScriptContext>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val openBraces by literalToken("{")
    private val closeBraces by literalToken("}")
    private val comma by literalToken(",")
    private val identifier by regexToken("^[a-z]+")

    private val activityNameParser by -identifier * identifier use { ScriptHead(text) }
    private val listConfigurationParser by identifier * -openBraces * separatedTerms(
        identifier,
        comma
    ) * -closeBraces map { (identifier, terms) ->
        identifier.text to terms.map { it.text }
    }
    private val activityBodyParser by -openBraces * zeroOrMore(listConfigurationParser) * -closeBraces

    override val rootParser by activityNameParser * activityBodyParser map { (scriptHead, options) ->
        ActivityScriptContext(scriptHead, options.toMap())
    }
}
