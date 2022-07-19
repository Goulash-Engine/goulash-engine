package com.barbarus.prosper.processor

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class DesireProcessorTest {
    private val desireProcessor = DesireProcessor()

    @Test
    fun `should add the desire for sleep if health is under 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.health = 59.0

        desireProcessor.process(clan)

        assertThat(clan.desires).contains("sleep")
    }

    @Test
    fun `should add the desire for sleep if stamina is under 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 25.0

        desireProcessor.process(clan)

        assertThat(clan.desires).contains("sleep")
    }

    @Test
    fun `should add the desire for work if stamina is over 60`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 61.0

        desireProcessor.process(clan)

        assertThat(clan.desires).contains("work")
    }

    @Test
    fun `should add the desire for hunger if hunger is over 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.hunger = 31.0

        desireProcessor.process(clan)

        assertThat(clan.desires).contains("hunger")
    }
}
