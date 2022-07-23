package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.simulation.Simulation
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SimulationTest {

    @Disabled
    @Test
    fun `all clans should die due to starvation`() {
        val clans: MutableList<Actor> = mutableListOf(
            ClanFactory.poorGathererClan()
        )
        clans.first().inventory().clear()

        val civilisation = Civilisation(clans)
        val simulation = Simulation(maximumTicks = 1000, millisecondsPerTick = 1, civilisation = civilisation)

        assertThat(simulation.civilisation.actors).hasSize(1)
        simulation.run()

        assertThat(simulation.civilisation.actors).hasSize(0)
    }
}
