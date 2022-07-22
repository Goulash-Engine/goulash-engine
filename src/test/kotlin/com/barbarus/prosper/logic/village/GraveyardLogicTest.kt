package com.barbarus.prosper.logic.village

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.core.domain.Village
import com.barbarus.prosper.village.logic.GraveyardLogic
import org.junit.jupiter.api.Test

internal class GraveyardLogicTest {
    private val graveyardLogic = GraveyardLogic()

    @Test
    fun `should remove clan from village if has condition dead`() {
        val clans = mutableListOf(
            ClanFactory.simpleGathererClan(),
            ClanFactory.simpleGathererClan().also { it.conditions.add("dead") },
            ClanFactory.simpleGathererClan()
        )
        val village = Village(clans)

        graveyardLogic.process(village)

        assertThat(village.clans.size).isEqualTo(2)
    }
}
