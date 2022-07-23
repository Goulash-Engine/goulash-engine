package com.barbarus.prosper.village.logic

import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.core.logic.Logic
import com.barbarus.prosper.simulation.Simulation

class NightTimeVillageLogic : Logic<Village> {
    override fun process(context: Village) {
        if (Simulation.WORLD_TIME.isNight()) {
            context.clans.forEach { it.urges.increaseUrge("sleep", 1.0) }
        }
    }
}
