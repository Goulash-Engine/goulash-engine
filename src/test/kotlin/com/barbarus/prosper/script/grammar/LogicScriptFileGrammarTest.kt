package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.script.domain.ScriptedLogic
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

        val one = ClanFactory.testClan()
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))

        val actual: ScriptedLogic<Civilisation> = logicScriptFileGrammar.parseToEnd(scriptData)
        actual.process(civilisation)

        assertThat(one.urges.getUrges()["eat"]).isEqualTo(2.0)
        assertThat(two.urges.getUrges()["eat"]).isEqualTo(2.0)
    }

    @Test
    fun `should increase the urge to eat for every actor`() {
        val scriptData = """
            logic myfoo {
                actors::urge(eat).plus(1);
            }
        """.trimIndent()

        val one = ClanFactory.testClan()
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))

        val actual: ScriptedLogic<Civilisation> = logicScriptFileGrammar.parseToEnd(scriptData)
        actual.process(civilisation)

        assertThat(one.urges.getUrges()["eat"]).isEqualTo(1.0)
        assertThat(two.urges.getUrges()["eat"]).isEqualTo(1.0)
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

        val actual: ScriptedLogic<Civilisation> = logicScriptFileGrammar.parseToEnd(scriptData)

        assertThat(actual.name).isEqualTo("myfoo")
    }
}
