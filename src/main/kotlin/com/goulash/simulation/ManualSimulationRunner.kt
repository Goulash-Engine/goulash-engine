package com.goulash.simulation

import com.goulash.core.domain.Container

/**
 * The simulation runner for manual tick control.
 */
class ManualSimulationRunner(
    private var containers: List<Container>
) {
    /**
     * The ticks that have been simulated so far.
     */
    var ticks: Long = 0
        private set

    /**
     * Will execute a tick for this simulation manually.
     */
    fun tick() {
        containers.forEach(Container::tick)
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
}
