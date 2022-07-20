package com.barbarus.prosper.logic

interface Logic<T : Any> {
    fun process(context: T)
}
