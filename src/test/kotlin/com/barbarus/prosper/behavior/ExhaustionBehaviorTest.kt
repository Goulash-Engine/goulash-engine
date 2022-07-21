package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class ExhaustionBehaviorTest {
    private val exhaustionBehavior = ExhaustionBehavior()

    @Test
    fun `should remove rested condition if stamina is lesser than 90`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 80.0
        clan.conditions.add("rested")

        exhaustionBehavior.act(clan)

        assertThat(clan.conditions).doesNotContain("rested")
    }

    @Test
    fun `should set condition to tired if stamina is lesser than 30`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 29.0

        exhaustionBehavior.act(clan)

        assertThat(clan.conditions).contains("tired")
    }

    @Test
    fun `should set condition to exhausted if stamina is lesser than 10`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 9.0

        exhaustionBehavior.act(clan)

        assertThat(clan.conditions).contains("exhausted")
    }
}
