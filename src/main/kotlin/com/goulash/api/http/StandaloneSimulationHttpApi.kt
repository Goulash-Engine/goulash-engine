package com.goulash.api.http

import com.goulash.core.domain.Actor
import com.goulash.core.domain.Container
import com.goulash.simulation.StandaloneSimulationRunner
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("simulation/standalone")
class StandaloneSimulationHttpApi {
    private val standaloneSimulationRunner = StandaloneSimulationRunner()

    @PostMapping("start")
    fun startSimulation() {
        standaloneSimulationRunner.run(
            listOf(Container(actors = mutableListOf<Actor>())),
            1000
        )
    }

    @PostMapping("pause")
    fun pauseSimulation() {
        standaloneSimulationRunner.pause()
    }

    @PostMapping("stop")
    fun stopSimulation() {
        standaloneSimulationRunner.stop()
    }
}
