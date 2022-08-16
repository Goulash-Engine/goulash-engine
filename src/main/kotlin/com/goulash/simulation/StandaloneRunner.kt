package com.goulash.simulation

import com.goulash.core.domain.Container
import java.util.concurrent.TimeUnit

class StandaloneRunner(
    private val containers: List<Container>
) {
    var runnerContainer: List<Container> = listOf()
        get() = containers
        private set
    val containerCache: List<Container>
    var paused: Boolean = false
        private set
    var running: Boolean = false
        private set
    var ticks: Long = 0
        private set

    init {
        containerCache = mutableListOf<Container>().also { it.addAll(containers) }
    }

    fun run(millisecondsPerTick: Long) {
        running = true
        while (!paused) {
            runnerContainer.forEach(Container::tick)
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
        runnerContainer = containerCache
        running = false
        paused = false
        ticks = 0
    }
}
