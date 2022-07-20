package com.barbarus.prosper.processor

interface Processor<T : Any> {
    fun process(actor: T)
}
