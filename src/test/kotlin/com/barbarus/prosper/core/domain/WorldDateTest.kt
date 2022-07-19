package com.barbarus.prosper.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class WorldDateTest {
    private val worldDate = WorldDate()

    @Test
    fun `should add seconds to time`() {
        worldDate.tickSecond()
        assertThat(worldDate.time.seconds).isEqualTo(1)
    }
}
