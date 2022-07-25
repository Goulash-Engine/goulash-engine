package com.barbarus.prosper.script

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class GlobalBlockingConditionParserTest {
    private val parser = GlobalBlockingConditionParser()
    private val scriptPath: String = "/scripts/global_blocking_condition"

    @Test
    fun `should parse gcp_1 test script and return foo`() {
        val actual = parser.parse(scriptPath)

        assertThat(actual.trim()).isEqualTo("foo")
    }
}
