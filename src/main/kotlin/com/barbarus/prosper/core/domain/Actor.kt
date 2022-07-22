package com.barbarus.prosper.core.domain

import com.barbarus.prosper.behavior.Behavior

interface Actor {
    val id: String
    val conditions: MutableSet<String>
    val behaviors: List<Behavior>
    val urges: Urges
    var currentActivity: String
    val state: State
    fun inventory(): MutableList<Resource>
    fun act()
}
