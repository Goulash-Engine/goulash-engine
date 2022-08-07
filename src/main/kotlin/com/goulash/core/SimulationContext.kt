package com.goulash.core

import com.goulash.api.http.response.SimulationStatus
import com.goulash.simulation.Simulation

/**
 * Holds the [Simulation] for app wide access.
 */
object SimulationContext {
    var simulation: Simulation? = null

    /**
     * The amount of ticks already simulated.
     */
    var ticks: Long = 0
    var paused: Boolean = false
    var stopped: Boolean = false

    fun isRunning(): Boolean {
        return simulation != null && !paused
    }

    private fun isPaused(): Boolean {
        return simulation != null && paused
    }

    fun toStatus(): SimulationStatus {
        return when {
            isPaused() -> SimulationStatus("paused", ticks)
            isRunning() -> SimulationStatus("running", ticks)
            !isRunning() -> SimulationStatus("not running", ticks)
            else -> SimulationStatus("unknown", 0)
        }
    }
}
