package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.isBetween
import com.barbarus.prosper.core.domain.ResourceType
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.RepeatedTest

internal class ClanFactoryTest {

    @RepeatedTest(5)
    fun `should create a poor clan with 1 to 3 food`() {
        val poorGathererClan = ClanFactory.poorGathererClan()
        assertThat(poorGathererClan.stash.filter { it.type == ResourceType.FOOD }.size).isBetween(1, 3)
    }
}
