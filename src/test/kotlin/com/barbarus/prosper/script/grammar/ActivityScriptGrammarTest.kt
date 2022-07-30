package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

/**
 * Tests the grammatical parsing ability for given script data.
 */
internal class ActivityScriptGrammarTest {
    private val activityGrammar = ActivityScriptGrammar()

    @Test
    fun `should parse simple activity script with name and trigger`() {
        val scriptData = """
            activity eat {
                trigger { eat }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.triggerUrges).contains("eat")
    }
}
