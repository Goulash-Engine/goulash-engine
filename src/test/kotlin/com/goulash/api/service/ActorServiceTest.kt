package com.goulash.api.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.goulash.core.SimulationHolder
import com.goulash.core.domain.Container
import com.goulash.simulation.StandaloneSimulationRunner
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

internal class ActorServiceTest {
    private val actorService = ActorService()

    @Test
    fun `should not create actor for key that already exists`() {
        val runner = StandaloneSimulationRunner()
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            runner.run(listOf(Container()), 1000)
        }

        Thread.sleep(1000)
        actorService.registerActor("test")
        actorService.registerActor("test")
        val container = SimulationHolder.simulation?.getContainers()?.first()

        assertThat(container!!).isNotNull()
        assertThat(container.actors).hasSize(1)
        assertThat(container.actors[0].key).isEqualTo("test")

        runner.stop()
    }

    @Test
    fun `should create a new actor and add it to the root container`() {
        val runner = StandaloneSimulationRunner()
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            runner.run(listOf(Container()), 1000)
        }

        Thread.sleep(1000)
        actorService.registerActor("test")
        val container = SimulationHolder.simulation?.getContainers()?.first()

        assertThat(container!!).isNotNull()
        assertThat(container.actors).hasSize(1)
        assertThat(container.actors[0].key).isEqualTo("test")

        runner.stop()
    }
}
