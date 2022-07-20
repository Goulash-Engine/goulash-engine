package com.barbarus.prosper.core.domain

import com.barbarus.prosper.logic.Logic
import com.barbarus.prosper.logic.village.GraveyardLogic

/**
 * The core entity of the prosper engine. Within a village multiple [Clan]s try to survive and prosper through work,
 * trade and socialisation.
 */
class Village(
    val clans: MutableList<Clan> = mutableListOf()
) {
    private val villageLogic: List<Logic<Village>> = listOf(
        GraveyardLogic()
    )

    fun act() {
        villageLogic.forEach { it.process(this) }
        clans.forEach { it.act() }
        villageLogic.forEach { it.process(this) }
    }
}
