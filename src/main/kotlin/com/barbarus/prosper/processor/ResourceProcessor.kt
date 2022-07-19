package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType

class ResourceProcessor {
    fun process(profession: Profession): Resource {
        return Resource(ResourceType.FOOD, 1.0)
    }
}
