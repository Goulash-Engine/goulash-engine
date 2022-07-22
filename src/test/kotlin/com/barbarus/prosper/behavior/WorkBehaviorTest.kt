package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class WorkBehaviorTest {
    private val workBehavior = WorkBehavior()

    @Test
    fun `should increase urge to rest by 1_0`() {
        val clan = ClanFactory.poorGathererClan()

        workBehavior.act(clan)

        assertThat(clan.urges.getUrges()["rest"]).isEqualTo(1.0)
    }

    @Test
    fun `should increase hunger and set current activity to working`() {
        val clan = ClanFactory.simpleGathererClan()

        workBehavior.act(clan)

        assertThat(clan.state.hunger).isEqualTo(0.5)
        assertThat(clan.currentActivity).isEqualTo("working")
    }
}
