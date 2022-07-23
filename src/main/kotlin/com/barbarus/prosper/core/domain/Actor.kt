package com.barbarus.prosper.core.domain

import com.barbarus.prosper.core.activity.Activity

interface Actor {
    val id: String
    val conditions: MutableSet<String>
    val activities: List<Activity>
    val urges: Urges
    var currentActivity: String
    val state: State
    fun inventory(): MutableList<Resource>
    fun act()
}
