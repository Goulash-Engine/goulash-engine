package com.barbarus.prosper.core.logic

import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType
import kotlin.random.Random

class ResourceLogic {
    fun process(profession: Profession): Resource {
        return when (profession.type) {
            ProfessionType.GATHERER -> generateFood(profession.experience)
            ProfessionType.WOODWORKER -> generateWoodenMaterial(profession.experience)
            ProfessionType.TOOLMAKER -> Resource(type = ResourceType.TOOLS, weight = 1.0)
            ProfessionType.HERBALIST -> Resource(type = ResourceType.HERBS, weight = 1.0)
        }
    }

    private fun generateWoodenMaterial(experience: Double): Resource {
        val weight = Random.nextDouble(0.1 * (experience / 2), 0.5 * (experience / 2))
        return Resource(type = ResourceType.WOODEN_MATERIAL, weight = weight)
    }

    private fun generateFood(experience: Double): Resource {
        val weight = Random.nextDouble(0.1 * experience, 0.5 * experience)
        return Resource(type = ResourceType.FOOD, weight = weight)
    }
}
