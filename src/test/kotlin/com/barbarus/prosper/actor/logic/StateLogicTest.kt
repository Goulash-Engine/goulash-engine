package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.isLessThan
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class StateLogicTest {

    private val stateLogic = StateLogic()

    @Test
    fun `should decrease health if malnourished`() {
        val clan = ClanFactory.poorGathererClan()
        clan.conditions.add("malnourished")

        stateLogic.process(clan)

        assertThat(clan.state.health).isLessThan(100.0)
    }
}
