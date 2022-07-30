package com.barbarus.prosper.script.grammar

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

        // val actual: ScriptContext = activityGrammar.parseToEnd(scriptData)
        //
        // assertThat(actual.head.name).isEqualTo("myfoo")
        // assertThat(actual.statements[0].mutationType).isEqualTo("urge")
        // assertThat(actual.statements[0].filter).isEqualTo("state.health > 1")
        // assertThat(actual.statements[0].mutationTarget).isEqualTo("eat")
        // assertThat(actual.statements[0].mutationOperation).isEqualTo("plus")
        // assertThat(actual.statements[0].mutationOperationArgument).isEqualTo("1")
        // assertThat(actual.statements[1].mutationType).isEqualTo("urge")
        // assertThat(actual.statements[1].filter).isEqualTo("")
        // assertThat(actual.statements[1].mutationTarget).isEqualTo("eat")
        // assertThat(actual.statements[1].mutationOperation).isEqualTo("plus")
        // assertThat(actual.statements[1].mutationOperationArgument).isEqualTo("2")
    }
}
