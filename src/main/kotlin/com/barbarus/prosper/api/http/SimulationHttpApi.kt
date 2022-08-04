package com.barbarus.prosper.api.http

import com.barbarus.prosper.api.service.SimulationService
import com.barbarus.prosper.core.SimulationContext
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.simulation.Simulation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("simulation")
class SimulationHttpApi(
    private val simulationService: SimulationService
) {

    @GetMapping("status")
    fun simulationStatus(): String {
        return when {
            SimulationContext.isPaused() -> "paused"
            SimulationContext.isRunning() -> "running"
            !SimulationContext.isRunning() -> "not running"
            else -> "unknown"
        }
    }

    @PostMapping("start")
    fun startSimulation() {
        simulationService.start()
    }

    @PostMapping("pause")
    fun pauseSimulation() {
        simulationService.togglePause()
    }

    @PostMapping("stop")
    fun stopSimulation() {
        simulationService.stop()
    }

    @GetMapping("time")
    fun getWorldTime(): WorldDate {
        return Simulation.WORLD_TIME
    }
}
