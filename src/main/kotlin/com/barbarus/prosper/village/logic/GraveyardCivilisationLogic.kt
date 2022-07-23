package com.barbarus.prosper.village.logic

import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.logic.Logic

/**
 * This logic simulates the disappearance of [Clan]s .
 */
class GraveyardCivilisationLogic : Logic<Civilisation> {
    override fun process(context: Civilisation) {
        context.clans.removeIf { it.conditions.contains("dead") }
    }
}
