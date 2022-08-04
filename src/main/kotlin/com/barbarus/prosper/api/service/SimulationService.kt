package com.barbarus.prosper.api.service

import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.script.loader.ScriptLoader
import com.barbarus.prosper.simulation.Simulation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SimulationService {
    private var simulation: Simulation? = null

    fun startSimulation() {
        LOG.info("Simulation start requested")
        ScriptLoader.load()
        val simulation = Simulation(
            millisecondsPerTick = 100,
            tickBase = WorldDate.MINUTE,
            render = true
        )
        simulation.run()
    }

    fun stopSimulation() {
        val simulation = Simulation(
            millisecondsPerTick = 100,
            tickBase = WorldDate.MINUTE,
            render = true
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation (http-server)")
    }
}
