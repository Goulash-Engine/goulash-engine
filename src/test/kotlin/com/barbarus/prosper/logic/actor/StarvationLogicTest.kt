package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class StarvationLogicTest {
    private val starvationLogic = StarvationLogic()

    @Test
    fun `should should reduce health stronger if hunger is over critical threshold`() {
        val clan = ClanFactory.simpleGathererClan()

        clan.state.hunger = 91.0

        starvationLogic.process(clan)

        assertThat(clan.health).isEqualTo(95.0)
    }

    @Test
    fun `should should reduce health if hunger is over normal threshold`() {
        val clan = ClanFactory.simpleGathererClan()

        clan.state.hunger = 81.0

        starvationLogic.process(clan)

        assertThat(clan.health).isEqualTo(99.0)
    }
}
