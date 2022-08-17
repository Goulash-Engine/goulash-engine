package com.goulash.simulation

import com.goulash.core.domain.Container

class ManualRunner(
    private var containers: List<Container>
) {
    var ticks: Long = 0
        private set

    fun tick() {
        containers.forEach(Container::tick)
        ticks++
    }

    fun reset(containers: List<Container>) {
        this.containers = containers
        ticks = 0
    }
}
