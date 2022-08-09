package com.goulash.script.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.goulash.core.domain.Container
import com.goulash.factory.ActorFactory
import com.goulash.script.domain.ScriptStatement
import org.junit.jupiter.api.Test

internal class ContainerScriptTranspilerTest {

    @Test
    fun `should transpile init logic of container script`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "init" to
                    listOf(
                        ScriptStatement(
                            "actors",
                            "",
                            "state",
                            "health",
                            "minus",
                            "10"
                        )
                    )
            )
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0
        val container = Container(actors = mutableListOf(testActor))

        scriptedLogic.init(container)

        assertThat(testActor.state["health"]!!).isEqualTo(40.0)
    }

    @Test
    fun `should transpile health decrease by 10`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "container" to
                    listOf(
                        ScriptStatement(
                            "actors",
                            "",
                            "state",
                            "health",
                            "minus",
                            "10"
                        )
                    )
            )
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0
        val container = Container(actors = mutableListOf(testActor))

        scriptedLogic.process(container)

        assertThat(testActor.state["health"]!!).isEqualTo(40.0)
    }

    @Test
    fun `should transpile health increase by 10`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "container" to listOf(
                    ScriptStatement(
                        "actors",
                        "",
                        "state",
                        "health",
                        "plus",
                        "10"
                    )
                )
            )
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0
        val container = Container(actors = mutableListOf(testActor))

        scriptedLogic.process(container)

        assertThat(testActor.state["health"]!!).isEqualTo(60.0)
    }

    @Test
    fun `should transpile health set to 50_0`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "container" to listOf(
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
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        val container = Container(actors = mutableListOf(testActor))

        scriptedLogic.process(container)

        assertThat(testActor.state["health"]!!).isEqualTo(50.0)
    }

    @Test
    fun `should transpile filter for greater than`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "container" to listOf(
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
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 61.0
        val container = Container(actors = mutableListOf(testActor))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(container)

        assertThat(testActor.urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }

    @Test
    fun `should transpile filter for less than`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "container" to listOf(
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
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 30.0
        val container = Container(actors = mutableListOf(testActor))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(container)

        assertThat(testActor.urges.getUrgeOrNull("eat")).isNull()
    }

    @Test
    fun `should transpile script context`() {
        val containerScriptTranspiler = ContainerScriptTranspiler()
        val containerScriptContext = ContainerScriptContext(
            ScriptHead("foo"),
            mapOf(
                "container" to listOf(
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
        )

        val scriptedLogic = containerScriptTranspiler.transpile(containerScriptContext)

        val testClan = ActorFactory.testActor()
        val container = Container(actors = mutableListOf(testClan))

        assertThat(scriptedLogic.name).isEqualTo("foo")

        scriptedLogic.process(container)

        assertThat(testClan.urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
