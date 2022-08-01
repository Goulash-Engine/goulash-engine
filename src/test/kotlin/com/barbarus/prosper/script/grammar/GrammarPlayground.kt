package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.containsAll
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
            activity eating {
                 trigger_urges [ "eat", "brot" ]
                 abort_conditions [ "foo", "bar" ]
                 logic act {
                     actor::urges(eat).plus(1);
                     actor::urges(eat).minus(1);
                 } 
                 
             }
        """.trimIndent()

        val activityScriptContext = playground.parseToEnd(scriptData)

        assertThat(activityScriptContext.activity).isEqualTo("eating")
        assertThat(activityScriptContext.triggerUrges).containsAll("eat", "brot")
        assertThat(activityScriptContext.abortConditions).containsAll("foo", "bar")
        assertThat(activityScriptContext.actLogic).isEqualTo(
            """
            actor::urges(eat).plus(1);actor::urges(eat).minus(1);
            """.trimIndent()
        )
    }
}
