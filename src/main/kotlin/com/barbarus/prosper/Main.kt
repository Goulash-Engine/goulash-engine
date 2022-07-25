package com.barbarus.prosper

import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.simulation.Simulation

fun main() {
    val simulation = Simulation(
        millisecondsPerTick = 1,
        tickBase = WorldDate.MINUTE,
        render = true
    )
    simulation.run()
}
