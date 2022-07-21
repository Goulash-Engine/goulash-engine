package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class StarvationBehaviorTest {
    private val starvationBehavior = StarvationBehavior()

    @Test
    fun `should reduce health by 5`() {
        val clan = ClanFactory.poorGathererClan()
        clan.conditions.add("starving")
        clan.state.hunger = 91.0

        starvationBehavior.act(clan)

        assertThat(clan.state.health).isEqualTo(95.0)
        assertThat(clan.conditions).contains("dying")
    }

    @Test
    fun `should reduce health by 1`() {
        val clan = ClanFactory.poorGathererClan()
        clan.conditions.add("starving")
        clan.state.hunger = 81.0

        starvationBehavior.act(clan)

        assertThat(clan.state.health).isEqualTo(99.0)
    }

    @Test
    fun `should remove starvation condition if hunger is under 80`() {
        val clan = ClanFactory.poorGathererClan()
        clan.conditions.add("starving")

        starvationBehavior.act(clan)

        assertThat(clan.conditions).doesNotContain("starving")
    }
}
