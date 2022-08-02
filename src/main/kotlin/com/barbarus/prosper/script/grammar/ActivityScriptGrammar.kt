package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ActivityScriptContext
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

internal class ActivityScriptGrammar : Grammar<ActivityScriptContext>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val activityKeyword by regexToken("^activity\\s+[a-z_]+\\s*")
    private val logicKeyword by regexToken("^logic\\s+[a-z_]+\\s*")
    private val configKeyword by regexToken("priority(?!_)|duration|trigger_urges|priority_conditions|blocker_conditions|abort_conditions")
    private val openConfig by literalToken("[")
    private val configValue by regexToken("^\"[a-z\\d.]+\"")
    private val closeConfig by literalToken("]")
    private val openBraces by literalToken("{")
    private val logicBlock by regexToken("^[actor[\\[\\]a-z.<>=\\d\\s]*::[a-z]+\\([a-z]+\\)\\.[a-z]+\\([\\d.\\d\\\\]+\\);\\s*]+")
    private val closedBraces by literalToken("}")
    private val comma by literalToken(",")

    private val logicParser by logicKeyword * -openBraces * logicBlock * -closedBraces map { (logic, block) ->
        Logic(
            logic.text.removePrefix("logic").trim(),
            block.text.replace(Regex("\\s+"), "").trim()
        )
    }
    private val configParser by configKeyword * -openConfig * separatedTerms(
        configValue,
        comma
    ) * -closeConfig map { (keyword, values) ->
        keyword.text to values.map { it.text.removeSurrounding("\"") }
    }

    override val rootParser by activityKeyword * -openBraces * oneOrMore(logicParser or configParser) * -closedBraces map { (activity, body) ->
        val activityName = activity.text.removePrefix("activity").trim()
        val configs =
            body.filterIsInstanceTo<Pair<String, List<String>>, MutableList<Pair<String, List<String>>>>(mutableListOf())
        val logics =
            body.filterIsInstanceTo<Logic, MutableList<Logic>>(mutableListOf())

        ActivityScriptContext(activityName, logics.associate { it.toPair() }, configs.toMap())
    }

    internal class Logic(
        private val name: String,
        private val statements: String
    ) {
        fun toPair() = name to statementsToLogicBlock()
        private fun statementsToLogicBlock() = "logic$name{$statements}".trim()
    }
}
