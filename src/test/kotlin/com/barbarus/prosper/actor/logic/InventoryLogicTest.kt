package com.barbarus.prosper.actor.logic

import assertk.assertThat
import assertk.assertions.hasSize
import com.barbarus.prosper.factory.ActorFactory
import com.barbarus.prosper.factory.ResourceFactory
import org.junit.jupiter.api.Test

internal class InventoryLogicTest {
    private val inventoryLogic = InventoryLogic()

    @Test
    fun `should remove resource from inventory if weight is greater than 0_1`() {
        val actor = ActorFactory.simpleGathererActor()
        actor.inventory().add(ResourceFactory.food().also { it.weight = -0.1 })
        val sizeBefore = actor.inventory().size

        inventoryLogic.process(actor)

        assertThat(actor.inventory()).hasSize(sizeBefore - 1)
    }
}
