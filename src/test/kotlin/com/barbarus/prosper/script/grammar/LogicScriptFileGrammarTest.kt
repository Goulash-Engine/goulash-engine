package com.barbarus.prosper.script.grammar

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.script.logic.ScriptContext
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.parser.ParseException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class LogicScriptFileGrammarTest {
    private val logicScriptFileGrammar = LogicScriptFileGrammar()

    @Test
    fun `should increase the urge two times to eat for every actor`() {
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
    fun `should increase the urge to eat for every actor`() {
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

    @Test
    fun `should parse simple logic statement`() {
        val scriptData = """
            logic myfoo {
                actors::urge(eat).plus(1);
                actors::urge(eat).plus(0.5);
            }
        """.trimIndent()

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)
    }
}
