package com.barbarus.prosper.simulation

import com.barbarus.prosper.core.SimulationContext
import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.factory.ActorFactory
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class Simulation(
    private val maximumTicks: Int? = null,
    private val millisecondsPerTick: Long = 1000,
    private val tickBase: Int = WorldDate.SECOND,
    val container: Container = Container(
        mutableListOf(
            ActorFactory.poorActor(),
            ActorFactory.poorActor(),
            ActorFactory.poorActor(),
            ActorFactory.poorActor(),
            ActorFactory.poorActor()
        )
    )
) {

    init {
        LOG.info("Initializing simulation")
        LOG.info("${container.actors.size} actors initialized")
    }

    fun run() {
        if (maximumTicks != null) {
            repeat(maximumTicks) { currentTick ->
                while (SimulationContext.pause) {
                }
                runSimulation(currentTick)
            }
        } else {
            while (true) {
                while (SimulationContext.pause) {
                }
                runSimulation()
            }
        }

        LOG.info("Simulation finished")
    }

    private fun runSimulation(currentTick: Int? = null) {
        WORLD_TIME.tick(tickBase)
        container.act()
        TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Simulation")
        val WORLD_TIME = WorldDate()
    }
}
