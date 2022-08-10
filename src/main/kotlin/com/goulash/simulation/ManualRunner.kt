package com.goulash.simulation

import com.goulash.core.domain.Container

class ManualRunner(
    private var containers: List<Container>
) : SimulationRunner {
    override fun tick() {
        containers.forEach(Container::tick)
    }
}
