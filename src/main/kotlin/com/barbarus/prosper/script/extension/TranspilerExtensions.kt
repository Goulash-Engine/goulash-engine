package com.barbarus.prosper.script.extension

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.script.grammar.FilterGrammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd

object TranspilerExtensions {
    fun <T : Actor> List<T>.tryScriptFilter(filterStatement: String): List<T> {
        if (filterStatement.isEmpty()) return this
        val contextFilter = FilterGrammar().parseToEnd(filterStatement)
        if (contextFilter.type == "state") {
            if (contextFilter.attribute == "health") {
                when (contextFilter.operator) {
                    "=" -> return this.filter { it.state.health == contextFilter.argument.toDouble() }
                    ">" -> return this.filter { it.state.health > contextFilter.argument.toDouble() }
                    "<" -> return this.filter { it.state.health < contextFilter.argument.toDouble() }
                }
            }
        }
        return this
    }

    fun <T : Actor> T.tryScriptFilter(filterStatement: String): T? {
        if (filterStatement.isEmpty()) return this
        val contextFilter = FilterGrammar().parseToEnd(filterStatement)
        if (contextFilter.type == "state") {
            if (contextFilter.attribute == "health") {
                when (contextFilter.operator) {
                    "=" -> return if (this.state.health == contextFilter.argument.toDouble()) this else null
                    ">" -> return if (this.state.health > contextFilter.argument.toDouble()) this else null
                    "<" -> return if (this.state.health < contextFilter.argument.toDouble()) this else null
                }
            }
        }
        return this
    }
}
