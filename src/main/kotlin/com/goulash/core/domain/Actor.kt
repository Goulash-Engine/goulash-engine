package com.goulash.core.domain

import com.goulash.core.activity.Activity

interface Actor {
    val id: String
    val key: String
    val name: String
    val conditions: MutableSet<String>
    val activities: List<Activity>
    val urges: Urges
    var currentActivity: String
    var activity: Activity?
    val state: MutableMap<String, Double>
}
