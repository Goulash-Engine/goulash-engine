package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.core.domain.Container
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test

internal class ManualRunnerTest {

    @Test
    fun `should reset ticks`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val manualRunner = ManualRunner(listOf(containerMock, containerMock2))

        manualRunner.tick()
        manualRunner.tick()
        manualRunner.reset()

        assertThat(manualRunner.ticks).isEqualTo(0)
    }

    @Test
    fun `should tick containers`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val manualRunner = ManualRunner(listOf(containerMock, containerMock2))

        manualRunner.tick()

        verifyAll {
            containerMock.tick()
            containerMock2.tick()
        }
        assertThat(manualRunner.ticks).isEqualTo(1)
    }
}
