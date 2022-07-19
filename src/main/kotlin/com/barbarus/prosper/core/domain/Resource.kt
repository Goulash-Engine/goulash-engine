package com.barbarus.prosper.core.domain

enum class ResourceType {
    FOOD,
    WOODEN_MATERIAL,
    HERBS,
    TOOLS,
}

data class Resource(
    val type: ResourceType,
    val quantity: Double = 0.1
)
