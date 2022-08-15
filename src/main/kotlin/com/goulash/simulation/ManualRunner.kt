package com.goulash.simulation

import com.goulash.core.domain.Container
import org.slf4j.LoggerFactory

class ManualRunner(
    private val containers: List<Container>
) : SimulationRunner {
    var ticks: Long = 0
        private set

    override fun tick() {
        ticks++
        containers.forEach(Container::tick)
    }

    override fun reset() {
        ticks = 0
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("ManualRunner")
    }
}
