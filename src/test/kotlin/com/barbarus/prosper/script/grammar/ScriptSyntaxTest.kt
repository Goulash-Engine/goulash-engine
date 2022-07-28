package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.script.domain.ScriptedLogic
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

internal class ScriptSyntaxTest {
    private val logicScriptFileGrammar = LogicScriptFileGrammar()

    @Test
    fun `should increase urge of all actors`() {
        val scriptData = """
            logic myfoo {
                actors::urge(foo).plus(1);
                actors::urge(bar).plus(0.5);
                actors::urge(foo).plus(2);
            }
        """.trimIndent()

        val one = ClanFactory.testClan()
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))

        val actual: ScriptedLogic<Civilisation> = logicScriptFileGrammar.parseToEnd(scriptData)
        actual.process(civilisation)

        assertThat(one.urges.getUrges()["foo"]).isEqualTo(3.0)
        assertThat(one.urges.getUrges()["bar"]).isEqualTo(0.5)
        assertThat(two.urges.getUrges()["foo"]).isEqualTo(3.0)
        assertThat(two.urges.getUrges()["bar"]).isEqualTo(0.5)
    }
}