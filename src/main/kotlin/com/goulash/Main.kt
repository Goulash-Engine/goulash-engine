package com.goulash

import com.goulash.core.domain.WorldDate
import com.goulash.script.loader.ScriptLoader
import com.goulash.simulation.Simulation

fun main() {
    ScriptLoader.load()
    val simulation = Simulation(
        millisecondsPerTick = 100,
        tickBase = WorldDate.MINUTE
    )
    simulation.run()
}
