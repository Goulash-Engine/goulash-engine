package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.script.logic.ScriptContext
import com.barbarus.prosper.script.logic.ScriptTranspiler
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

/**
 * This test is a whitebox test that asserts functionality from script data to the
 * effects on the context.
 */
internal class ScriptSyntaxTest {
    private val logicScriptFileGrammar = LogicScriptFileGrammar()

    @Test
    fun `should decrease actors health down three times by 10`() {
        val scriptData = """
            logic myfoo {
                actors::state(health).minus(10);
                actors::state(health).minus(10);
                actors::state(health).minus(10);
            }
        """.trimIndent()

        val one = ClanFactory.testClan()
        one.state.health = 100.0
        val civilisation = Civilisation(mutableListOf(one))

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)
        val transpiler = ScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(civilisation)

        assertThat(one.state.health).isEqualTo(70.0)
    }

    @Test
    fun `should set health to 100 if over 100`() {
        val scriptData = """
            logic myfoo {
                actors::state(health).plus(50);
                actors[state.health > 100]::state(health).set(100);
            }
        """.trimIndent()

        val one = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one))

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)
        val transpiler = ScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(civilisation)

        assertThat(one.state.health).isEqualTo(100.0)
    }

    @Test
    fun `should set eat urge of actors where health is equal 30 to 20`() {
        val scriptData = """
            logic myfoo {
                actors[state.health = 50]::urge(foo).set(20);
            }
        """.trimIndent()

        val one = ClanFactory.testClan()
        one.state.health = 30.0
        val two = ClanFactory.testClan()
        two.state.health = 50.0
        val civilisation = Civilisation(mutableListOf(one, two))

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)
        val transpiler = ScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(civilisation)

        assertThat(one.urges.getUrgeOrNull("foo")).isNull()
        assertThat(two.urges.getUrgeOrNull("foo")).isEqualTo(20.0)
    }

    @Test
    fun `should increase urge of all actors where health is greater than 50`() {
        val scriptData = """
            logic myfoo {
                actors[state.health > 50]::urge(foo).plus(1);
            }
        """.trimIndent()

        val one = ClanFactory.testClan()
        one.state.health = 30.0
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)
        val transpiler = ScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(civilisation)

        assertThat(one.urges.getUrgeOrNull("foo")).isNull()
        assertThat(two.urges.getUrgeOrNull("foo")).isEqualTo(1.0)
    }

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

        val actual: ScriptContext = logicScriptFileGrammar.parseToEnd(scriptData)
        val transpiler = ScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(civilisation)

        assertThat(one.urges.getUrgeOrNull("foo")).isEqualTo(3.0)
        assertThat(one.urges.getUrgeOrNull("bar")).isEqualTo(0.5)
        assertThat(two.urges.getUrgeOrNull("foo")).isEqualTo(3.0)
        assertThat(two.urges.getUrgeOrNull("bar")).isEqualTo(0.5)
    }
}
