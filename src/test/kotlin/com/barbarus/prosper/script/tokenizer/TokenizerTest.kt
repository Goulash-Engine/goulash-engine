package com.barbarus.prosper.script.tokenizer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Test

internal class TokenizerTest {

    @Test
    fun `should find a section and a list value`() {
        val tokenizer = Tokenizer(
            listOf(
                sectionToken(),
                listValueToken()
            )
        )

        val lines = """
            [foo]
            - bar
            - baz
        """.trimIndent().lines()

        val actual = lines.map { tokenizer.tokenize(it) }

        assertThat(actual).isNotEmpty()
        assertThat(actual[0]).isEqualTo("foo")
        assertThat(actual[1]).isEqualTo("bar")
        assertThat(actual[2]).isEqualTo("baz")
    }

    @Test
    fun `should find a list value`() {
        val tokenizer = Tokenizer(listOf(listValueToken()))

        val actual: String = tokenizer.tokenize("- bar")

        assertThat(actual).isEqualTo("bar")
    }

    @Test
    fun `should find a section within a string line`() {
        val tokenizer = Tokenizer(listOf(sectionToken()))

        val actual: String = tokenizer.tokenize("[foo]")

        assertThat(actual).isEqualTo("foo")
    }

    private fun sectionToken() = Regex("^\\[(.*)\\]\$")
    private fun listValueToken() = Regex("^- (.*)$")
}
