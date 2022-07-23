package com.barbarus.prosper.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

internal class WorldDateTest {
    private val worldDate = WorldDate()

    @Test
    fun `should tick on hour base`() {
        worldDate.tick(WorldDate.HOUR)

        assertThat(worldDate.toString()).isEqualTo("00-00-0000 01:00:00")
    }

    @Test
    fun `should tick on year base`() {
        worldDate.tick(WorldDate.YEAR)

        assertThat(worldDate.toString()).isEqualTo("00-00-0001 00:00:00")
    }

    @Test
    fun `should tick on month base`() {
        worldDate.tick(WorldDate.MONTH)

        assertThat(worldDate.toString()).isEqualTo("00-01-0000 00:00:00")
    }

    @Test
    fun `should tick on day base`() {
        worldDate.tick(WorldDate.DAY)

        assertThat(worldDate.toString()).isEqualTo("01-00-0000 00:00:00")
    }

    @Test
    fun `should tick on minute base`() {
        worldDate.tick(WorldDate.MINUTE)

        assertThat(worldDate.toString()).isEqualTo("00-00-0000 00:01:00")
    }

    @Test
    fun `should tell if its night or day`() {
        repeat(60 * 60 * 12) { worldDate.tick() }

        assertThat(worldDate.isDay()).isTrue()
    }

    @Test
    fun `should have proper time format`() {
        // year
        repeat(60 * 60 * 24 * 30 * 12) { worldDate.tick() }
        // two days
        repeat(60 * 60 * 24 * 2) { worldDate.tick() }
        // 30 minutes
        repeat(60 * 30) { worldDate.tick() }

        assertThat(worldDate.toString()).isEqualTo("02-00-0001 00:30:00")
    }

    @Test
    fun `should work with random ticks`() {
        // year
        repeat(60 * 60 * 24 * 30 * 12) { worldDate.tick() }
        // two days
        repeat(60 * 60 * 24 * 2) { worldDate.tick() }
        // 30 minutes
        repeat(60 * 30) { worldDate.tick() }

        assertThat(worldDate.year).isEqualTo(1)
        assertThat(worldDate.month).isEqualTo(0)
        assertThat(worldDate.day).isEqualTo(2)
        assertThat(worldDate.time.hours).isEqualTo(0)
        assertThat(worldDate.time.minutes).isEqualTo(30)
        assertThat(worldDate.time.seconds).isEqualTo(0)
    }

    @Test
    fun `should add add 12 months to time that results in 1 year 0 month 0 day 0 hour 0 minutes 0 seconds`() {
        repeat(12) {
            repeat(30) {
                repeat(24) {
                    repeat(60) {
                        repeat(60) { worldDate.tick() }
                    }
                }
            }
        }

        assertThat(worldDate.year).isEqualTo(1)
        assertThat(worldDate.month).isEqualTo(0)
        assertThat(worldDate.day).isEqualTo(0)
        assertThat(worldDate.time.hours).isEqualTo(0)
        assertThat(worldDate.time.minutes).isEqualTo(0)
        assertThat(worldDate.time.seconds).isEqualTo(0)
    }

    @Test
    fun `should add add 30 days to time that results in 1 month 0 day 0 hour 0 minutes 0 seconds`() {
        repeat(30) {
            repeat(24) {
                repeat(60) {
                    repeat(60) { worldDate.tick() }
                }
            }
        }

        assertThat(worldDate.month).isEqualTo(1)
        assertThat(worldDate.day).isEqualTo(0)
        assertThat(worldDate.time.hours).isEqualTo(0)
        assertThat(worldDate.time.minutes).isEqualTo(0)
        assertThat(worldDate.time.seconds).isEqualTo(0)
    }

    @Test
    fun `should add add 24 hours to time that results in 1 day 0 hour 0 minutes 0 seconds`() {
        repeat(24) {
            repeat(60) {
                repeat(60) { worldDate.tick() }
            }
        }

        assertThat(worldDate.day).isEqualTo(1)
        assertThat(worldDate.time.hours).isEqualTo(0)
        assertThat(worldDate.time.minutes).isEqualTo(0)
        assertThat(worldDate.time.seconds).isEqualTo(0)
    }

    @Test
    fun `should add add 60 minutes to time that results in 1 hour 0 minutes 0 seconds`() {
        repeat(60 * 60) { worldDate.tick() }
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
