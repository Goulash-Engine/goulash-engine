package com.barbarus.prosper.village.logic

import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.logic.Logic

class HungerCivilisationLogic : Logic<Civilisation> {
    override fun process(context: Civilisation) {
        context.clans.forEach { it.urges.increaseUrge("eat", 0.1) }
    }
}
