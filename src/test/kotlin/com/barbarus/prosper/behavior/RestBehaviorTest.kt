package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class RestBehaviorTest {
    private val restBehavior = RestBehavior()

    @Test
    fun `should remove exhausted condition if stamina is above above 10`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 11.0
        clan.conditions.add("exhausted")

        restBehavior.act(clan)

        assertThat(clan.conditions).doesNotContain("exhausted")
    }

    @Test
    fun `should remove tired condition if stamina is above above 75`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 76.0
        clan.conditions.add("tired")

        restBehavior.act(clan)

        assertThat(clan.conditions).doesNotContain("tired")
    }

    @Test
    fun `should make an actor rested if stamina is above 90`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 91.0

        restBehavior.act(clan)

        assertThat(clan.conditions).contains("rested")
    }
}
