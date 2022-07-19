package com.barbarus.prosper.core.domain

enum class ProfessionType {
    GATHERER,
    WOODWORKER,
    TOOLMAKER,
    HERBALIST
}

/**
 * The profession is the core feature that allows entities to generate resources or commodities.
 */
data class Profession(
    val type: ProfessionType = ProfessionType.GATHERER,
    val experience: Double = 1.0
)
