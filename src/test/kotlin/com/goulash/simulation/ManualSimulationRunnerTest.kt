package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.core.domain.Container
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test

internal class ManualSimulationRunnerTest {
    @Test
    fun `should reset ticks`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val manualSimulationRunner = ManualSimulationRunner(listOf(containerMock, containerMock2))

        manualSimulationRunner.tick()
        manualSimulationRunner.tick()
        manualSimulationRunner.reset(listOf())

        assertThat(manualSimulationRunner.ticks).isEqualTo(0)
    }

    @Test
    fun `should tick containers`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val manualSimulationRunner = ManualSimulationRunner(listOf(containerMock, containerMock2))

        manualSimulationRunner.tick()

        verifyAll {
            containerMock.tick()
            containerMock2.tick()
        }
        assertThat(manualSimulationRunner.ticks).isEqualTo(1)
    }
}
