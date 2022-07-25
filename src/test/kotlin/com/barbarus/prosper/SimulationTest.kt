package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.actor.activity.EatActivity
import com.barbarus.prosper.actor.activity.RestActivity
import com.barbarus.prosper.actor.activity.SleepActivity
import com.barbarus.prosper.actor.activity.ThinkActivity
import com.barbarus.prosper.actor.activity.WorkActivity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.simulation.Simulation
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SimulationTest {

    @Disabled
    @Test
    fun `clan should survive`() {
        val mockedRestActivity = mockk<RestActivity>(relaxed = true)
        val clans: MutableList<Actor> = mutableListOf(
            ClanFactory.testClan(
                mutableListOf(
                    WorkActivity(),
                    mockedRestActivity,
                    ThinkActivity(),
                    EatActivity(),
                    SleepActivity()
                )
            )
        )
        clans.first().inventory().clear()

        val civilisation = Civilisation(clans)
        val simulation = Simulation(
            maximumTicks = 60 * 24 * 30,
            millisecondsPerTick = 1,
            civilisation = civilisation,
            tickBase = WorldDate.MINUTE
        )

        assertThat(simulation.civilisation.actors).hasSize(1)
        simulation.run()
        assertThat(simulation.civilisation.actors).hasSize(1)
    }
}
