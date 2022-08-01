package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

/**
 * Playground for the `better-parse` API
 */
internal class GrammarPlayground {
    private val playground = PlaygroundGrammar()

    @Test
    fun `should parse logic for on finished`() {
        val scriptData = """
            activity foo { 
                logic act { 
                    actor::urges(eat).plus(1);
                    actor[state.health > 10]::urges(eat).plus(10);
                 } 
            }
        """.trimIndent()

        val logic = playground.parseToEnd(scriptData)

        assertThat(logic.replace(" ", "").trimIndent()).isEqualTo(
            """
                    actor::urges(eat).plus(1);actor[state.health > 10]::urges(eat).plus(10);
            """.trimIndent()
        )
    }
}
