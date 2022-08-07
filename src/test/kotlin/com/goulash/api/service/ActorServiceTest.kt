package com.goulash.api.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.goulash.core.SimulationContext
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

internal class ActorServiceTest {
    private val actorService = ActorService()

    @Test
    fun `should not create actor for key that already exists`() {
        val simulationService = SimulationService()
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            simulationService.start()
        }

        Thread.sleep(1000)
        actorService.registerActor("test")
        actorService.registerActor("test")
        val container = SimulationContext.simulation?.container

        assertThat(container!!).isNotNull()
        assertThat(container.actors).hasSize(1)
        assertThat(container.actors[0].key).isEqualTo("test")

        simulationService.stop()
    }

    @Test
    fun `should create a new actor and add it to the root container`() {
        val simulationService = SimulationService()
        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            simulationService.start()
        }

        Thread.sleep(1000)
        actorService.registerActor("test")
        val container = SimulationContext.simulation?.container

        assertThat(container!!).isNotNull()
        assertThat(container.actors).hasSize(1)
        assertThat(container.actors[0].key).isEqualTo("test")

        simulationService.stop()
    }
}
