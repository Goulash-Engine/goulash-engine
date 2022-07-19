package com.barbarus.prosper.core.domain

import java.util.UUID

enum class ResourceType {
    FOOD,
    WOODEN_MATERIAL,
    HERBS,
    TOOLS,
}

data class Resource(
    val id: String = UUID.randomUUID().toString(),
    val type: ResourceType,
    val quantity: Double = 0.1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resource

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Resource(id='$id', type=$type, quantity=$quantity)"
    }
}
