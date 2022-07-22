package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.contains
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class AmbitionBehaviorTest {
    private val ambitionBehavior = AmbitionBehavior()

    @Test
    fun `should make an actor ambitious if no food resource is in its inventory`() {
        val clan = ClanFactory.poorGathererClan()
        clan.stash.clear()

        ambitionBehavior.act(clan)

        assertThat(clan.conditions).contains("ambitious")
    }
}
