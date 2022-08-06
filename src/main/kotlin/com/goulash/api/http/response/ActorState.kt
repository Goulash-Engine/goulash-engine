package com.goulash.api.http.response

import com.goulash.core.domain.Actor

data class ActorState(
    val id: String,
    val name: String,
    val state: Map<String, Double>,
    val urges: Map<String, Double>,
    val activity: String,
    val conditions: List<String>
)

fun Actor.toResponse() = ActorState(
    id = this.id,
    name = this.name,
    state = mapOf(
        "health" to (this.state["health"] ?: 0.0),
        "nourishment" to (this.state["nourishment"] ?: 0.0)
    ),
    urges = this.urges.getAllUrges().toSortedMap(),
    activity = this.currentActivity,
    conditions = this.conditions.toList()
)