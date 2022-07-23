package com.barbarus.prosper.village.logic

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.core.domain.Village
import org.junit.jupiter.api.Test

internal class GraveyardVillageLogicTest {
    private val graveyardVillageLogic = GraveyardVillageLogic()

    @Test
    fun `should remove clan from village if has condition dead`() {
        val clans = mutableListOf(
            ClanFactory.simpleGathererClan(),
            ClanFactory.simpleGathererClan().also { it.conditions.add("dead") },
            ClanFactory.simpleGathererClan()
        )
        val village = Village(clans)

        graveyardVillageLogic.process(village)

        assertThat(village.clans.size).isEqualTo(2)
    }
}
