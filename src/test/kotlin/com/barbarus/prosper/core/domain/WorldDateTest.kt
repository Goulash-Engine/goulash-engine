package com.barbarus.prosper.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class WorldDateTest {
    private val worldDate = WorldDate()

    @Test
    fun `should add add 60 minutes to time that results in 1 hour 0 minutes 0 seconds`() {
        repeat(60) {
            repeat(60) { worldDate.tick() }
        }
        assertThat(worldDate.time.hours).isEqualTo(1)
        assertThat(worldDate.time.minutes).isEqualTo(0)
        assertThat(worldDate.time.seconds).isEqualTo(0)
    }

    @Test
    fun `should add add 60 seconds to time that results in 1 minute 0 seconds`() {
        repeat(60) { worldDate.tick() }
        assertThat(worldDate.time.minutes).isEqualTo(1)
        assertThat(worldDate.time.seconds).isEqualTo(0)
    }

    @Test
    fun `should add two seconds to time`() {
        worldDate.tick()
        worldDate.tick()
        assertThat(worldDate.time.seconds).isEqualTo(2)
    }

    @Test
    fun `should add seconds to time`() {
        worldDate.tick()
        assertThat(worldDate.time.seconds).isEqualTo(1)
    }
}
