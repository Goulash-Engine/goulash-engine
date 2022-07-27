package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.barbarus.prosper.script.domain.LogicStatement
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class CivilisationScriptLogicGrammarTest {
    private val civilisationScriptLogicGrammar = CivilisationScriptLogicGrammar()

    @Test
    fun `should parse multiple mutation operations`() {
        val scriptData = """
            {
                actors::urge(eat).plus(brot);
                actors::urge(eat).plus(kaese);
            }
        """.trimIndent()

        val actual: List<LogicStatement> = civilisationScriptLogicGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual).hasSize(2)

        assertThat(actual[0].context).isEqualTo("actors")
        assertThat(actual[0].mutationType).isEqualTo("urge")
        assertThat(actual[0].mutationTarget).isEqualTo("eat")
        assertThat(actual[0].mutationOperation).isEqualTo("plus")
        assertThat(actual[0].mutationOperationArgument).isEqualTo("brot")

        assertThat(actual[1].context).isEqualTo("actors")
        assertThat(actual[1].mutationType).isEqualTo("urge")
        assertThat(actual[1].mutationTarget).isEqualTo("eat")
        assertThat(actual[1].mutationOperation).isEqualTo("plus")
        assertThat(actual[1].mutationOperationArgument).isEqualTo("kaese")
    }

    @Test
    fun `should parse simple mutation logic statement`() {
        val scriptData = """
            {
                actors::urge(eat).plus(brot);
            }
        """.trimIndent()

        val actual: List<LogicStatement> = civilisationScriptLogicGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0].context).isEqualTo("actors")
        assertThat(actual[0].mutationType).isEqualTo("urge")
        assertThat(actual[0].mutationTarget).isEqualTo("eat")
        assertThat(actual[0].mutationOperation).isEqualTo("plus")
        assertThat(actual[0].mutationOperationArgument).isEqualTo("brot")
    }
}
