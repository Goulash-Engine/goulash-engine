package com.goulash.core.domain

import com.goulash.core.activity.Activity

interface Actor {
    val id: String
    val key: String
    val name: String
    val conditions: MutableSet<String>
    var activity: String
    val urges: Urges
    val state: MutableMap<String, Double>

    fun copy(): Actor
}
