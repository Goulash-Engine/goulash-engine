package com.barbarus.prosper.village.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Civilisation
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class GraveyardCivilisationLogicTest {
    private val graveyardCivilisationLogic = GraveyardCivilisationLogic()

    @Test
    fun `should remove clan from village if has condition dead`() {
        val clans = mutableListOf(
            ClanFactory.simpleGathererClan(),
            ClanFactory.simpleGathererClan().also { it.conditions.add("dead") },
            ClanFactory.simpleGathererClan()
        )
        val civilisation = Civilisation(clans)

        graveyardCivilisationLogic.process(civilisation)

        assertThat(civilisation.clans.size).isEqualTo(2)
    }
}
