package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class LogicScriptFileGrammarTest {
    private val logicScriptFileGrammar = LogicScriptFileGrammar()

    @Test
    fun `should parse simple logic statement`() {
        val scriptData = """
            logic myfoo {
                actors::urge(eat).plus(1);
                actors::urge(eat).plus(0.5);
            }
        """.trimIndent()

        val actual: ScriptedLogic<Civilisation> = logicScriptFileGrammar.parseToEnd(scriptData)

        assertThat(actual.name).isEqualTo("myfoo")
    }
}
