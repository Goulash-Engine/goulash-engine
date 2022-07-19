package com.barbarus.prosper.core.domain

/**
 * Represenation of a [Clan]s state
 */
data class State(
    var stamina: Double = 100.0,
    var hunger: Double = 0.0,
    var health: Double = 100.0
)
