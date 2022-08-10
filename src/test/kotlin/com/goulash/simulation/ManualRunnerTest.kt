package com.goulash.simulation

import com.goulash.core.domain.Container
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ManualRunnerTest {

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
    }
}
