package com.barbarus.prosper.script.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.barbarus.prosper.factories.ActorFactory
import com.barbarus.prosper.script.domain.ScriptStatement
import org.junit.jupiter.api.Test

internal class ActivityScriptTranspilerTest {
    private val activityScriptTranspiler = ActivityScriptTranspiler()

    @Test
    fun `should transpile health increase by 10 for act logic and return true`() {
        val containerScriptContext = ActivityScriptContext(
            "eating",
            mapOf(
                "act" to listOf(
                    ScriptStatement(
                        "actor",
                        "",
                        "state",
                        "health",
                        "plus",
                        "10"
                    )
                )
            ),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript = activityScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state.health = 50.0

        val returnVal = activityScript.act(testActor)

        assertThat(testActor.state.health).isEqualTo(60.0)
        assertThat(returnVal).isTrue()
    }

    @Test
    fun `should transpile health increase by 10 for on abort logic with filter`() {
        val containerScriptContext = ActivityScriptContext(
            "eating",
            mapOf(
                "on_abort" to listOf(
                    ScriptStatement(
                        "actor",
                        "state.health > 60",
                        "state",
                        "health",
                        "plus",
                        "10"
                    )
                )
            ),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript = activityScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state.health = 50.0

        activityScript.onAbort(testActor)

        assertThat(testActor.state.health).isEqualTo(50.0)
    }

    @Test
    fun `should transpile health incrase by 10 for on abort logic`() {
        val containerScriptContext = ActivityScriptContext(
            "eating",
            mapOf(
                "on_abort" to listOf(
                    ScriptStatement(
                        "actor",
                        "",
                        "state",
                        "health",
                        "plus",
                        "10"
                    )
                )
            ),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript = activityScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state.health = 50.0

        activityScript.onAbort(testActor)

        assertThat(testActor.state.health).isEqualTo(60.0)
    }

    @Test
    fun `should transpile health incrase by 10 for on finish logic`() {
        val containerScriptContext = ActivityScriptContext(
            "eating",
            mapOf(
                "on_finish" to listOf(
                    ScriptStatement(
                        "actor",
                        "",
                        "state",
                        "health",
                        "plus",
                        "10"
                    )
                )
            ),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript = activityScriptTranspiler.transpile(containerScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state.health = 50.0

        activityScript.onFinish(testActor)

        assertThat(testActor.state.health).isEqualTo(60.0)
    }
}
