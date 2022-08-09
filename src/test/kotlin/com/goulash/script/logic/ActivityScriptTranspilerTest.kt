package com.goulash.script.logic

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.goulash.factory.ActorFactory
import com.goulash.script.domain.ActivityScript
import com.goulash.script.domain.ScriptStatement
import org.junit.jupiter.api.Test

internal class ActivityScriptTranspilerTest {
    private val activityScriptTranspiler = ActivityScriptTranspiler()

    @Test
    fun `should transpile init logic for activities`() {
        val activityScriptContext = ActivityScriptContext(
            "eating",
            mapOf(
                "init" to listOf(
                    ScriptStatement(
                        "actor",
                        "",
                        "state",
                        "health",
                        "set",
                        "50"
                    )
                )
            ),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript = activityScriptTranspiler.transpile(activityScriptContext)

        val testActor = ActorFactory.testActor()
        val returnVal = activityScript.init(testActor)

        assertThat(testActor.state["health"]!!).isEqualTo(50.0)
    }

    @Test
    fun `should process condition type`() {
        val activityScriptContext = ActivityScriptContext(
            "eating",
            mapOf(
                "act" to listOf(
                    ScriptStatement(
                        "actor",
                        "",
                        "condition",
                        "sick",
                        "add",
                        ""
                    )
                )
            ),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript = activityScriptTranspiler.transpile(activityScriptContext)

        val testActor = ActorFactory.testActor()
        val returnVal = activityScript.act(testActor)

        assertThat(testActor.conditions).contains("sick")
        assertThat(returnVal).isTrue()
    }

    @Test
    fun `should set all given configurations properly`() {
        val activityScriptContext = ActivityScriptContext(
            "eating",
            emptyMap(),
            mapOf("trigger_urges" to listOf("eat"))
        )

        val activityScript: ActivityScript = activityScriptTranspiler.transpile(activityScriptContext)

        assertThat(activityScript.triggerUrges()).containsOnly("eat")
    }

    @Test
    fun `should transpile health increase by 10 for act logic and return true`() {
        val activityScriptContext = ActivityScriptContext(
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

        val activityScript = activityScriptTranspiler.transpile(activityScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0

        val returnVal = activityScript.act(testActor)

        assertThat(testActor.state["health"]!!).isEqualTo(60.0)
        assertThat(returnVal).isTrue()
    }

    @Test
    fun `should transpile health increase by 10 for on abort logic with filter`() {
        val activityScriptContext = ActivityScriptContext(
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

        val activityScript = activityScriptTranspiler.transpile(activityScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0

        activityScript.onAbort(testActor)

        assertThat(testActor.state["health"]!!).isEqualTo(50.0)
    }

    @Test
    fun `should transpile health incrase by 10 for on abort logic`() {
        val activityScriptContext = ActivityScriptContext(
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

        val activityScript = activityScriptTranspiler.transpile(activityScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0

        activityScript.onAbort(testActor)

        assertThat(testActor.state["health"]!!).isEqualTo(60.0)
    }

    @Test
    fun `should transpile health incrase by 10 for on finish logic`() {
        val activityScriptContext = ActivityScriptContext(
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

        val activityScript = activityScriptTranspiler.transpile(activityScriptContext)

        val testActor = ActorFactory.testActor()
        testActor.state["health"] = 50.0

        activityScript.onFinish(testActor)

        assertThat(testActor.state["health"]!!).isEqualTo(60.0)
    }
}
