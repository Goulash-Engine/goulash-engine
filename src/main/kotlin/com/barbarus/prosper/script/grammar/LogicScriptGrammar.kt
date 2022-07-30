package com.barbarus.prosper.script.grammar

import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.logic.ContextMutation
import com.barbarus.prosper.script.logic.LogicScriptContext
import com.barbarus.prosper.script.logic.Operation
import com.barbarus.prosper.script.logic.ScriptHead
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.optional
import com.github.h0tk3y.betterParse.combinators.or
import com.github.h0tk3y.betterParse.combinators.separatedTerms
import com.github.h0tk3y.betterParse.combinators.times
import com.github.h0tk3y.betterParse.combinators.unaryMinus
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

internal class LogicScriptGrammar : Grammar<LogicScriptContext>() {
    private val space by regexToken("\\s+", ignore = true)
    private val newLine by literalToken("\n", ignore = true)

    private val scriptKeyword by literalToken("logic")
    private val filter by regexToken("\\[(.*)\\]")
    private val contextMutationOperator by literalToken("::")
    private val identifier by regexToken("^[a-z]+")
    private val scriptName by regexToken("^[a-z_]+")
    private val startLogicBlock by literalToken("{")
    private val operationOperator by literalToken(".")
    private val digit by regexToken("^[\\d.\\d]+")
    private val rightPar by literalToken(")")
    private val leftPar by literalToken("(")
    private val endLogicBlock by literalToken("}")
    private val endOfStatement by literalToken(";", ignore = true)

    /**
     * logic <name>
     */
    private val scriptHeadParser by -scriptKeyword * (scriptName or identifier) use { ScriptHead(text) }

    /**
     * .plus(1)
     */
    private val operationParser by -operationOperator * identifier * -leftPar * (digit or identifier) * -rightPar map { (name, argument) ->
        Operation(name.text, argument.text)
    }

    /**
     * ::urge(eat)[.plus(1)]
     */
    private val mutationParser by -contextMutationOperator * identifier * -leftPar * identifier * -rightPar * operationParser map { (type, target, operation) ->
        ContextMutation(type.text, target.text, operation)
    }

    private val filterParser by filter use { text.removeSurrounding("[", "]") }

    /**
     * { actors[::urge(eat)[.plus(1)]]]; }
     */
    private val contextCommandParser by identifier * optional(filterParser) * mutationParser
    private val statementParser by contextCommandParser map { (context, filter, mutation) ->
        ScriptStatement(
            context.text,
            filter ?: "",
            mutation.type,
            mutation.target,
            mutation.operation.name,
            mutation.operation.argument
        )
    }

    private val statementsParser by -startLogicBlock * separatedTerms(statementParser, endOfStatement) * -endLogicBlock
    override val rootParser by (scriptHeadParser * statementsParser) map { (scriptHead, statements) ->
        LogicScriptContext(scriptHead, statements)
    }
}
