package com.goulash.api.service

import com.goulash.core.SimulationContext
import com.goulash.core.domain.Container
import com.goulash.factory.ActorFactory
import org.slf4j.LoggerFactory

class ActorService {
    /**
     * Registers a new [Actor] to a [Container].
     * @param key Reference key to associate the actor for later use.
     * @param containerId ID of the [Container] to register the actor to. Defaults to [Container.ROOT_CONTAINER].
     */
    fun registerActor(key: String, containerId: String = Container.ROOT_CONTAINER) {
        require(key.isNotBlank()) { "Key must not be blank." }
        val container = SimulationContext.simulation?.container
        if (container == null) {
            LOG.error("No simulation context found. Cannot register actor.")
            return
        }
        val newActor = ActorFactory.newActor(key)
        container.actors.add(newActor)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ActorService")
    }
}
