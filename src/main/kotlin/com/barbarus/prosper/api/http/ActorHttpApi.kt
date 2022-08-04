package com.barbarus.prosper.api.http

import com.barbarus.prosper.api.http.response.ActorState
import com.barbarus.prosper.api.http.response.toResponse
import com.barbarus.prosper.core.SimulationContext
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("actors")
class ActorHttpApi {

    @GetMapping("")
    fun getActorsForContainer(@RequestParam container: String): List<ActorState> {
        check(SimulationContext.isRunning()) { "Simulation is not running" }
        return when (container) {
            "root" -> {
                val rootContainer = SimulationContext.simulation?.container
                if (rootContainer == null) {
                    LOG.error("Container is null")
                    return emptyList<ActorState>()
                }
                rootContainer.actors.map { it.toResponse() }
            }

            else -> emptyList()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
