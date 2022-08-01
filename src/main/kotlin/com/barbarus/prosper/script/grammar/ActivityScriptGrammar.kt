package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.or
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
    private val digit by regexToken("^[\\d.\\d]+")
    private val identifier by regexToken("^[a-z_]+")

    private val activityNameParser by -identifier * identifier use { text }
    private val configurationParser by identifier * -openBraces * separatedTerms(
        (identifier or digit),
        comma
    ) * -closeBraces map { (identifier, terms) ->
        identifier.text to terms.map { it.text }
    }
    private val activityBodyParser by zeroOrMore(configurationParser)

    override val rootParser by activityNameParser * -openBraces * activityBodyParser * -closeBraces map { (activity, body) ->
        val configs = body.filterIsInstanceTo(mutableListOf<Pair<String, List<String>>>())
        ActivityScriptContext(activity, "", configs.toMap())
    }
}
