package com.barbarus.prosper.script.parser

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import com.barbarus.prosper.script.domain.ListConfiguration
import com.barbarus.prosper.script.exception.UnknownSectionException
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ListConfigurationParserTest {
    private val listConfigurationParser = ListConfigurationParser()

    @Disabled
    @Test
    fun `should break`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
            - baz
            ###
        """.trimIndent()

        val actual: ListConfiguration = listConfigurationParser.parseToEnd(scriptData)

        assertThat(actual.configurations).isEmpty()
    }

    @Test
    fun `should return condition with empty list`() {
        val scriptData = """
            [GlobalBlocker]
        """.trimIndent()

        val actual: ListConfiguration = listConfigurationParser.parseToEnd(scriptData)

        assertThat(actual.configurations).isEmpty()
    }

    @Test
    fun `should throw exception if section identifier is unknown`() {
        val scriptData = """
            [FooBar]
            - foo
            - bar
        """.trimIndent()

        assertThrows<UnknownSectionException> {
            val actual: ListConfiguration = listConfigurationParser.parseToEnd(scriptData)
        }
    }

    @Test
    fun `should parse with grammar`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
        """.trimIndent()

        val actual: ListConfiguration = listConfigurationParser.parseToEnd(scriptData)

        assertThat(actual.configurations).containsAll("foo", "bar")
    }
}
