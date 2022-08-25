package com.goulash.core

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.goulash.core.domain.Actor
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RequirementsProviderTest {
    @Test
    fun `should add multiple requirements`() {
        val actor: Actor = mockk()
        RequirementsProvider.satisfy(actor, "food")
        RequirementsProvider.satisfy(actor, "food")

        assertThat(RequirementsProvider.providesAll(actor)).hasSize(2)
        assertThat(RequirementsProvider.providesAll(actor)).containsAll("food", "food")
    }

    @Test
    fun `should consume multiple requirements`() {
        val actor: Actor = mockk()
        RequirementsProvider.satisfy(actor, "food")
        RequirementsProvider.satisfy(actor, "food")

        assertThat(RequirementsProvider.consume(actor, "food")).isTrue()
        assertThat(RequirementsProvider.isProvided(actor, "food")).isTrue()
        assertThat(RequirementsProvider.consume(actor, "food")).isTrue()
        assertThat(RequirementsProvider.isProvided(actor, "food")).isFalse()
    }

    @Test
    fun `should consume requirement`() {
        val actor: Actor = mockk()
        RequirementsProvider.satisfy(actor, "food")

        assertThat(RequirementsProvider.consume(actor, "food")).isTrue()
        assertThat(RequirementsProvider.isProvided(actor, "food")).isFalse()
    }

    @Test
    fun `should check if requirement is provided`() {
        val actor: Actor = mockk()
        RequirementsProvider.satisfy(actor, "food")

        assertThat(RequirementsProvider.isProvided(actor, "food")).isTrue()
    }

    @Test
    fun `should return empty list`() {
        assertThat(RequirementsProvider.providesAll(mockk())).isEmpty()
    }

    @Test
    fun `should add requirement to the provided map`() {
        val actor: Actor = mockk()
        RequirementsProvider.satisfy(actor, "food")
        assertThat(RequirementsProvider.providesAll(actor)).contains("food")
    }

}

