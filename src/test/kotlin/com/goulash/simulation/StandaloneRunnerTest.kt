package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.goulash.core.domain.Container
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

internal class StandaloneRunnerTest {

    @Test
    fun `should stop simulation`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneRunner = StandaloneRunner(listOf(containerMock, containerMock2))

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneRunner.run(listOf(containerMock, containerMock2), 10)
        }
        Thread.sleep(50)
        standaloneRunner.stop()

        assertThat(standaloneRunner.running).isFalse()
        assertThat(standaloneRunner.ticks).isEqualTo(0)
    }

    @Test
    fun `should pause simulation`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneRunner = StandaloneRunner(listOf(containerMock, containerMock2))

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneRunner.run(listOf(containerMock, containerMock2), 1000)
        }
        Thread.sleep(50)
        standaloneRunner.pause()

        assertThat(standaloneRunner.paused).isTrue()

        standaloneRunner.stop()
    }

    @Test
    fun `should run for two seconds and have two ticks`() {
        val containerMock: Container = mockk(relaxed = true)
        val containerMock2: Container = mockk(relaxed = true)
        val standaloneRunner = StandaloneRunner(listOf(containerMock, containerMock2))

        val executor = Executors.newSingleThreadExecutor()
        executor.submit {
            standaloneRunner.run(listOf(containerMock, containerMock2), 1000)
        }
        Thread.sleep(2000)
        standaloneRunner.pause()

        assertThat(standaloneRunner.ticks).isEqualTo(2)

        standaloneRunner.stop()
    }
}
