package com.barbarus.prosper.logic

import assertk.assertThat
import assertk.assertions.isBetween
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.ResourceType
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class ResourceLogicTest {
    private val resourceLogic = ResourceLogic()

    @RepeatedTest(10)
    fun `should generate more wood if the woodworker experience is higher`() {
        var profession = Profession(ProfessionType.WOODWORKER, 1.0)
        var resource = resourceLogic.process(profession)

        assertThat(resource.type).isEqualTo(ResourceType.WOODEN_MATERIAL)
        assertThat(resource.weight).isBetween(0.1 * (profession.experience / 2), 0.5 * (profession.experience / 2))

        profession = Profession(ProfessionType.WOODWORKER, 2.0)
        resource = resourceLogic.process(profession)

        assertThat(resource.type).isEqualTo(ResourceType.WOODEN_MATERIAL)
        assertThat(resource.weight).isBetween(0.1 * (profession.experience / 2), 0.5 * (profession.experience / 2))
    }

    @Test
    fun `should generate more food if the gatherer experience is higher`() {
        var profession = Profession(ProfessionType.GATHERER, 1.0)
        var resource = resourceLogic.process(profession)

        assertThat(resource.type).isEqualTo(ResourceType.FOOD)
        assertThat(resource.weight).isBetween(0.1 * profession.experience, 0.5 * profession.experience)

        profession = Profession(ProfessionType.GATHERER, 2.0)
        resource = resourceLogic.process(profession)

        assertThat(resource.type).isEqualTo(ResourceType.FOOD)
        assertThat(resource.weight).isBetween(0.1 * profession.experience, 0.5 * profession.experience)
    }
}