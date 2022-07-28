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
