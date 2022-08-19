package com.goulash.core

import com.goulash.simulation.Simulation
import com.goulash.simulation.SimulationRunner

/**
 * Holds the [Simulation] for app wide access.
 */
object SimulationHolder {
    var simulation: SimulationRunner? = null

    /**
     * The amount of ticks already simulated.
     */
    var ticks: Long = 0
    var paused: Boolean = false
    var stopped: Boolean = false

    fun isRunning(): Boolean {
        if (simulation == null) {
            return false
        }
        return simulation!!.toStatus().status != "paused"
    }
}
