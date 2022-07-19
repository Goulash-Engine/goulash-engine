package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

interface Behavior {
    fun act(actor: Actor)
}
