package com.goulash.api.http.response

data class SimulationStatus(
    val status: String = "not running",
    val ticks: Long
)
