package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Playground for the `better-parse` API
 */
internal class GrammarPlayground {
    private val playground = PlaygroundGrammar()

    @Test
    @Disabled
    fun `foo my bar`() {
        val scriptData = "[state.health>1]::urge(eat).plus(1);"

        val actual: String = playground.parseToEnd(scriptData)

        assertThat(actual).isEqualTo("state.health>1")
    }
}
