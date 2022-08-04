package com.barbarus.prosper.api.http

import com.barbarus.prosper.core.SimulationContext
import com.barbarus.prosper.core.domain.Actor
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("actors")
class ActorHttpEndpoint {

    @GetMapping("")
    fun getActorsForContainer(@RequestParam container: String): List<Actor> {
        check(SimulationContext.isRunning()) { "Simulation is not running" }
        return when (container) {
            "root" -> {
                val rootContainer = SimulationContext.simulation?.container
                if (rootContainer == null) {
                    LOG.error("Container is null")
                    return emptyList<Actor>()
                }
                rootContainer.actors
            }

            else -> emptyList()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
