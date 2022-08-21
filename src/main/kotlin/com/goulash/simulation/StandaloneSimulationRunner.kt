package com.goulash.simulation

import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import java.util.concurrent.TimeUnit

class StandaloneSimulationRunner : SimulationRunner {
    private lateinit var containers: List<Container>

    var paused: Boolean = false
        private set
    var running: Boolean = false
        private set
    var ticks: Long = 0
        private set

    override fun getContainers(): List<Container> {
        return this.containers
    }

    fun run(containers: List<Container>, millisecondsPerTick: Long) {
        running = true
        this.containers = containers
        SimulationHolder.simulation = this
        while (!paused) {
            this.containers.forEach(Container::tick)
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
        this.containers = emptyList()
        SimulationHolder.simulation = null
        running = false
        paused = false
        ticks = 0
    }

    override fun toStatus(): SimulationStatus {
        return when {
            paused -> SimulationStatus("paused", ticks)
            running -> SimulationStatus("running", ticks)
            !running -> SimulationStatus("not running", ticks)
            else -> SimulationStatus("unknown", 0)
        }
    }
}