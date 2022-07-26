package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.core.logic.Logic
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class CivilisationLogicGrammarTest {
    private val civilisationLogicGrammar = CivilisationLogicGrammar()

    @Test
    fun `should parse two sections`() {
        val scriptData = """
            [Logic:Civilisation]
            ###
            [Logic:Civilisation]
            ###
        """.trimIndent()

        val actual: List<Logic<Civilisation>> = civilisationLogicGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual).hasSize(2)
        assertThat(actual[0]).isInstanceOf(ScriptedLogic::class)
        assertThat(actual[1]).isInstanceOf(ScriptedLogic::class)
    }

    @Test
    fun `should parse with grammar`() {
        val scriptData = """
            [Logic:Civilisation]
        """.trimIndent()

        val actual: List<Logic<Civilisation>> = civilisationLogicGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0]).isInstanceOf(ScriptedLogic::class)
    }
}
