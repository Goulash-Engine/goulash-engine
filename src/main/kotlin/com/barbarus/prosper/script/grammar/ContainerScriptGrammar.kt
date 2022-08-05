package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.logic.ContainerScriptContext
import com.barbarus.prosper.script.logic.ScriptHead
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

class ContainerScriptGrammar : Grammar<ContainerScriptContext>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val logicKeyword by regexToken("^logic\\s*[a-z_]+\\s*")
    private val logicBlock by regexToken("^[actor[\\[\\]a-z.<>=\\d\\s]*::[a-z]+\\([a-z]+\\)\\.[a-z]+\\([\\d.\\d\\\\]+\\);\\s*]+")
    private val startLogicBlock by literalToken("{")
    private val endLogicBlock by literalToken("}")

    /**
     * logic <name>
     */
    private val scriptHeadParser by logicKeyword use { ScriptHead(text.removePrefix("logic").trim()) }

    private val logicBlockParser by logicBlock use { text }

    private val statementsParser by -startLogicBlock * logicBlockParser * -endLogicBlock
    override val rootParser by (scriptHeadParser * statementsParser) map { (scriptHead, logicBlock) ->
        val logicStatementGrammar = LogicStatementGrammar()
        val statements = logicStatementGrammar.parseToEnd(logicBlock)
        ContainerScriptContext(scriptHead, statements)
    }
}
