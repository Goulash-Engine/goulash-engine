package com.barbarus.prosper.core.domain

interface Actor {
    val id: String
    val desires: List<String>
    fun inventory(): MutableList<Resource>
    fun act()
}
