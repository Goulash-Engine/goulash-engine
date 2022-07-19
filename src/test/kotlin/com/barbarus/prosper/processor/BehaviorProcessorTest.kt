package com.barbarus.prosper.processor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class BehaviorProcessorTest {
    private val behaviorProcessor = BehaviorProcessor()

    @Test
    fun `should not consume a consumable resource if the actor is sick`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.addCondition("sick")
        clan.addCondition("hunger")

        behaviorProcessor.process(clan, clan.behaviors)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(1.0)
    }

    @Test
    fun `should consume a consumable resource if the actor has the hunger desire`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.addCondition("hunger")

        val expectedWeight = clan.stash.minBy { it.weight }.weight - 0.1

        behaviorProcessor.process(clan, clan.behaviors)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(expectedWeight)
    }
}
