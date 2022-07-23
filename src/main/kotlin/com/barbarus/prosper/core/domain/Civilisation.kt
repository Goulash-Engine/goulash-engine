package com.barbarus.prosper.core.domain

import com.barbarus.prosper.core.logic.Logic
import com.barbarus.prosper.village.logic.GraveyardCivilisationLogic
import com.barbarus.prosper.village.logic.HungerCivilisationLogic

/**
 * The core entity of the prosper engine. Within a village multiple [Clan]s try to survive and prosper through work,
 * trade and socialisation.
 */
class Civilisation(
    val clans: MutableList<Clan> = mutableListOf()
) {
    private val civilisationLogic: List<Logic<Civilisation>> = listOf(
        GraveyardCivilisationLogic(),
        HungerCivilisationLogic()
    )

    fun act() {
        civilisationLogic.forEach { it.process(this) }
        clans.forEach { it.act() }
    }
}
