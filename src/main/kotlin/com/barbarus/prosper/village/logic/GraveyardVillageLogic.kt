package com.barbarus.prosper.village.logic

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.logic.Logic

/**
 * This logic simulates the disappearance of [Clan]s .
 */
class GraveyardVillageLogic : Logic<Village> {
    override fun process(context: Village) {
        context.clans.removeIf { it.conditions.contains("dead") }
    }
}
