package com.goulash.script.grammar

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.goulash.script.domain.GlobalBlockerCondition
import com.goulash.script.domain.ListConfiguration
import com.goulash.script.exception.SyntaxException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ListConfigurationGrammarTest {
    private val listConfigurationGrammar = ListConfigurationGrammar()

    @Test
    fun `should throw parser error if one section name is undefined`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
            - baz
            ###
            [Foo]
            - what
            - up
        """.trimIndent()

        assertThrows<SyntaxException> {
            listConfigurationGrammar.parseToEnd(scriptData)
        }
    }

    @Test
    fun `should find two global blocker configs`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
            - baz
            ###
            [GlobalBlocker]
            - what
            - up
        """.trimIndent()

        val actual: List<ListConfiguration> = listConfigurationGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0]).isInstanceOf(GlobalBlockerCondition::class)
        assertThat(actual[0].configurations).containsAll("foo", "bar", "baz")
        assertThat(actual[1]).isInstanceOf(GlobalBlockerCondition::class)
        assertThat(actual[1].configurations).containsAll("what", "up")
    }

    @Test
    fun `should not break if separator occurs`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
            - baz
            ###
        """.trimIndent()

        val actual: List<ListConfiguration> = listConfigurationGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0]).isInstanceOf(GlobalBlockerCondition::class)
        assertThat(actual[0].configurations).containsAll("foo", "bar", "baz")
    }

    @Test
    fun `should return condition with empty list`() {
        val scriptData = """
            [GlobalBlocker]
        """.trimIndent()

        val actual: List<ListConfiguration> = listConfigurationGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0]).isInstanceOf(GlobalBlockerCondition::class)
        assertThat(actual[0].configurations).isEmpty()
    }

    @Test
    fun `should throw exception if section identifier is unknown`() {
        val scriptData = """
            [FooBar]
            - foo
            - bar
        """.trimIndent()

        assertThrows<SyntaxException> {
            listConfigurationGrammar.parseToEnd(scriptData)
        }
    }

    @Test
    fun `should parse with grammar`() {
        val scriptData = """
            [GlobalBlocker]
            - foo
            - bar
        """.trimIndent()

        val actual: List<ListConfiguration> = listConfigurationGrammar.parseToEnd(scriptData)

        assertThat(actual).isNotEmpty()
        assertThat(actual[0]).isInstanceOf(GlobalBlockerCondition::class)
        assertThat(actual[0].configurations).containsAll("foo", "bar")
    }
}
