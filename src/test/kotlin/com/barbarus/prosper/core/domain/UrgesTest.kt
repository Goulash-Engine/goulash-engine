package com.barbarus.prosper.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import org.junit.jupiter.api.Test

internal class UrgesTest {
    private val urges = Urges()

    @Test
    fun `should do nothing if the urge to decrease does not exist`() {
        urges.decreaseUrge("rest", 2.0)

        assertThat(urges.getUrges().containsKey("rest")).isFalse()
    }

    @Test
    fun `remove urge if under 0`() {
        urges.increaseUrge("rest", 1.0)
        urges.decreaseUrge("rest", 2.0)

        assertThat(urges.getUrges().containsKey("rest")).isFalse()
    }

    @Test
    fun `should reset urge`() {
        urges.increaseUrge("rest", 1.0)
        urges.increaseUrge("rest", 1.0)
        urges.increaseUrge("rest", 1.0)
        urges.stopUrge("rest")

        assertThat(urges.getUrges().containsKey("rest")).isFalse()
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
