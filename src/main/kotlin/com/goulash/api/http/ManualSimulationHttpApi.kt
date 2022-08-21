package com.goulash.api.http

import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import com.goulash.simulation.ManualSimulationRunner
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("simulation/manual")
class ManualSimulationHttpApi {
    private var manualSimulationRunner: ManualSimulationRunner? = null

    @GetMapping("status")
    fun simulationStatus() = SimulationHolder.simulation?.toStatus() ?: SimulationStatus("not running", 0)

    @PostMapping("start")
    fun startSimulation() {
        manualSimulationRunner = ManualSimulationRunner(listOf(Container()))
    }

    @PostMapping("tick")
    fun pauseSimulation() {
        manualSimulationRunner?.tick()
    }

    @PostMapping("stop")
    fun stopSimulation() {
        manualSimulationRunner?.stop()
    }

    @PostMapping("reset")
    fun resetSimulation() {
        manualSimulationRunner?.reset(listOf(Container()))
    }
}
