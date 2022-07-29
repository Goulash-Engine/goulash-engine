package com.barbarus.prosper.script.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.script.domain.ScriptStatement
import com.barbarus.prosper.script.grammar.LogicScriptFileGrammar
import org.junit.jupiter.api.Test

internal class ScriptTranspilerTest {

    @Test
    fun `should transpile health set to 50_0`() {
        val scriptTranspiler = ScriptTranspiler()
        val scriptContext = ScriptContext(
            LogicScriptFileGrammar.ScriptHead("foo"),
            listOf(
                ScriptStatement(
                    "actors",
                    "",
                    "state",
                    "health",
                    "set",
                    "50"
                )
            )
        )

        val scriptedLogic = scriptTranspiler.transpile(scriptContext)

        val testClan = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(testClan))

        scriptedLogic.process(civilisation)

        assertThat(testClan.state.health).isEqualTo(50.0)
    }
    @Test
    fun `should transpile filter for greater than`() {
        val scriptTranspiler = ScriptTranspiler()
        val scriptContext = ScriptContext(
            LogicScriptFileGrammar.ScriptHead("foo"),
            listOf(
                ScriptStatement(
                    "actors",
                    "state.health > 60",
                    "urge",
                    "eat",
                    "plus",
                    "1"
                )
            )
        )

        val scriptedLogic = scriptTranspiler.transpile(scriptContext)

        val testClan = ClanFactory.testClan()
        testClan.state.health = 61.0
        val civilisation = Civilisation(mutableListOf(testClan))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(civilisation)

        assertThat(testClan.urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }

    @Test
    fun `should transpile filter for less than`() {
        val scriptTranspiler = ScriptTranspiler()
        val scriptContext = ScriptContext(
            LogicScriptFileGrammar.ScriptHead("foo"),
            listOf(
                ScriptStatement(
                    "actors",
                    "state.health < 20",
                    "urge",
                    "eat",
                    "plus",
                    "1"
                )
            )
        )

        val scriptedLogic = scriptTranspiler.transpile(scriptContext)

        val testClan = ClanFactory.testClan()
        testClan.state.health = 30.0
        val civilisation = Civilisation(mutableListOf(testClan))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(civilisation)

        assertThat(testClan.urges.getUrgeOrNull("eat")).isNull()
    }

    @Test
    fun `should transpile script context`() {
        val scriptTranspiler = ScriptTranspiler()
        val scriptContext = ScriptContext(
            LogicScriptFileGrammar.ScriptHead("foo"),
            listOf(
                ScriptStatement(
                    "actors",
                    "",
                    "urge",
                    "eat",
                    "plus",
                    "1"
                )
            )
        )

        val scriptedLogic = scriptTranspiler.transpile(scriptContext)

        val testClan = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(testClan))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(civilisation)

        assertThat(testClan.urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
