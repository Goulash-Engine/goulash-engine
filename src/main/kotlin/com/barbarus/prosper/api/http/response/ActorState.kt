package com.barbarus.prosper.api.http.response

data class ActorState(
    val id: String,
    val name: String,
    val state: Map<String, Double>,
    val urges: Map<String, Double>,
    val activity: String,
    val conditions: List<String>
)
