package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class WorkBehaviorTest {
    private val workBehavior = WorkBehavior()

    @Test
    fun `should decrease stamina and increase hunger and set current activity to working`() {
        val clan = ClanFactory.simpleGathererClan()

        workBehavior.act(clan)

        assertThat(clan.stamina).isEqualTo(99.0)
        assertThat(clan.hunger).isEqualTo(0.5)
        assertThat(clan.currentActivity).isEqualTo("working")
    }
}