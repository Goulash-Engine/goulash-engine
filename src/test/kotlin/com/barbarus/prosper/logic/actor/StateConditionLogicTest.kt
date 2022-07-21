package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class StateConditionLogicTest {
    private val stateConditionLogic = StateConditionLogic()

    @Test
    fun `should add the condition for sleep if health is under 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.health = 59.0

        stateConditionLogic.process(clan)

        assertThat(clan.conditions).contains("tired")
    }

    @Test
    fun `should add the condition for sleep if stamina is under 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 25.0

        stateConditionLogic.process(clan)

        assertThat(clan.conditions).contains("tired")
    }

    @Test
    fun `should add the condition for work if stamina is over 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 61.0

        stateConditionLogic.process(clan)

        assertThat(clan.conditions).contains("ambitious")
    }

    @Test
    fun `should add the condition for hunger if hunger is over 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.hunger = 31.0

        stateConditionLogic.process(clan)

        assertThat(clan.conditions).contains("hungry")
    }
}
