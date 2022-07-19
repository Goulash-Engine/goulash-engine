package com.barbarus.prosper

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class SimulationTest {

    @Test
    fun `should run simulation`() {
        val simulation = Simulation()
        simulation.run()
    }
}
