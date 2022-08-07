package com.goulash.api.service

import com.goulash.core.SimulationContext
import com.goulash.core.domain.Container
import com.goulash.factory.ActorFactory
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ActorService {
    /**
     * Registers a new [Actor] to a [Container].
     * @param key Reference key to associate the actor for later use.
     * @param containerId ID of the [Container] to register the actor to. Defaults to [Container.ROOT_CONTAINER].
     */
    fun registerActor(key: String, containerId: String = Container.ROOT_CONTAINER) {
        require(key.isNotBlank()) { "Key must not be blank." }
        if (!SimulationContext.isRunning()) {
            LOG.error("Simulation is not running")
            return
        }
        val container = SimulationContext.simulation?.container
        if (container == null) {
            LOG.error("No simulation context found. Cannot register actor.")
            return
        }
        val actorExists = container.actors.any { it.key == key }
        if (actorExists) {
            LOG.error("Actor with key '$key' already exists.")
            return
        }
        val newActor = ActorFactory.newActor(key)
        container.actors.add(newActor)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ActorService")
    }
}