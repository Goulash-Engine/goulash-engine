package com.goulash.simulation

import com.goulash.core.domain.Container
import java.util.concurrent.TimeUnit

class StandaloneRunner {
    private lateinit var containers: List<Container>
    var paused: Boolean = false
        private set
    var running: Boolean = false
        private set
    var ticks: Long = 0
        private set

    fun run(containers: List<Container>, millisecondsPerTick: Long) {
        running = true
        while (!paused) {
            containers.forEach(Container::tick)
            ticks++
            TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
        }
    }

    fun pause() {
        if (running) {
            paused = true
        }
    }

    fun `continue`() {
        if (running) {
            paused = false
        }
    }

    fun stop() {
        running = false
        paused = false
        ticks = 0
    }
}
