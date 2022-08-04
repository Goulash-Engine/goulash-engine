package com.barbarus.prosper.api.service

import com.barbarus.prosper.core.SimulationContext
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.core.domain.WorldTime
import com.barbarus.prosper.script.loader.ScriptLoader
import com.barbarus.prosper.simulation.Simulation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SimulationService {
    fun start() {
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
        SimulationContext.simulation = null
        LOG.info("Simulation stopped")
    }

    fun togglePause() {
        if (SimulationContext.simulation == null) {
            LOG.error("Simulation not initialized")
            return
        }
        SimulationContext.pause = !SimulationContext.pause
        if (SimulationContext.pause) LOG.info("Simulation paused") else LOG.info("Simulation resumed")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
