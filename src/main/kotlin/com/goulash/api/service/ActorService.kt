package com.goulash.api.service

import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import com.goulash.factory.BaseActorFactory
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
        if (SimulationHolder.simulation == null) {
            LOG.error("No running Simulation found")
            return
        }
        if (SimulationHolder.simulation?.toStatus()?.status == "not running") {
            LOG.error("Simulation is not running")
            return
        }
        val container = SimulationHolder.simulation?.getContainers()?.find { it.id == containerId }
        if (container == null) {
            LOG.error("No container found for id $containerId")
            return
        }
        val actorExists = container.getActors().any { it.key == key }
        if (actorExists) {
            LOG.error("Actor with key '$key' already exists.")
            return
        }
        val newActor = BaseActorFactory.newActor(key)
        container.addActor(newActor)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ActorService")
    }
}
