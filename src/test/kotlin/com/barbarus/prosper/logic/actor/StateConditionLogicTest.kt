package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.actor.logic.StateConditionLogic
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class StateConditionLogicTest {
    private val stateConditionLogic = StateConditionLogic()

    @Test
    fun `should add the condition for tireness if health is under 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.health = 59.0

        stateConditionLogic.process(clan)

        assertThat(clan.conditions).contains("tired")
    }

}
