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

    private val triggerKeyword by literalToken("trigger")
    private val blockerKeyword by literalToken("blocker")
    private val openBraces by literalToken("{")
    private val closeBraces by literalToken("}")
    private val comma by literalToken(",")
    private val identifier by regexToken("^[a-z]+")

    private val activityNameParser by -identifier * identifier use { ScriptHead(text) }
    private val triggerParser by -triggerKeyword * -openBraces * separatedTerms(
        identifier,
        comma
    ) * -closeBraces map { terms -> terms.map { it.text } }
    private val blockerParser by -blockerKeyword * -openBraces * separatedTerms(
        identifier,
        comma
    ) * -closeBraces map { terms -> terms.map { it.text } }
    private val activityBodyParser by -openBraces * zeroOrMore(triggerParser) * zeroOrMore(blockerParser) * -closeBraces

    override val rootParser by activityNameParser * activityBodyParser map { (scriptHead, optionals) ->
        val (triggers, blockers) = optionals
        val foundTriggers = triggers.firstOrNull() ?: emptyList()
        val foundBlockers = blockers.firstOrNull() ?: emptyList()

        ActivityScriptContext(scriptHead, foundTriggers, foundBlockers)
    }
}
