package com.barbarus.prosper.logic.actor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import org.junit.jupiter.api.Test

internal class UrgeLogicTest {
    private val urgeLogic = UrgeLogic()

    @Test
    fun `should increase urge to rest if stamina is low`() {
        val clan = ClanFactory.poorGathererClan()
        clan.state.stamina = 59.0

        urgeLogic.process(clan)
        urgeLogic.process(clan)
        urgeLogic.process(clan)

        assertThat(clan.urges["rest"]).isEqualTo(3.0)
    }
}
