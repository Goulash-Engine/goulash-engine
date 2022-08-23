package com.goulash.simulation

import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container

/**
 * The simulation runner for manual tick control.
 */
class ManualSimulationRunner(
    private var containers: List<Container>,
    private val containerRunner: ContainerRunner = ContainerRunner()
) : SimulationRunner {

    override fun getContainers(): List<Container> {
        return this.containers
    }

    init {
        SimulationHolder.simulation = this
        containers.forEach(containerRunner::register)
    }

    /**
     * The ticks that have been simulated so far.
     */
    var ticks: Long = 0
        private set

    /**
     * Will execute a tick for this simulation manually.
     */
    fun tick() {
        containerRunner.tick()
        ticks++
    }

    /**
     * Resets the runner with new containers.
     * @param containers The containers to reset the runner with.
     */
    fun reset(containers: List<Container>) {
        this.containers = containers
        ticks = 0
    }

    fun stop() {
        this.containers = emptyList()
        ticks = 0
        SimulationHolder.simulation = null
    }

    override fun toStatus(): SimulationStatus {
        return SimulationStatus("manual", ticks)
    }
}
