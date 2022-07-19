package com.barbarus.prosper.behavior

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType
import org.junit.jupiter.api.Test

internal class ConsumptionBehaviorTest {
    private val consumptionBehavior = ConsumptionBehavior()

    @Test
    fun `should reduce the amount of a consumable by 0_1`() {
        val clan = Clan(
            primaryProfession = Profession(),
            stash = mutableListOf(
                Resource(
                    "food",
                    ResourceType.FOOD,
                    traits = mutableListOf("consumable"),
                    quantity = 1.0
                )
            )
        )

        consumptionBehavior.act(clan)

        assertThat(clan.stash[0].quantity).isEqualTo(0.9)
    }
}
