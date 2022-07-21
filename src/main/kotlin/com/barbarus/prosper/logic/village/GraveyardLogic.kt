package com.barbarus.prosper.logic.village

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.logic.Logic

/**
 * This logic simulates the disappearance of [Clan]s .
 */
class GraveyardLogic : Logic<Village> {
    override fun process(context: Village) {
        context.clans.removeIf { it.conditions.contains("dead") }
    }
}