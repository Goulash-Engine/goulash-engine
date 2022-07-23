package com.barbarus.prosper.actor.activity

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.Test

internal class WorkActivityTest {
    private val workActivity = WorkActivity()

    @Test
    fun `should return 'working' activity`() {
        assertThat(workActivity.activity()).isEqualTo("working")
    }

    @Test
    fun `should increase urge to rest by 1_0`() {
        val clan = ClanFactory.poorGathererClan()

        workActivity.act(clan)

        assertThat(clan.urges.getUrges()["rest"]).isEqualTo(1.5)
    }
}
