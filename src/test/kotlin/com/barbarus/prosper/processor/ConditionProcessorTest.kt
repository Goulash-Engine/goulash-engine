package com.barbarus.prosper.processor

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.ClanFactory
import com.barbarus.prosper.processor.actor.ConditionProcessor
import org.junit.jupiter.api.Test

internal class ConditionProcessorTest {
    private val conditionProcessor = ConditionProcessor()

    @Test
    fun `should add the desire for sleep if health is under 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.health = 59.0

        conditionProcessor.process(clan)

        assertThat(clan.conditions).contains("sleep")
    }

    @Test
    fun `should add the desire for sleep if stamina is under 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 25.0

        conditionProcessor.process(clan)

        assertThat(clan.conditions).contains("sleep")
    }

    @Test
    fun `should add the desire for work if stamina is over 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 61.0

        conditionProcessor.process(clan)

        assertThat(clan.conditions).contains("work")
    }

    @Test
    fun `should add the desire for hunger if hunger is over 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.hunger = 31.0

        conditionProcessor.process(clan)

        assertThat(clan.conditions).contains("hunger")
    }
}
