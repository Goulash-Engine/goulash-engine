package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.actor.activity.EatActivity
import com.barbarus.prosper.actor.activity.RestActivity
import com.barbarus.prosper.actor.activity.SleepActivity
import com.barbarus.prosper.actor.activity.ThinkActivity
import com.barbarus.prosper.actor.activity.WorkActivity
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.core.domain.WorldDate
import com.barbarus.prosper.factory.ActorFactory
import com.barbarus.prosper.simulation.Simulation
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SimulationTest {

    @Disabled
    @Test
    fun `actor should survive`() {
        val mockedRestActivity = mockk<RestActivity>(relaxed = true)
        val actors: MutableList<Actor> = mutableListOf(
            ActorFactory.testActor(
                mutableListOf(
                    WorkActivity(),
                    mockedRestActivity,
                    ThinkActivity(),
                    EatActivity(),
                    SleepActivity()
                )
            )
        )
        actors.first().inventory().clear()

        val container = Container(actors)
        val simulation = Simulation(
            maximumTicks = 60 * 24 * 30,
            millisecondsPerTick = 1,
            tickBase = WorldDate.MINUTE
        )

        assertThat(simulation.container.actors).hasSize(1)
        simulation.run()
        assertThat(simulation.container.actors).hasSize(1)
    }
}
