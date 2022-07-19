package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

interface Behavior {
    /**
     * The condition of an [Actor] that trigger this behavior
     */
    fun trigger(): String

    /**
     * A blacklist condition that prevents the [Behavior] from being triggered
     */
    fun blocker(): String
    fun act(actor: Actor)
}
