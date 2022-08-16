package com.goulash.simulation

import com.goulash.core.domain.Container

class ManualRunner(
    private val containers: List<Container>
) : SimulationRunner {
    var ticks: Long = 0
        private set

    override fun tick() {
        containers.forEach(Container::tick)
        ticks++
    }

    override fun reset() {
        ticks = 0
    }
}
