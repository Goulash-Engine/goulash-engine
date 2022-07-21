package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class WorkBehaviorTest {
    private val workBehavior = WorkBehavior()

    @Test
    fun `should add the tired condition and remove ambitious condition if stamina is below 30`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.stamina = 31.0

        workBehavior.act(clan)

        assertThat(clan.stamina).isEqualTo(28.0)
        assertThat(clan.hunger).isEqualTo(0.5)
        assertThat(clan.conditions).contains(workBehavior.blocker())
        assertThat(clan.conditions).doesNotContain(workBehavior.trigger())
    }

    @Test
    fun `should decrease stamina and increase hunger and set current activity to working`() {
        val clan = ClanFactory.simpleGathererClan()

        workBehavior.act(clan)

        assertThat(clan.stamina).isEqualTo(97.0)
        assertThat(clan.hunger).isEqualTo(0.5)
        assertThat(clan.currentActivity).isEqualTo("working")
    }
}
