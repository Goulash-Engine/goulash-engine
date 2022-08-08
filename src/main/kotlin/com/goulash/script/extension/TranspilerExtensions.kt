package com.goulash.script.extension

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.goulash.core.domain.Actor
import com.goulash.script.domain.ContextFilter
import com.goulash.script.grammar.FilterGrammar

object TranspilerExtensions {
    fun <T : Actor> List<T>.tryScriptFilter(filterStatement: String): List<T> {
        if (filterStatement.isEmpty()) return this
        val contextFilter = FilterGrammar().parseToEnd(filterStatement)
        return this.mapNotNull { filter(contextFilter, it) }
    }

    fun <T : Actor> T.tryScriptFilter(filterStatement: String): T? {
        if (filterStatement.isEmpty()) return this
        val contextFilter = FilterGrammar().parseToEnd(filterStatement)
        return filter(contextFilter, this)
    }

    private fun <T : Actor> filter(filter: ContextFilter, actor: T): T? {
        if (filter.type == "state") {
            initStateIfMissing(actor, filter.attribute)
            when (filter.operator) {
                "=" -> return if (actor.state[filter.attribute] == filter.argument.toDouble()) actor else null
                ">" -> return if (actor.state[filter.attribute]!! > filter.argument.toDouble()) actor else null
                ">=" -> return if (actor.state[filter.attribute]!! >= filter.argument.toDouble()) actor else null
                "<" -> return if (actor.state[filter.attribute]!! < filter.argument.toDouble()) actor else null
                "<=" -> return if (actor.state[filter.attribute]!! <= filter.argument.toDouble()) actor else null
            }
        }
        return actor
    }

    private fun initStateIfMissing(context: Actor, mutationTarget: String) {
        if (!context.state.containsKey(mutationTarget)) {
            context.state[mutationTarget] = 0.0
        }
    }
}
