package com.goulash.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class FilterGrammarTest {

    private val filterGrammar = FilterGrammar()

    @Test
    fun `should parse filter for state health lesser equal to 50`() {
        val filter = "state.health <= 50"

        val contextFilter = filterGrammar.parseToEnd(filter)

        assertThat(contextFilter.type).isEqualTo("state")
        assertThat(contextFilter.attribute).isEqualTo("health")
        assertThat(contextFilter.operator).isEqualTo("<=")
        assertThat(contextFilter.argument).isEqualTo("50")
    }

    @Test
    fun `should parse filter for state health greater equal to 50`() {
        val filter = "state.health >= 50"

        val contextFilter = filterGrammar.parseToEnd(filter)

        assertThat(contextFilter.type).isEqualTo("state")
        assertThat(contextFilter.attribute).isEqualTo("health")
        assertThat(contextFilter.operator).isEqualTo(">=")
        assertThat(contextFilter.argument).isEqualTo("50")
    }

    @Test
    fun `should parse filter for state health equal to 50`() {
        val filter = "state.health = 50"

        val contextFilter = filterGrammar.parseToEnd(filter)

        assertThat(contextFilter.type).isEqualTo("state")
        assertThat(contextFilter.attribute).isEqualTo("health")
        assertThat(contextFilter.operator).isEqualTo("=")
        assertThat(contextFilter.argument).isEqualTo("50")
    }

    @Test
    fun `should parse filter for state health lesser than 50`() {
        val filter = "state.health < 50"

        val contextFilter = filterGrammar.parseToEnd(filter)

        assertThat(contextFilter.type).isEqualTo("state")
        assertThat(contextFilter.attribute).isEqualTo("health")
        assertThat(contextFilter.operator).isEqualTo("<")
        assertThat(contextFilter.argument).isEqualTo("50")
    }

    @Test
    fun `should parse filter for state health greater than 50`() {
        val filter = "state.health > 50"

        val contextFilter = filterGrammar.parseToEnd(filter)

        assertThat(contextFilter.type).isEqualTo("state")
        assertThat(contextFilter.attribute).isEqualTo("health")
        assertThat(contextFilter.operator).isEqualTo(">")
        assertThat(contextFilter.argument).isEqualTo("50")
    }
}
