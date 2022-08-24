package com.goulash.api.http

import com.goulash.api.http.response.ActorState
import com.goulash.api.http.response.toResponse
import com.goulash.api.service.ActorService
import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
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
        if (SimulationHolder.simulation?.toStatus()?.status == "not running") {
            LOG.trace("Simulation is not running")
            return emptyList()
        }

        return when (container) {
            "root" -> {
                val rootContainer = SimulationHolder.simulation?.getContainers()?.find { it.id == Container.ROOT_CONTAINER }
                if (rootContainer == null) {
                    LOG.error("Container is null")
                    return emptyList<ActorState>()
                }
                rootContainer.getActors().map { it.toResponse() }
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
