package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.core.domain.Village
import org.junit.jupiter.api.Test

internal class SimulationTest {

    @Test
    fun `all clans should die due to starvation`() {
        val clans = mutableListOf(
            ClanFactory.poorGathererClan()
        )
        val village = Village(clans)
        val simulation = Simulation(village)

        assertThat(simulation.village.clans).hasSize(1)
        simulation.run(700, 1)

        assertThat(simulation.village.clans).hasSize(0)
    }
}
