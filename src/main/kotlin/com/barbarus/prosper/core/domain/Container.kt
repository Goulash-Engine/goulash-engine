package com.barbarus.prosper.core.domain

import ScriptLoader
import com.barbarus.prosper.core.logic.Logic
import org.slf4j.LoggerFactory

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val actors: MutableList<Actor> = mutableListOf(),
    var loadedLogicScripts: List<Logic<Container>> = listOf()
) {
    init {
        LOG.info("Initialize logic scripts")
        loadedLogicScripts = ScriptLoader.getLogicScripts()
        LOG.info("Initialized ${loadedLogicScripts.size} logic scripts")
    }

    fun act() {
        loadedLogicScripts.forEach { it.process(this) }
        actors.forEach { it.act() }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
    }
}
