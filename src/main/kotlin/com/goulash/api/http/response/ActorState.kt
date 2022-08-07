package com.goulash.api.http.response

import com.goulash.core.domain.Actor

data class ActorState(
    val key: String,
    val state: Map<String, Double>,
    val urges: Map<String, Double>,
    val activity: String,
    val conditions: List<String>
)

fun Actor.toResponse() = ActorState(
    key = this.key,
    state = this.state,
    urges = this.urges.getAllUrges().toSortedMap(),
    activity = this.currentActivity,
    conditions = this.conditions.toList()
)
