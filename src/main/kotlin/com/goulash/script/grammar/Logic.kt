package com.goulash.script.grammar

import com.github.h0tk3y.betterParse.grammar.parseToEnd

internal class Logic(
    private val name: String,
    private val statements: String
) {
    private val grammar = LogicStatementGrammar()
    fun toPair() = name to grammar.parseToEnd(statements.split(";").joinToString(";\n"))
}
