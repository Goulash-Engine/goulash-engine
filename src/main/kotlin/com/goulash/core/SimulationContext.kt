package com.goulash.core

import com.goulash.simulation.Simulation

/**
 * Holds the [Simulation] for app wide access.
 */
object SimulationContext {
    var simulation: Simulation? = null

    /**
     * The amount of ticks already simulated.
     */
    var ticks: Int = 0
    var pause: Boolean = false

    fun isRunning(): Boolean {
        return simulation != null && !pause
    }

    fun isPaused(): Boolean {
        return simulation != null && pause
    }
}
