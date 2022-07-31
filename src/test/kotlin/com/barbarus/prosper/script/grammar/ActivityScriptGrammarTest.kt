package com.barbarus.prosper.script.grammar

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Test

/**
 * Tests the grammatical parsing ability for given script data.
 */
internal class ActivityScriptGrammarTest {
    private val activityGrammar = ActivityScriptGrammar()

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
                blocker { sick, dead }
                trigger { eat, enjoy }
                priority { starving, crazy }
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
                priority { starving, crazy }
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
                blocker { sick, dead }
                trigger { eat, enjoy }
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
                trigger { eat, enjoy }
                blocker { sick, dead }
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
                blocker { sick, dead }
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
                trigger { eat, enjoy }
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
                trigger { eat }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.triggerUrges).containsAll("eat")
    }
}
