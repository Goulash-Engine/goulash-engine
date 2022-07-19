package com.barbarus.prosper.core.domain

enum class ProfessionType {
    Gatherer,
    Woodworker,
    Toolmaker,
    Herbalist
}

/**
 * The profession is the core feature that allows entities to generate resources or commodities.
 */
class Profession(
    val type: ProfessionType = ProfessionType.Gatherer,
    val experience: Double = 1.0
)
