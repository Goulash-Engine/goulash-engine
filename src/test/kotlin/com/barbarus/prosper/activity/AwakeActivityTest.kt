package com.barbarus.prosper.activity

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class AwakeActivityTest {
    private val awakeBehavior = AwakeActivity()

    @Test
    fun `should increase hunger`() {
        val clan = ClanFactory.simpleGathererClan()

        awakeBehavior.act(clan)

        assertThat(clan.state.hunger).isEqualTo(0.1)
    }
}
