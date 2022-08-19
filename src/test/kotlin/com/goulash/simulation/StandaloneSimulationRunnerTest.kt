package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.goulash.core.domain.Container
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

internal class StandaloneSimulationRunnerTest {

    @Test
    fun `should stop simulation`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneSimulationRunner = StandaloneSimulationRunner()

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneSimulationRunner.run(listOf(containerMock, containerMock2), 10)
        }
        Thread.sleep(50)
        standaloneSimulationRunner.stop()

        assertThat(standaloneSimulationRunner.running).isFalse()
        assertThat(standaloneSimulationRunner.ticks).isEqualTo(0)
    }

    @Test
    fun `should pause simulation`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneSimulationRunner = StandaloneSimulationRunner()

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneSimulationRunner.run(listOf(containerMock, containerMock2), 1000)
        }
        Thread.sleep(50)
        standaloneSimulationRunner.pause()

        assertThat(standaloneSimulationRunner.paused).isTrue()

        standaloneSimulationRunner.stop()
    }

    @Test
    fun `should run for two seconds and have two ticks`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneSimulationRunner = StandaloneSimulationRunner()

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneSimulationRunner.run(listOf(containerMock, containerMock2), 1000)
        }
        Thread.sleep(2000)
        standaloneSimulationRunner.pause()

        assertThat(standaloneSimulationRunner.ticks).isEqualTo(2)

        standaloneSimulationRunner.stop()
    }
}
