package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class BehaviorLogicTest {
    private val behaviorLogic = BehaviorLogic()

    @Test
    fun `should not consume a consumable resource if the actor is sick but should still run awake behavior`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.conditions.add("sick")
        clan.conditions.add("hungry")

        behaviorLogic.process(clan)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(1.0)
        assertThat(clan.state.hunger).isEqualTo(0.1)
    }

    @Test
    fun `should consume a consumable resource if the actor has the hunger desire`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.conditions.add("hungry")

        val expectedWeight = clan.stash.minBy { it.weight }.weight - 0.1

        behaviorLogic.process(clan)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(expectedWeight)
    }
}
