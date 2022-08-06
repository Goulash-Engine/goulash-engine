package com.goulash.script.domain

data class ContextFilter(
    val type: String,
    val attribute: String,
    val operator: String,
    val argument: String
)
