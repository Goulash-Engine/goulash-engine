package com.goulash.script.grammar

import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.combinators.zeroOrMore
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.goulash.script.logic.ContainerScriptContext
import com.goulash.script.logic.ScriptHead

class ContainerScriptGrammar : Grammar<ContainerScriptContext>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val containerKeyword by regexToken("^container\\s+[a-z_]+\\s*")
    private val logicKeyword by regexToken("^logic\\s+[a-z_]+\\s*")
    private val openBraces by literalToken("{")
    private val logicBlock by regexToken("^[actors[\\[\\]a-z.<>=\\d\\s]*::[a-z]+\\([a-z]+\\)\\.[a-z]+\\([\\d.\\d\\\\]+\\);\\s*]+")
    private val closedBraces by literalToken("}")

    /**
     * logic <name>
     */
    private val scriptHeadParser by containerKeyword use { ScriptHead(text.removePrefix("container").trim()) }

    private val logicParser by logicKeyword * -openBraces * logicBlock * -closedBraces map { (logic, block) ->
        Logic(
            logic.text.removePrefix("logic").trim(),
            block.text.replace(Regex("\\s+"), "").trim()
        )
    }

    private val statementsParser by -openBraces * zeroOrMore(logicParser) * -closedBraces
    override val rootParser by (scriptHeadParser * statementsParser) map { (scriptHead, logics) ->
        ContainerScriptContext(scriptHead, logics.associate { it.toPair() })
    }
}
