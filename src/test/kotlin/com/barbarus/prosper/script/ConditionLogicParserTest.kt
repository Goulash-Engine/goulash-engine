package com.barbarus.prosper.script

import assertk.assertThat
import assertk.assertions.containsAll
import com.barbarus.prosper.script.domain.ScriptedConditionLogic
import com.barbarus.prosper.script.tokenizer.Tokenizer
import org.junit.jupiter.api.Test

internal class ConditionLogicParserTest {
    private val scriptPath: String = "/scripts/global_blocking_condition"

    @Test
    fun `should parse global blocking conditions`() {
        val tokenizer = Tokenizer(listOf(sectionToken(), listValueToken()))
        val parser = ConditionLogicParser(tokenizer)

        val actual: ScriptedConditionLogic = parser.parse("$scriptPath/conditionlogic_1.pros")

        assertThat(actual.globalBlockingConditions).containsAll("dying", "starving")
    }

    private fun sectionToken() = Regex("^\\[(.*)\\]\$")
    private fun listValueToken() = Regex("^- (.*)$")
}
