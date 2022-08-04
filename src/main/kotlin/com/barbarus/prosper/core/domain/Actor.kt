package com.barbarus.prosper.core.domain

import com.barbarus.prosper.core.activity.Activity

interface Actor {
    val id: String
    val name: String
    val conditions: MutableSet<String>
    val activities: List<Activity>
    val urges: Urges
    var currentActivity: String
    val state: State

    // TODO: Add buffs as concepts of temporary conditions
    fun inventory(): MutableList<Resource>
    fun act()
}
