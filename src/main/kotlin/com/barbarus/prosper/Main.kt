package com.barbarus.prosper

import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.script.loader.ScriptLoader
import com.barbarus.prosper.simulation.Simulation

fun main() {
    ScriptLoader.load()
    val simulation = Simulation(
        millisecondsPerTick = 100,
        tickBase = WorldDate.MINUTE
    )
    simulation.run()
}
