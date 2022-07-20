package com.barbarus.prosper.core.domain

/**
 * The core entity of the prosper engine. Within a village multiple [Clan]s try to survive and prosper through work,
 * trade and socialisation.
 */
class Village {
    val clans: MutableList<Clan> = mutableListOf()

    fun act() {
        clans.forEach { it.act() }
    }
}
