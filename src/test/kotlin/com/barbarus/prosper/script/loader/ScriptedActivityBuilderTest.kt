package com.barbarus.prosper.script.loader

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ActorFactory
import com.barbarus.prosper.script.logic.ActivityScriptContext
import org.junit.jupiter.api.Test

internal class ScriptedActivityBuilderTest {
    private val scriptedActivityBuilder = ScriptedActivityBuilder()

    @Test
    fun `should parse activity script context to correct scripted script with working functions`() {
        val activityScriptContext = ActivityScriptContext(
            activity = "eating",
            mapOf(
                "act" to "actor::urges(eat).plus(1);actor::urges(eat).minus(1);",
                "on_finish" to "actor::urges(eat).minus(10);",
                "on_abort" to "actor::urges(eat).minus(10);actor::urges(eat).minus(10);actor::urges(eat).minus(10);"
            ),
            mapOf(
                "trigger_urges" to listOf("eat", "brot"),
                "abort_conditions" to listOf("foo", "bar"),
                "priority" to listOf("1"),
                "duration" to listOf("40.5"),
                "priority_conditions" to listOf("foo45")
            )
        )

        val scriptedActivity = scriptedActivityBuilder.parse(activityScriptContext)

        assertThat(scriptedActivity.activity()).isEqualTo("eating")
        assertThat(scriptedActivity.triggerUrges()).containsAll("eat", "brot")
        assertThat(scriptedActivity.abortConditions()).containsAll("foo", "bar")
        assertThat(scriptedActivity.priority()).isEqualTo(1)
        assertThat(scriptedActivity.duration().asDouble()).isEqualTo(40.5)
        assertThat(scriptedActivity.priorityConditions()).contains("foo45")

        val testActor = ActorFactory.testActor()
        scriptedActivity.act(testActor)
        assertThat(testActor.urges.getUrgeOrNull("eat")).isEqualTo(1)
    }
}
