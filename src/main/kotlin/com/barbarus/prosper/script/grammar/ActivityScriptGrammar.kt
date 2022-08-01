package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ActivityScriptContext
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

    private val activityKeyword by regexToken("^activity\\s+[a-z_]+\\s*\\{")

    // private val logicKeyword by regexToken("^logic\\s+[a-z_]+\\s*\\{")
    private val configKeyword by regexToken("priority(?!_)|duration|trigger_urges|priority_conditions|blocker_conditions|abort_conditions")
    private val configValue by regexToken("^[a-z\\d.\\d]+")

    // private val logicBlock by regexToken("^[a-z0-9\\s.:;<>=\\[\\]()]+\\s*")
    private val openConfig by literalToken("[")
    private val closeConfig by literalToken("]")

    // private val openBraces by literalToken("{")
    private val closeBraces by literalToken("}")
    private val comma by literalToken(",")

    private val activityNameParser by activityKeyword use { text.removePrefix("activity").removeSuffix("{").trim() }

    // private val logicParser by logicKeyword * logicBlock * -closeBraces map { (logic, block) ->
    //     logic.text.removePrefix(
    //         "logic"
    //     ).trim() to block.text.trim()
    // }
    private val configurationParser by configKeyword * -openConfig * separatedTerms(
        configValue,
        comma
    ) * -closeConfig map { (keyword, values) ->
        keyword.text to values.map { it.text }
    }
    private val activityBodyParser by zeroOrMore(configurationParser)

    override val rootParser by activityNameParser * activityBodyParser * -closeBraces map { (activity, body) ->
        val configs = body.filterIsInstanceTo(mutableListOf<Pair<String, List<String>>>())
        val logics = body.filterIsInstanceTo(mutableListOf<Pair<String, String>>())
        ActivityScriptContext(activity, logics.toMap(), configs.toMap())
    }
}
