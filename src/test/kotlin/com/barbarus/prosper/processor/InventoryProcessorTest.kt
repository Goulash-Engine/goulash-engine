package com.barbarus.prosper.processor

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.ClanFactory
import com.barbarus.prosper.ResourceFactory
import org.junit.jupiter.api.Test

internal class InventoryProcessorTest {
    private val inventoryProcessor = InventoryProcessor()

    @Test
    fun `should remove resource from inventory if weight is greater than 0_1`() {
        val clan = ClanFactory.simpleGathererClan()
        clan.inventory().add(ResourceFactory.food().also { it.weight = -0.1 })
        val sizeBefore = clan.inventory().size

        inventoryProcessor.process(clan)

        assertThat(clan.inventory()).hasSize(sizeBefore - 1)
    }
}
