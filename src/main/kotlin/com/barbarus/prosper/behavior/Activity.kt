package com.barbarus.prosper.behavior

import com.barbarus.prosper.core.domain.Actor

interface Activity {
    /**
     * The urges of an [Actor] that trigger this behavior
     */
    fun triggerUrge(): List<String>

    /**
     * A blacklist conditions that prevents the [Activity] from being triggered
     */
    fun blockerCondition(): List<String>
    fun act(actor: Actor)
}
