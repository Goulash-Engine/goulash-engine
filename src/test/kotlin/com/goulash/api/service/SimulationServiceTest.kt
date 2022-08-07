package com.goulash.api.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import com.goulash.core.SimulationContext
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep
import java.util.concurrent.Executors

internal class SimulationServiceTest {
    private val simulationService = SimulationService()

    @Test
    fun `should update the amount of ticks ran by the simulation`() {
        assertThat(SimulationContext.ticks).isEqualTo(0)

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            simulationService.start()
        }
        sleep(1000)

        assertThat(SimulationContext.ticks).isGreaterThan(0)

        simulationService.stop()
    }

    @Test
    fun `should reset ticks when stopped`() {
        SimulationContext.ticks = 10

        simulationService.stop()

        assertThat(SimulationContext.ticks).isEqualTo(0)
    }
}
