package com.goulash.simulation

import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.domain.Container

interface SimulationRunner {
    fun getContainers(): List<Container>
    fun toStatus(): SimulationStatus = SimulationStatus("not running", 0)
}
