package com.goulash.simulation

import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import com.goulash.core.domain.WorldDate
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class Simulation(
    private val maximumTicks: Int? = null,
    private val millisecondsPerTick: Long = 1000,
    private val tickBase: Int = WorldDate.SECOND
) {
    val container: Container = Container()

    init {
        LOG.info("Initializing simulation")
        LOG.info("${container.actors.size} actors initialized")
    }

    fun run() {
        if (maximumTicks != null) {
            repeat(maximumTicks) {
                while (SimulationHolder.paused) {
                }
                runSimulation()
            }
        } else {
            while (!SimulationHolder.stopped) {
                while (SimulationHolder.paused) {
                }
                runSimulation()
            }
        }

        LOG.info("Simulation finished")
    }

    private fun runSimulation() {
        SimulationHolder.ticks++
        container.tick()
        TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
    }
}
