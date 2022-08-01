package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Tests the grammatical parsing ability for given script data.
 */
internal class ActivityScriptGrammarTest {
    private val activityGrammar = ActivityScriptGrammar()

    @Disabled
    @Test
    fun `should parse logic for on finished`() {
        val scriptData = """
            activity eating {
                logic act { hello }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.actLogic).isEqualTo("logic act { }".trim())
    }

    @Test
    fun `should parse priority and duration value (reverse)`() {
        val scriptData = """
            activity eating {
                priority { 30 }
                duration { 10 }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.duration.asDouble()).isEqualTo(10.0)
        assertThat(scriptContext.priority).isEqualTo(30)
    }

    @Test
    fun `should parse priority and duration value`() {
        val scriptData = """
            activity eating {
                duration { 10 }
                priority { 30 }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.duration.asDouble()).isEqualTo(10.0)
        assertThat(scriptContext.priority).isEqualTo(30)
    }

    @Test
    fun `should parse priority value`() {
        val scriptData = """
            activity eating {
                priority { 30 }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.priority).isEqualTo(30)
    }

    @Test
    fun `should parse abort conditions`() {
        val scriptData = """
            activity eating {
                abort_conditions { sick, dead }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.abortConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse all list options incl duration & priority in any order`() {
        val scriptData = """
            activity eating {
                duration { 40.0 }
                blocker_conditions { sick, dead }
                trigger_urges { eat, enjoy }
                abort_conditions { stuff }
                priority { 3 }
                priority_conditions { starving, crazy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.priorityConditions).containsAll("starving", "crazy")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
        assertThat(scriptContext.abortConditions).containsAll("stuff")
        assertThat(scriptContext.duration.asDouble()).isEqualTo(40.0)
        assertThat(scriptContext.priority).isEqualTo(3)
    }

    @Test
    fun `should parse duration for activity`() {
        val scriptData = """
            activity eating {
                duration { 40.0 }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.duration.asDouble()).isEqualTo(40.0)
    }

    @Test
    fun `should parse all list options`() {
        val scriptData = """
            activity eating {
                blocker_conditions { sick, dead }
                trigger_urges { eat, enjoy }
                priority_conditions { starving, crazy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.priorityConditions).containsAll("starving", "crazy")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse priority conditions`() {
        val scriptData = """
            activity eating {
                priority_conditions { starving, crazy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.priorityConditions).containsAll("starving", "crazy")
    }

    @Test
    fun `should parse activity with triggers and blockers declared (reversed)`() {
        val scriptData = """
            activity eating {
                blocker_conditions { sick, dead }
                trigger_urges { eat, enjoy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse activity with triggers and blockers declared`() {
        val scriptData = """
            activity eating {
                trigger_urges { eat, enjoy }
                blocker_conditions { sick, dead }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse simple activity script with name and blocker conditions`() {
        val scriptData = """
            activity eating {
                blocker_conditions { sick, dead }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse simple activity script with name and trigger`() {
        val scriptData = """
            activity eating {
                trigger_urges { eat, enjoy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
    }

    @Test
    fun `should parse simple activity script with name and one trigger`() {
        val scriptData = """
            activity eating {
                trigger_urges { eat }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.triggerUrges).containsAll("eat")
    }
}
