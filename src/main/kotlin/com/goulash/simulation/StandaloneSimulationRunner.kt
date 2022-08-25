package com.goulash.simulation

import com.goulash.api.http.response.SimulationStatus
import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import com.goulash.script.loader.ScriptLoader
import java.util.concurrent.TimeUnit

class StandaloneSimulationRunner(
    private val containerRunner: ContainerRunner = ContainerRunner()
) : SimulationRunner {
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
        ScriptLoader.load()
        containers.forEach(containerRunner::register)
        running = true
        this.containers = containers
        SimulationHolder.simulation = this
        while (running) {
            while (paused) {
            }
            containerRunner.tick()
            ticks++
            TimeUnit.MILLISECONDS.sleep(millisecondsPerTick)
        }
    }

    fun pause() {
        paused = !paused
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
