package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.ClanFactory
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType
import org.junit.jupiter.api.Test

internal class ConsumptionBehaviorTest {
    private val consumptionBehavior = ConsumptionBehavior()

    @Test
    fun `should decrease hunger if consumed successfully`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.state.hunger = 30.0

        consumptionBehavior.act(clan)

        assertThat(clan.stash.minBy { it.weight }.weight).isEqualTo(0.9)
        assertThat(clan.hunger).isEqualTo(25.0)
        assertThat(clan.currentActivity).isEqualTo("consuming")
    }
    @Test
    fun `should only trigger if actor is in need of consumption`() {
        val clan = Clan(
            primaryProfession = Profession(),
            stash = mutableListOf(
                Resource(
                    "food",
                    ResourceType.FOOD,
                    traits = mutableListOf("consumable"),
                    weight = 1.0
                )
            )
        )

        consumptionBehavior.act(clan)

        assertThat(clan.stash[0].weight).isEqualTo(0.9)
        assertThat(clan.currentActivity).isEqualTo("consuming")
    }
    @Test
    fun `should reduce the amount of a consumable by 0_1`() {
        val clan = Clan(
            primaryProfession = Profession(),
            stash = mutableListOf(
                Resource(
                    "food",
                    ResourceType.FOOD,
                    traits = mutableListOf("consumable"),
                    weight = 1.0
                )
            )
        )

        consumptionBehavior.act(clan)

        assertThat(clan.stash[0].weight).isEqualTo(0.9)
        assertThat(clan.currentActivity).isEqualTo("consuming")
    }
}
