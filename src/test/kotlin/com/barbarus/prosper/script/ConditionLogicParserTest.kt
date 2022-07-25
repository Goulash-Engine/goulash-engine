package com.barbarus.prosper.script

import assertk.assertThat
import assertk.assertions.containsAll
import com.barbarus.prosper.script.domain.ScriptedConditionLogic
import com.barbarus.prosper.script.tokenizer.Tokenizer
import org.junit.jupiter.api.Test

internal class ConditionLogicParserTest {
    private val scriptPath: String = "/logic"

    @Test
    fun `should parse global blocking conditions`() {
        val tokenizer = Tokenizer(listOf(sectionToken(), listValueToken()))
        val parser = ConditionLogicParser(tokenizer)

        val data = javaClass.getResource("$scriptPath/conditionlogic_1.pros").readText()
        val actual: ScriptedConditionLogic = parser.parse(data)

        assertThat(actual.globalBlockingConditions).containsAll("dying", "starving")
    }

    private fun sectionToken() = Regex("^\\[(.*)\\]\$")
    private fun listValueToken() = Regex("^- (.*)$")
}
