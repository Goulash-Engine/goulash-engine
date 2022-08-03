package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.barbarus.prosper.core.domain.Container
import com.barbarus.prosper.factory.ActorFactory
import com.barbarus.prosper.script.logic.ContainerScriptContext
import com.barbarus.prosper.script.logic.ContainerScriptTranspiler
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

/**
 * This test is a whitebox test that asserts functionality from script data to the
 * effects on the context.
 */
internal class LogicSyntaxTest {
    private val containerScriptGrammar = ContainerScriptGrammar()

    @Test
    fun `should reset only reset actors health to 0 who are below 0`() {
        val scriptData = """
            logic myfoo {
                actors::state(health).minus(200);
                actors[state.health < 0]::state(health).set(0);
            }
        """.trimIndent()

        val one = ActorFactory.testActor()
        one.state.health = 100.0
        val two = ActorFactory.testActor()
        two.state.health = 300.0
        val container = Container(mutableListOf(one, two))

        val actual: ContainerScriptContext = containerScriptGrammar.parseToEnd(scriptData)
        val transpiler = ContainerScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(container)

        assertThat(one.state.health).isEqualTo(0.0)
        assertThat(two.state.health).isEqualTo(100.0)
    }

    @Test
    fun `should decrease actors health down three times by 10`() {
        val scriptData = """
            logic myfoo {
                actors::state(health).minus(10);
                actors::state(health).minus(10);
                actors::state(health).minus(10);
            }
        """.trimIndent()

        val one = ActorFactory.testActor()
        one.state.health = 100.0
        val container = Container(mutableListOf(one))

        val actual: ContainerScriptContext = containerScriptGrammar.parseToEnd(scriptData)
        val transpiler = ContainerScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(container)

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

        val one = ActorFactory.testActor()
        val container = Container(mutableListOf(one))

        val actual: ContainerScriptContext = containerScriptGrammar.parseToEnd(scriptData)
        val transpiler = ContainerScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(container)

        assertThat(one.state.health).isEqualTo(100.0)
    }

    @Test
    fun `should set eat urge of actors where health is equal 30 to 20`() {
        val scriptData = """
            logic myfoo {
                actors[state.health = 50]::urge(foo).set(20);
            }
        """.trimIndent()

        val one = ActorFactory.testActor()
        one.state.health = 30.0
        val two = ActorFactory.testActor()
        two.state.health = 50.0
        val container = Container(mutableListOf(one, two))

        val actual: ContainerScriptContext = containerScriptGrammar.parseToEnd(scriptData)
        val transpiler = ContainerScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(container)

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

        val one = ActorFactory.testActor()
        one.state.health = 30.0
        val two = ActorFactory.testActor()
        val container = Container(mutableListOf(one, two))

        val actual: ContainerScriptContext = containerScriptGrammar.parseToEnd(scriptData)
        val transpiler = ContainerScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(container)

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

        val one = ActorFactory.testActor()
        val two = ActorFactory.testActor()
        val container = Container(mutableListOf(one, two))

        val actual: ContainerScriptContext = containerScriptGrammar.parseToEnd(scriptData)
        val transpiler = ContainerScriptTranspiler()
        val scriptedLogic = transpiler.transpile(actual)
        scriptedLogic.process(container)

        assertThat(one.urges.getUrgeOrNull("foo")).isEqualTo(3.0)
        assertThat(one.urges.getUrgeOrNull("bar")).isEqualTo(0.5)
        assertThat(two.urges.getUrgeOrNull("foo")).isEqualTo(3.0)
        assertThat(two.urges.getUrgeOrNull("bar")).isEqualTo(0.5)
    }
}
