package com.barbarus.prosper.logic

interface Logic<T : Any> {
    fun process(actor: T)
}
