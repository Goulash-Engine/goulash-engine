package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType
import kotlin.random.Random

class ResourceProcessor {
    fun process(profession: Profession): Resource {
        return when (profession.type) {
            ProfessionType.GATHERER -> generateFood(profession.experience)
            ProfessionType.WOODWORKER -> generateWoodenMaterial(profession.experience)
            ProfessionType.TOOLMAKER -> Resource(ResourceType.TOOLS, 1.0)
            ProfessionType.HERBALIST -> Resource(ResourceType.HERBS, 1.0)
        }
    }

    private fun generateWoodenMaterial(experience: Double): Resource {
        val quantity = Random.nextDouble(0.1 * (experience / 2), 0.5 * experience)
        return Resource(ResourceType.WOODEN_MATERIAL, quantity)
    }
    private fun generateFood(experience: Double): Resource {
        val quantity = Random.nextDouble(0.1 * experience, 0.5 * experience)
        return Resource(ResourceType.FOOD, quantity)
    }
}
