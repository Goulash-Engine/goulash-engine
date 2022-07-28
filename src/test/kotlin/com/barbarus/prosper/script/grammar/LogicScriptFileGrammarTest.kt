package com.barbarus.prosper.script.grammar

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.script.logic.ScriptContext
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests the grammatical parsing ability for given script data.
 */
internal class LogicScriptFileGrammarTest {
    private val logicScriptFileGrammar = LogicScriptFileGrammar()

    @Test
    fun `should parse statement with filter and no filter mutation operation`() {
        val scriptData = """
            logic myfoo {
                actors[state.health > 1]::urge(eat).plus(1);
                actors::urge(eat).plus(2);
            }
        """.trimIndent()

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)

        assertThat(actual.head.name).isEqualTo("myfoo")
        assertThat(actual.statements[0].mutationType).isEqualTo("urge")
        assertThat(actual.statements[0].filter).isEqualTo("state.health > 1")
        assertThat(actual.statements[0].mutationTarget).isEqualTo("eat")
        assertThat(actual.statements[0].mutationOperation).isEqualTo("plus")
        assertThat(actual.statements[0].mutationOperationArgument).isEqualTo("1")
        assertThat(actual.statements[1].mutationType).isEqualTo("urge")
        assertThat(actual.statements[1].filter).isEqualTo("")
        assertThat(actual.statements[1].mutationTarget).isEqualTo("eat")
        assertThat(actual.statements[1].mutationOperation).isEqualTo("plus")
        assertThat(actual.statements[1].mutationOperationArgument).isEqualTo("2")
    }
    @Test
    fun `should parse statement with filter`() {
        val scriptData = """
            logic myfoo {
                actors[state.health > 1]::urge(eat).plus(1);
            }
        """.trimIndent()

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)

        assertThat(actual.head.name).isEqualTo("myfoo")
        assertAll {
            actual.statements.forEach {
                assertThat(it.mutationType).isEqualTo("urge")
                assertThat(it.filter).isEqualTo("state.health > 1")
                assertThat(it.mutationTarget).isEqualTo("eat")
                assertThat(it.mutationOperation).isEqualTo("plus")
                assertThat(it.mutationOperationArgument).isEqualTo("1")
            }
        }
    }

    @Test
    fun `should parse multiple script statements`() {
        val scriptData = """
            logic myfoo {
                actors::urge(eat).plus(1);
                actors::urge(eat).plus(1);
            }
        """.trimIndent()

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)

        assertThat(actual.head.name).isEqualTo("myfoo")
        assertAll {
            actual.statements.forEach {
                assertThat(it.mutationType).isEqualTo("urge")
                assertThat(it.mutationTarget).isEqualTo("eat")
                assertThat(it.mutationOperation).isEqualTo("plus")
                assertThat(it.mutationOperationArgument).isEqualTo("1")
            }
        }
    }

    @Test
    fun `should parse simple script statement`() {
        val scriptData = """
            logic myfoo {
                actors::urge(eat).plus(1);
            }
        """.trimIndent()

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)

        assertThat(actual.head.name).isEqualTo("myfoo")
        assertAll {
            actual.statements.forEach {
                assertThat(it.mutationType).isEqualTo("urge")
                assertThat(it.mutationTarget).isEqualTo("eat")
                assertThat(it.mutationOperation).isEqualTo("plus")
                assertThat(it.mutationOperationArgument).isEqualTo("1")
            }
        }
    }

    @Test
    fun `should fail if logic with empty block`() {
        val scriptData = """
            logic myfoo { }
        """.trimIndent()

        assertThrows<ParseException> {
            logicScriptFileGrammar.parseToEnd(scriptData)
        }
    }
}
