package com.barbarus.prosper.script.grammar

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Playground for the `better-parse` API
 */
internal class GrammarPlayground {
    private val playground = PlaygroundGrammar()

    @Disabled
    @Test
    fun `should parse logic for on finished`() {
        val scriptData = """
            activity eating {
                 trigger_urges [ "eat", "brot" ]
                 abort_conditions [ "foo", "bar" ]
                 logic act {
                     actor::urges(eat).plus(1);
                     actor::urges(eat).minus(1);
                 } 
                 priority [ "1" ]
                 duration [ "40.5" ]
                 priority_conditions ["foo45"]
                 logic on_finish {
                     actor::urges(eat).minus(10);
                 } 
                 logic on_abort {
                     actor::urges(eat).minus(10);
                     actor::urges(eat).minus(10);
                     actor::urges(eat).minus(10);
                 } 
             }
        """.trimIndent()

        // val activityScriptContext = playground.parseToEnd(scriptData)

        // assertThat(activityScriptContext.activity).isEqualTo("eating")
        // assertThat(activityScriptContext.triggerUrges).containsAll("eat", "brot")
        // assertThat(activityScriptContext.abortConditions).containsAll("foo", "bar")
        // assertThat(activityScriptContext.priority).isEqualTo(1)
        // assertThat(activityScriptContext.duration.asDouble()).isEqualTo(40.5)
        // assertThat(activityScriptContext.priorityConditions).contains("foo45")
        // assertThat(activityScriptContext.actLogic).isEqualTo("actor::urges(eat).plus(1);actor::urges(eat).minus(1);")
        // assertThat(activityScriptContext.onFinish).isEqualTo("actor::urges(eat).minus(10);")
        // assertThat(activityScriptContext.onAbort).isEqualTo("actor::urges(eat).minus(10);actor::urges(eat).minus(10);actor::urges(eat).minus(10);")
    }
}
