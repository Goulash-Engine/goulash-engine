package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.factories.ClanFactory
import com.barbarus.prosper.factories.ResourceFactory
import org.junit.jupiter.api.Test

internal class InventoryLogicTest {
    private val inventoryLogic = InventoryLogic()

    @Test
    fun `should remove resource from inventory if weight is greater than 0_1`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.inventory().add(ResourceFactory.food().also { it.weight = -0.1 })
        val sizeBefore = clan.inventory().size

        inventoryLogic.process(clan)

        assertThat(clan.inventory()).hasSize(sizeBefore - 1)
    }
}
