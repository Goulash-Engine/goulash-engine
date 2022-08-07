package com.goulash.simulation

import com.goulash.core.SimulationContext
import com.goulash.core.domain.Container
import com.goulash.core.domain.WorldDate
import com.goulash.factory.ActorFactory
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
                while (SimulationContext.paused) {
                }
                runSimulation()
            }
        } else {
            while (!SimulationContext.stopped) {
                while (SimulationContext.paused) {
                }
                runSimulation()
            }
        }

        LOG.info("Simulation finished")
    }

    private fun runSimulation() {
        SimulationContext.ticks++
        WORLD_TIME.tick(tickBase)
        container.act()
        TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
        val WORLD_TIME = WorldDate()
    }
}
