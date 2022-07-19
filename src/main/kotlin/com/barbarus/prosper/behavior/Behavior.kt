package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

interface Behavior {
    fun trigger(): String
    fun act(actor: Actor)
}
