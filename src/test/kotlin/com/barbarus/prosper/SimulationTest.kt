package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.hasSize
import org.junit.jupiter.api.Test

internal class SimulationTest {

    @Test
    fun `all clans should die due to starvation`() {
        val simulation = Simulation()

        assertThat(simulation.village.clans).hasSize(1)
        simulation.run(1000, 1)

        assertThat(simulation.village.clans).hasSize(0)
    }
}
