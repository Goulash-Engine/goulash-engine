package com.barbarus.prosper.core.domain

import ScriptLoader
import com.barbarus.prosper.core.logic.Logic
import org.slf4j.LoggerFactory

/**
 * The core entity of the prosper engine. Within a village multiple [Clan]s try to survive and prosper through work,
 * trade and socialisation.
 */
class Civilisation(
    val actors: MutableList<Actor> = mutableListOf(),
    var loadedLogicScripts: List<Logic<Civilisation>> = listOf()
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
