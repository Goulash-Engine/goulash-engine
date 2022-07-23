package com.barbarus.prosper.village.logic

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.logic.Logic

class HungerVillageLogic : Logic<Village> {
    override fun process(context: Village) {
        context.clans.forEach { it.urges.increaseUrge("eat", 0.1) }
    }
}
