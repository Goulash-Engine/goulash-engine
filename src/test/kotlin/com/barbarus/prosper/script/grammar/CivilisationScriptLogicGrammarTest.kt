package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.barbarus.prosper.script.domain.LogicStatement
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class CivilisationScriptLogicGrammarTest {
    private val civilisationScriptLogicGrammar = CivilisationScriptLogicGrammar()

    @Test
    fun `should parse simple logic statement`() {
        val scriptData = """
            {
                actors::urge(eat);
            }
        """.trimIndent()

        val actual: List<LogicStatement> = civilisationScriptLogicGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0].context).isEqualTo("actors")
        assertThat(actual[0].mutationType).isEqualTo("urge")
        assertThat(actual[0].mutationTarget).isEqualTo("eat")
    }
    // actors.urge("eat").plus(1);
}
