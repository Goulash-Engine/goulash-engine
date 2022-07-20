package com.barbarus.prosper.processor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import com.barbarus.prosper.processor.actor.BehaviorProcessor
import org.junit.jupiter.api.Test

internal class BehaviorProcessorTest {
    private val behaviorProcessor = BehaviorProcessor()

    @Test
    fun `should not consume a consumable resource if the actor is sick but should still run awake behavior`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.conditions.add("sick")
        clan.conditions.add("hunger")

        behaviorProcessor.process(clan)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(1.0)
        assertThat(clan.hunger).isEqualTo(0.1)
    }

    @Test
    fun `should consume a consumable resource if the actor has the hunger desire`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.conditions.add("hunger")

        val expectedWeight = clan.stash.minBy { it.weight }.weight - 0.1

        behaviorProcessor.process(clan)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(expectedWeight)
    }
}
