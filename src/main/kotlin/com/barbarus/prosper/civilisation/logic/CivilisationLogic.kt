package com.barbarus.prosper.civilisation.logic

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.logic.Logic

/**
 * Global logic for the whole civilisation.
 * This includes routines that occur for every actor
 */
class CivilisationLogic : Logic<Civilisation> {
    override fun process(context: Civilisation) {
        context.actors.forEach(::actorRoutines)
        context.actors.removeIf { it.conditions.contains("dead") }
    }

    private fun actorRoutines(actor: Actor) {
        actor.state.nourishment -= 0.05

        when {
            actor.state.nourishment < 70.0 -> actor.urges.increaseUrge("eat", 0.1)
            actor.state.nourishment < 30.0 -> actor.urges.increaseUrge("eat", 0.5)
            actor.state.nourishment < 10.0 -> actor.urges.increaseUrge("eat", 1.0)
        }
    }
}
