package com.barbarus.prosper.script.logic

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.script.domain.ScriptStatement
import org.junit.jupiter.api.Test

internal class ScriptExecuterTest {
    private val scriptExecuter = ScriptExecuter()

    @Test
    fun `should set eat urge for all actors to 50`() {
        val one = ClanFactory.testClan()
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))
        val statements = listOf(
            ScriptStatement(
                "actors",
                "urge",
                "eat",
                "set",
                "50"
            )
        )

        scriptExecuter.execute(civilisation, statements)

        assertAll {
            civilisation.actors.forEach {
                assertThat(it.urges.getUrges()["eat"]).isEqualTo(50.0)
            }
        }
    }

    @Test
    fun `should decrease eat urge for all actors by 1_0`() {
        val one = ClanFactory.testClan()
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))
        one.urges.increaseUrge("eat", 10.0)
        two.urges.increaseUrge("eat", 5.0)
        val statements = listOf(
            ScriptStatement(
                "actors",
                "urge",
                "eat",
                "minus",
                "1"
            )
        )

        scriptExecuter.execute(civilisation, statements)

        assertThat(one.urges.getUrges()["eat"]).isEqualTo(9.0)
        assertThat(two.urges.getUrges()["eat"]).isEqualTo(4.0)
    }

    @Test
    fun `should increase eat urge for all actors by 1_0`() {
        val one = ClanFactory.testClan()
        val two = ClanFactory.testClan()
        val civilisation = Civilisation(mutableListOf(one, two))
        val statements = listOf(
            ScriptStatement(
                "actors",
                "urge",
                "eat",
                "plus",
                "1"
            )
        )

        scriptExecuter.execute(civilisation, statements)

        assertAll {
            civilisation.actors.forEach {
                assertThat(it.urges.getUrges()["eat"]).isEqualTo(1.0)
            }
        }
    }
}
