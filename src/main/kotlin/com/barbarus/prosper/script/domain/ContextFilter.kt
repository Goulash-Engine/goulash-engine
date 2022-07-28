package com.barbarus.prosper.script.domain

data class ContextFilter(
    val type: String,
    val attribute: String,
    val operator: String,
    val argument: String
)
