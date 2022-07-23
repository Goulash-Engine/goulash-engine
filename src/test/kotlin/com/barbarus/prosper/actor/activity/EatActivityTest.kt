package com.barbarus.prosper.actor.activity

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.factories.ResourceFactory
import org.junit.jupiter.api.Test

internal class EatActivityTest {
    private val eatActivity = EatActivity()

    @Test
    fun `should decrease eat urge when eating `() {
        val clan = ClanFactory.poorGathererClan()
        clan.stash.clear()
        clan.stash.add(ResourceFactory.food())
        clan.urges.increaseUrge("eat", 3.0)

        eatActivity.act(clan)

        assertThat(clan.urges.getUrges()["eat"]).isEqualTo(2.0)
    }

    @Test
    fun `should increase rest urge when eating `() {
        val clan = ClanFactory.poorGathererClan()
        clan.stash.clear()
        clan.stash.add(ResourceFactory.food())

        eatActivity.act(clan)

        assertThat(clan.urges.getUrges()["rest"]).isEqualTo(0.3)
    }

    @Test
    fun `should decrease food by 0_1 per act`() {
        val clan = ClanFactory.poorGathererClan()
        clan.stash.clear()
        clan.stash.add(ResourceFactory.food())

        eatActivity.act(clan)

        assertThat(clan.stash.first().weight).isEqualTo(0.9)
    }
}
