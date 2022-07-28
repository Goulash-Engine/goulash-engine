package com.barbarus.prosper.core.domain

import ScriptLoader

/**
 * The core entity of the prosper engine. Within a village multiple [Clan]s try to survive and prosper through work,
 * trade and socialisation.
 */
class Civilisation(
    val actors: MutableList<Actor> = mutableListOf()
) {

    fun act() {
        ScriptLoader.getLogicScripts().forEach { it.process(this) }
        actors.forEach { it.act() }
    }
}
