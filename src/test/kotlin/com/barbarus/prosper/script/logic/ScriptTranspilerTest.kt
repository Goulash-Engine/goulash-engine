package com.barbarus.prosper.script.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.grammar.LogicScriptFileGrammar
import org.junit.jupiter.api.Test

internal class ScriptTranspilerTest {

    @Test
    fun `should transpile script context`() {
        val scriptTranspiler = ScriptTranspiler()
        val scriptContext = ScriptContext(
            LogicScriptFileGrammar.ScriptHead("foo"),
            listOf(ScriptStatement("actors", "urge", "eat", "plus", "1"))
        )

        val scriptedLogic = scriptTranspiler.transpile(scriptContext)

        val testClan = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(testClan))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(civilisation)

        assertThat(testClan.urges.getUrges()["eat"]).isEqualTo(1.0)
    }
}
