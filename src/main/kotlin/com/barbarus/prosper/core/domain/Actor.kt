package com.barbarus.prosper.core.domain

interface Actor {
    fun inventory(): MutableList<Resource>
}
