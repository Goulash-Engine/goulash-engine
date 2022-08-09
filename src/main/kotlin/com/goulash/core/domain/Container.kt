package com.goulash.core.domain

import com.goulash.core.DecisionEngine
import com.goulash.script.loader.ScriptLoader
import org.slf4j.LoggerFactory

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val id: String = ROOT_CONTAINER,
    val actors: MutableList<Actor> = mutableListOf(),
    private val decisionEngine: DecisionEngine = DecisionEngine()
) {
    init {
        ScriptLoader.containerScripts.forEach { it.init(this) }
    }

    fun tick() {
        ScriptLoader.containerScripts.forEach { it.process(this) }
        actors.forEach {
            decisionEngine.tick(it)
            it.tick()
        }
    }

    companion object {
        const val ROOT_CONTAINER = "root"
    }
}
