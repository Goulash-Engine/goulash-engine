package com.goulash.script.grammar

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.goulash.script.domain.ScriptStatement
import org.junit.jupiter.api.Test

/**
 * Tests the grammatical parsing ability for given script data.
 */
internal class ActivityScriptGrammarTest {
    private val activityGrammar = ActivityScriptGrammar()

    @Test
    fun `should parse requirements config`() {
        val scriptData = """
            activity eating {
                 requirements ["food"]
                 logic init {
                      actor::state(health).set(100);
                  }
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.configurations["requirements"]!!).contains("food")
    }

    @Test
    fun `should have parse logic init and other logic types`() {
        val scriptData = """
            activity eating {
                 logic init {
                      actor::state(health).set(100);
                      actor::state(health).set(100);
                  }
                 logic act {
                      actor::state(health).set(100);
                  }
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.statements["act"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "set",
                    "100"
                )
            )
        )
        assertThat(activityScriptContext.statements["init"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "set",
                    "100"
                ),
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "set",
                    "100"
                )
            )
        )
    }

    @Test
    fun `should have init logic working`() {
        val scriptData = """
            activity eating {
                 logic init {
                      actor::state(health).set(100);
                  }
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.statements["init"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "set",
                    "100"
                )
            )
        )
    }

    @Test
    fun `should parse act logic with two filter statements`() {
        val scriptData = """
            activity eating {
                 logic act {
                      actor[state.health>100]::state(health).minus(10);
                      actor[state.health<50]::state(health).plus(10);
                  }
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.statements["act"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "state.health>100",
                    "state",
                    "health",
                    "minus",
                    "10"
                ),
                ScriptStatement(
                    "actor",
                    "state.health<50",
                    "state",
                    "health",
                    "plus",
                    "10"
                )
            )
        )
    }

    @Test
    fun `should parse act logic with filter`() {
        val scriptData = """
            activity eating {
                 logic act { actor[state.health>100]::state(health).minus(10);} 
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.statements["act"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "state.health>100",
                    "state",
                    "health",
                    "minus",
                    "10"
                )
            )
        )
    }

    @Test
    fun `should logic components to statement scripts`() {
        val scriptData = """
            activity eating {
                 logic act { actor::state(health).minus(10)} 
                 logic on_finish { actor::state(health).minus(10)} 
                 logic on_abort { actor::state(health).minus(10)} 
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.statements["act"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "minus",
                    "10"
                )
            )
        )
        assertThat(activityScriptContext.statements["on_finish"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "minus",
                    "10"
                )
            )
        )
        assertThat(activityScriptContext.statements["on_abort"]).isEqualTo(
            listOf(
                ScriptStatement(
                    "actor",
                    "",
                    "state",
                    "health",
                    "minus",
                    "10"
                )
            )
        )
    }

    @Test
    fun `should parse complete activity script with diffused format`() {
        val scriptData = """
            activity eating {
                 trigger_urges ["eat","brot"]
                 abort_conditions [ "foo", "bar" ]
                 logic act { actor::urges(eat).plus(1); actor::urges(eat).minus(1); } 
                 priority [ "1" ]
                 duration [ "40.5" ]
                 priority_conditions ["foo45"]
                 logic on_finish {
                 
                 
                 
                 
                     actor::urges(eat).minus(10);
                     
                     
                     
                 } 
                 
                 
                 
                 logic on_abort {
                     actor::urges(eat).minus(10);
                     
                     
                     
                     actor::urges(eat).minus(10); actor::urges(eat).minus(10); } 
             }
        """.trimIndent()

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.activity).isEqualTo("eating")
        assertThat(activityScriptContext.configurations["trigger_urges"]!!).containsAll("eat", "brot")
        assertThat(activityScriptContext.configurations["abort_conditions"]!!).containsAll("foo", "bar")
        assertThat(activityScriptContext.configurations["priority"]!!).contains("1")
        assertThat(activityScriptContext.configurations["duration"]!!).contains("40.5")
        assertThat(activityScriptContext.configurations["priority_conditions"]!!).contains("foo45")
    }

    @Test
    fun `should parse complete activity script`() {
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

        val activityScriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(activityScriptContext.activity).isEqualTo("eating")
        assertThat(activityScriptContext.configurations["trigger_urges"]!!).containsAll("eat", "brot")
        assertThat(activityScriptContext.configurations["abort_conditions"]!!).containsAll("foo", "bar")
        assertThat(activityScriptContext.configurations["priority"]!!).contains("1")
        assertThat(activityScriptContext.configurations["duration"]!!).contains("40.5")
        assertThat(activityScriptContext.configurations["priority_conditions"]!!).contains("foo45")
    }

    @Test
    fun `should parse priority and duration value (reverse)`() {
        val scriptData = """
            activity eating {
                priority [ "30" ]
                duration [ "10" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.configurations["duration"]!!).contains("10")
        assertThat(scriptContext.configurations["priority"]!!).contains("30")
    }

    @Test
    fun `should parse priority and duration value`() {
        val scriptData = """
            activity eating {
                duration [ "10" ]
                priority [ "30" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.configurations["duration"]!!).contains("10")
        assertThat(scriptContext.configurations["priority"]!!).contains("30")
    }

    @Test
    fun `should parse priority value`() {
        val scriptData = """
            activity eating {
                priority [ "30" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.configurations["priority"]!!).contains("30")
    }

    @Test
    fun `should parse abort conditions`() {
        val scriptData = """
            activity eating {
                abort_conditions [ "sick", "dead" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.configurations["abort_conditions"]!!).containsAll("sick", "dead")
    }

    @Test
    fun `should parse all list options incl duration & priority in any order`() {
        val scriptData = """
            activity eating {
                duration [ "40.0" ]
                blocker_conditions [ "sick", "dead" ]
                trigger_urges [ "eat", "enjoy" ]
                abort_conditions [ "stuff" ]
                priority [ "3" ]
                priority_conditions [ "starving", "crazy" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["priority_conditions"]!!).containsAll("starving", "crazy")
        assertThat(scriptContext.configurations["trigger_urges"]!!).containsAll("eat", "enjoy")
        assertThat(scriptContext.configurations["abort_conditions"]!!).containsAll("stuff")
        assertThat(scriptContext.configurations["blocker_conditions"]!!).containsAll("sick", "dead")
        assertThat(scriptContext.configurations["priority"]!!).contains("3")
        assertThat(scriptContext.configurations["duration"]!!).contains("40.0")
    }

    @Test
    fun `should parse duration for activity`() {
        val scriptData = """
            activity eating {
                duration [ "40.0" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.configurations["duration"]!!).contains("40.0")
    }

    @Test
    fun `should parse all list options`() {
        val scriptData = """
            activity eating {
                blocker_conditions [ "sick", "dead" ]
                trigger_urges [ "eat", "enjoy" ]
                priority_conditions [ "starving", "crazy" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["priority_conditions"]!!).containsAll("starving", "crazy")
        assertThat(scriptContext.configurations["trigger_urges"]!!).containsAll("eat", "enjoy")
        assertThat(scriptContext.configurations["blocker_conditions"]!!).containsAll("sick", "dead")
    }

    @Test
    fun `should parse priority conditions`() {
        val scriptData = """
            activity eating {
                priority_conditions [ "starving", "crazy" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["priority_conditions"]!!).containsAll("starving", "crazy")
    }

    @Test
    fun `should parse activity with triggers and blockers declared (reversed)`() {
        val scriptData = """
            activity eating {
                blocker_conditions [ "sick", "dead" ]
                trigger_urges [ "eat", "enjoy" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["trigger_urges"]!!).containsAll("eat", "enjoy")
        assertThat(scriptContext.configurations["blocker_conditions"]!!).containsAll("sick", "dead")
    }

    @Test
    fun `should parse activity with triggers and blockers declared`() {
        val scriptData = """
            activity eating {
                trigger_urges [ "eat", "enjoy" ]
                blocker_conditions [ "sick", "dead" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["trigger_urges"]!!).containsAll("eat", "enjoy")
        assertThat(scriptContext.configurations["blocker_conditions"]!!).containsAll("sick", "dead")
    }

    @Test
    fun `should parse simple activity script with name and blocker conditions`() {
        val scriptData = """
            activity eating {
                blocker_conditions [ "sick", "dead" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["blocker_conditions"]!!).containsAll("sick", "dead")
    }

    @Test
    fun `should parse simple activity script with name and trigger`() {
        val scriptData = """
            activity eating {
                trigger_urges [ "eat", "enjoy" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["trigger_urges"]!!).containsAll("eat", "enjoy")
    }

    @Test
    fun `should parse simple activity script with name and one trigger`() {
        val scriptData = """
            activity eating {
                trigger_urges [ "eat" ]
            }
        """.trimIndent()

        val scriptContext = activityGrammar.parseToEnd(scriptData)

        assertThat(scriptContext.activity).isEqualTo("eating")
        assertThat(scriptContext.configurations["trigger_urges"]!!).contains("eat")
    }
}
