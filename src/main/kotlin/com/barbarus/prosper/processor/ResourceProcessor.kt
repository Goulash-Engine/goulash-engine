package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType

class ResourceProcessor {
    fun process(profession: Profession): Resource {
        return when (profession.type) {
            ProfessionType.GATHERER -> Resource(ResourceType.FOOD, 1.0)
            ProfessionType.WOODWORKER -> Resource(ResourceType.WOODEN_MATERIAL, 1.0)
            ProfessionType.TOOLMAKER -> TODO()
            ProfessionType.HERBALIST -> TODO()
        }
    }
}
