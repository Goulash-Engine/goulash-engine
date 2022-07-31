package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.optional
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
    private val durationKeyword by literalToken("duration")
    private val comma by literalToken(",")
    private val digit by regexToken("^[\\d.\\d]+")
    private val identifier by regexToken("^[a-z]+")

    private val activityNameParser by -identifier * identifier use { text }
    private val durationParser by -durationKeyword * -openBraces * digit * -closeBraces use { text }
    private val listConfigurationParser by identifier * -openBraces * separatedTerms(
        identifier,
        comma
    ) * -closeBraces map { (identifier, terms) ->
        identifier.text to terms.map { it.text }
    }
    private val activityBodyParser by optional(durationParser) * zeroOrMore(listConfigurationParser)

    override val rootParser by activityNameParser * -openBraces * activityBodyParser * -closeBraces map { (activity, body) ->
        val (duration, options) = body
        ActivityScriptContext(activity, duration ?: "", options.toMap())
    }
}
