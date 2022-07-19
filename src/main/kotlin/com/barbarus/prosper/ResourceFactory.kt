package com.barbarus.prosper

import com.barbarus.prosper.core.domain.Resource
import com.barbarus.prosper.core.domain.ResourceType

object ResourceFactory {

    fun woodenMaterial() = Resource(
        type = ResourceType.WOODEN_MATERIAL,
        weight = 1.0,
        traits = mutableListOf(
            "flammable",
            "shapeable",
            "resistant"
        )
    )

    fun food() = Resource(
        type = ResourceType.FOOD,
        weight = 1.0,
        traits = mutableListOf(
            "consumable",
            "cookable",
            "decayable"
        )
    )
}
