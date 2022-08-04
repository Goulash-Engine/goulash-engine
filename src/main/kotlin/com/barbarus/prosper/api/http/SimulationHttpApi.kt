package com.barbarus.prosper.api.http

import com.barbarus.prosper.api.service.SimulationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("simulation")
class SimulationHttpApi(
    private val simulationService: SimulationService
) {

    @PostMapping("start")
    fun startSimulation() {
        simulationService.startSimulation()
    }
}
