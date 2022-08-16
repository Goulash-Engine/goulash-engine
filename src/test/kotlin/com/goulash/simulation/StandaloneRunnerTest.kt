package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import com.goulash.core.SimulationContext
import com.goulash.core.domain.Container
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

internal class StandaloneRunnerTest {

    @Test
    fun `should run for two seconds and have two ticks`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneRunner = StandaloneRunner(listOf(containerMock, containerMock2))

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneRunner.run(1000)
        }
        Thread.sleep(2500)
        standaloneRunner.pause()

        assertThat(standaloneRunner.ticks).isEqualTo(2)

        standaloneRunner.stop()
    }
}
