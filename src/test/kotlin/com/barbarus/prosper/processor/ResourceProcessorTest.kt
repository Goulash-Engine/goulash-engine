package com.barbarus.prosper.processor

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.ResourceType
import org.junit.jupiter.api.Test

internal class ResourceProcessorTest {
    private val resourceProcessor = ResourceProcessor()

    @Test
    fun `should generate a wooden material resource when the profession type is woodworker`() {
        val profession = Profession(ProfessionType.WOODWORKER)

        val resource = resourceProcessor.process(profession)

        assertThat(resource.type).isEqualTo(ResourceType.WOODEN_MATERIAL)
    }

    @Test
    fun `should generate a food resource when the profession type is gatherer`() {
        val profession = Profession(ProfessionType.GATHERER, 2.0)

        val resource = resourceProcessor.process(profession)

        assertThat(resource.type).isEqualTo(ResourceType.FOOD)
    }
}
