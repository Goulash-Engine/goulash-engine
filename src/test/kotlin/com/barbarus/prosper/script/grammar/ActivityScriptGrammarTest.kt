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
    fun `should parse all list options`() {
        val scriptData = """
            activity eat {
                blocker { sick, dead }
                trigger { eat, enjoy }
                priority { starving, crazy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.priorityConditions).containsAll("starving", "crazy")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse priority conditions`() {
        val scriptData = """
            activity eat {
                priority { starving, crazy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.priorityConditions).containsAll("starving", "crazy")
    }

    @Test
    fun `should parse activity with triggers and blockers declared (reversed)`() {
        val scriptData = """
            activity eat {
                blocker { sick, dead }
                trigger { eat, enjoy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse activity with triggers and blockers declared`() {
        val scriptData = """
            activity eat {
                trigger { eat, enjoy }
                blocker { sick, dead }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse simple activity script with name and blocker conditions`() {
        val scriptData = """
            activity eat {
                blocker { sick, dead }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.blockerConditions).containsAll("sick", "dead")
    }

    @Test
    fun `should parse simple activity script with name and trigger`() {
        val scriptData = """
            activity eat {
                trigger { eat, enjoy }
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.head.name).isEqualTo("eat")
        assertThat(scriptContext.triggerUrges).containsAll("eat", "enjoy")
    }
}
