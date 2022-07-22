package com.barbarus.prosper.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class UrgesTest {
    private val urges = Urges()

    @Test
    fun `should reset urge`() {
        urges.increaseUrge("rest", 1.0)
        urges.increaseUrge("rest", 1.0)
        urges.increaseUrge("rest", 1.0)
        urges.resetUrge("rest")

        assertThat(urges.getUrges()["rest"]).isEqualTo(0.0)
    }

    @Test
    fun `should decrease urge by 1_0`() {
        urges.increaseUrge("rest", 1.0)
        urges.increaseUrge("rest", 1.0)
        urges.decreaseUrge("rest", 1.0)

        assertThat(urges.getUrges()["rest"]).isEqualTo(1.0)
    }

    @Test
    fun `should increase urge by 2_0`() {
        urges.increaseUrge("rest", 1.0)
        urges.increaseUrge("rest", 1.0)

        assertThat(urges.getUrges()["rest"]).isEqualTo(2.0)
    }

    @Test
    fun `should increase urge by 1_0`() {
        urges.increaseUrge("rest", 1.0)

        assertThat(urges.getUrges()["rest"]).isEqualTo(1.0)
    }
}
