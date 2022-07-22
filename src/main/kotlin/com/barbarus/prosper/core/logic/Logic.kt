package com.barbarus.prosper.core.logic

interface Logic<T : Any> {
    fun process(context: T)
}
