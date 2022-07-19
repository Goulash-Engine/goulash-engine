package com.barbarus.prosper.core.domain

interface Actor {
    val id: String
    fun inventory(): MutableList<Resource>
    fun act()
}
