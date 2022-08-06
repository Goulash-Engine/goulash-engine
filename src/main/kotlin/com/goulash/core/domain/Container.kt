package com.goulash.core.domain

import com.goulash.core.logic.Logic
import com.goulash.script.loader.ScriptLoader
import org.slf4j.LoggerFactory

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val actors: MutableList<Actor> = mutableListOf(),
    var loadedScripts: List<Logic<Container>> = listOf()
) {
    init {
        LOG.info("Initialize container scripts")
        loadedScripts = ScriptLoader.getContainerScripts()
        LOG.info("Initialized ${loadedScripts.size} container scripts")
    }

    fun act() {
        loadedScripts.forEach { it.process(this) }
        actors.forEach { it.act() }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
    }
}