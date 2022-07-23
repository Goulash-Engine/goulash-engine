package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.actor.logic.DeathLogic
import org.junit.jupiter.api.Test

internal class DeathLogicTest {
    private val deathLogic = DeathLogic()

    @Test
    fun `should give dead condition if health is below 0`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.health = -0.1

        deathLogic.process(clan)

        assertThat(clan.conditions).contains("dead")
    }
}
