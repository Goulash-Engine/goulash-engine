package com.barbarus.prosper.script

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.script.domain.ScriptedConditionLogic
import org.junit.jupiter.api.Test

internal class ConditionLogicParserTest {
    private val parser = ConditionLogicParser()
    private val scriptPath: String = "/scripts/global_blocking_condition"

    @Test
    fun `should parse global blocking conditions`() {
        val actual: ScriptedConditionLogic = parser.parse("$scriptPath/conditionlogic_1.pros")
        assertThat(actual.globalBlockingConditions).contains(listOf("dying", "unconscious"))
    }
}
