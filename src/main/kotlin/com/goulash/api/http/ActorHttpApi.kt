package com.goulash.api.http

import com.goulash.api.http.response.ActorState
import com.goulash.api.http.response.toResponse
import com.goulash.api.service.ActorService
import com.goulash.core.SimulationContext
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("actors")
class ActorHttpApi(
    private val actorService: ActorService
) {

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

    @PostMapping("{key}")
    fun registerActor(@PathVariable key: String) {
        actorService.registerActor(key)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
