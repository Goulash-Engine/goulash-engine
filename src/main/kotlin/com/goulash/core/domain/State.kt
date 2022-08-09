package com.goulash.core.domain

/**
 * Represenation of a [BaseActor]s state
 */
data class State(
    var health: Double = 100.0,
    var nourishment: Double = 100.0
)
