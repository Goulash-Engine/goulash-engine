package com.goulash.api.http

import com.goulash.core.SimulationContext
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
        check(SimulationContext.isRunning()) { "Simulation is not running" }
        val container = SimulationContext.simulation?.container
        if (container == null) {
            LOG.error("Container is null")
            return null
        }
        return container
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
