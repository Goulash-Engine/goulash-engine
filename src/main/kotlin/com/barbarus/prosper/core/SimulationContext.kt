package com.barbarus.prosper.core

import com.barbarus.prosper.simulation.Simulation

/**
 * Holds the [Simulation] for app wide access.
 */
object SimulationContext {
    var simulation: Simulation? = null
    var pause: Boolean = false

    fun isRunning(): Boolean {
        return simulation != null && !pause
    }
}
