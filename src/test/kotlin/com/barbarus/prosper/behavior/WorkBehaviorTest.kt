package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class WorkBehaviorTest {
    private val workBehavior = WorkBehavior()

    @Test
    fun `should decrease stamina`() {
        val clan = ClanFactory.simpleGathererClan()

        workBehavior.act(clan)

        assertThat(clan.stamina).isEqualTo(99.0)
    }
}
