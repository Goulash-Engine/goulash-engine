package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

class SimulationTest {
    private val simulation = Simulation()

    @Test
    fun `should run simulation`() {
        simulation.run()
    }
}
