package com.barbarus.prosper.civilisation.logic

import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.logic.Logic

/**
 * Global logic for the whole civilisation.
 * This includes routines that occur for every actor
 */
class CivilisationLogic : Logic<Civilisation> {
    override fun process(context: Civilisation) {
        context.actors.forEach { it.urges.increaseUrge("eat", 0.1) }
        context.actors.removeIf { it.conditions.contains("dead") }
    }
}
