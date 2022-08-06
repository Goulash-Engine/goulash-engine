package com.goulash.core.logic

interface Logic<T : Any> {
    fun process(context: T)
}
