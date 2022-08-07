package com.goulash.api.service

import com.goulash.core.SimulationContext
import com.goulash.core.domain.WorldDate
import com.goulash.script.loader.ScriptLoader
import com.goulash.simulation.Simulation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SimulationService {
    fun start() {
        SimulationContext.stopped = false
        if (SimulationContext.simulation != null) {
            LOG.warn("Simulation is already running")
            return
        }
        LOG.info("Simulation start requested")
        ScriptLoader.load()
        SimulationContext.simulation = Simulation(
            millisecondsPerTick = 100,
            tickBase = WorldDate.MINUTE
        )
        SimulationContext.simulation?.run() ?: LOG.error("Simulation not initialized")
    }

    fun stop() {
        SimulationContext.stopped = true
        SimulationContext.simulation = null
        SimulationContext.ticks = 0
        LOG.info("Simulation stopped")
    }

    fun togglePause() {
        if (SimulationContext.simulation == null) {
            LOG.error("Simulation not initialized")
            return
        }
        SimulationContext.paused = !SimulationContext.paused
        if (SimulationContext.paused) LOG.info("Simulation paused") else LOG.info("Simulation resumed")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
