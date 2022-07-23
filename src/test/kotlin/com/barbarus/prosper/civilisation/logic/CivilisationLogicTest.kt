package com.barbarus.prosper.civilisation.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class CivilisationLogicTest {
    private val civilisationLogic = CivilisationLogic()

    @Test
    fun `should increase the urge to eat`() {
        val clans: MutableList<Actor> = mutableListOf(ClanFactory.simpleGathererClan())
        val civilisation = Civilisation(clans)

        civilisationLogic.process(civilisation)

        assertThat(civilisation.actors.first().urges.getUrges()["eat"]!!).isGreaterThan(0.0)
    }

    @Test
    fun `should remove clan from village if has condition dead`() {
        val clans: MutableList<Actor> = mutableListOf(
            ClanFactory.simpleGathererClan(),
            ClanFactory.simpleGathererClan().also { it.conditions.add("dead") },
            ClanFactory.simpleGathererClan()
        )
        val civilisation = Civilisation(clans)

        civilisationLogic.process(civilisation)

        assertThat(civilisation.actors.size).isEqualTo(2)
    }
}
