package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor
import org.slf4j.LoggerFactory

/**
 * This [Behavior] controls the daily work of a clan.
 */
class WorkBehavior : Behavior {
    override fun trigger(): String {
        return "work"
    }

    override fun blocker(): String {
        return "sleep"
    }

    override fun act(actor: Actor) {
        // LOG.info("${actor.id} is working")
        actor.state.stamina -= 1
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ConsumptionBehavior")
    }
}
