package com.goulash.api.http

import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("container")
class ContainerHttpApi {

    @GetMapping("root")
    fun getRootContainer(): Container? {
        check(SimulationHolder.simulation?.toStatus()?.status != "running") { "Simulation is not running" }
        return SimulationHolder.simulation?.getContainers()?.find { it.id == Container.ROOT_CONTAINER } ?: return null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
