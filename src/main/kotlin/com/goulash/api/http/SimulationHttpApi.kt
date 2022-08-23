package com.goulash.api.http

import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.SimulationHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("simulation")
class SimulationHttpApi {
    @GetMapping("status")
    fun simulationStatus() = SimulationHolder.simulation?.toStatus() ?: SimulationStatus("not running", 0)
}
