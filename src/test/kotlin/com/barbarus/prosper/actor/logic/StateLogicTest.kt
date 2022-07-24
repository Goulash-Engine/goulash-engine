package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.isLessThan
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class StateLogicTest {

    private val stateLogic = StateLogic()

    @Test
    fun `should decrease health if bad nourished`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.nourishment = 50.0

        stateLogic.process(clan)

        assertThat(clan.state.health).isLessThan(100.0)
    }
    @Test
    fun `should decrease nourishment if underfed`() {
        val clan = ClanFactory.poorGathererClan()
        clan.conditions.add("underfed")

        stateLogic.process(clan)

        assertThat(clan.state.nourishment).isLessThan(100.0)
    }
}
