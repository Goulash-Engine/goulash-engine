package com.barbarus.prosper.core.domain

/**
 * Represenation of a [DemoActor]s state
 */
data class State(
    var health: Double = 100.0,
    var nourishment: Double = 100.0
)
