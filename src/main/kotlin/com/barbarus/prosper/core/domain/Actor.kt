package com.barbarus.prosper.core.domain

interface Actor {
    val id: String
    val conditions: List<String>
    val state: State
    fun inventory(): MutableList<Resource>
    fun act()
}
