package com.barbarus.prosper.script.parser

import assertk.assertThat
import assertk.assertions.containsAll
import com.barbarus.prosper.script.domain.GlobalBlockerCondition
import com.barbarus.prosper.script.exception.UnknownSectionException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GlobalBlockerConditionScriptParserTest {
    private val globalBlockerConditionScriptParser = GlobalBlockerConditionScriptParser()

    @Test
    fun `should throw exception if section identifier is unknown`() {
        val scriptData = """
            [FooBar]
            - foo
            - bar
        """.trimIndent()

        assertThrows<UnknownSectionException> {
            val actual: GlobalBlockerCondition = globalBlockerConditionScriptParser.parse(scriptData)
        }
    }

    @Test
    fun `should parse a globalblockercondition object from given script data`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
        """.trimIndent()

        val actual: GlobalBlockerCondition = globalBlockerConditionScriptParser.parse(scriptData)

        assertThat(actual.blockerConditions).containsAll("foo", "bar")
    }
}
