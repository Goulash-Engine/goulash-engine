package com.barbarus.prosper.activity

import com.barbarus.prosper.core.domain.Actor

interface Activity {
    /**
     * The urges of an [Actor] that trigger this activity
     */
    fun triggerUrge(): List<String>

    /**
     * A blacklist conditions that prevents the [Activity] from being triggered
     */
    fun blockerCondition(): List<String>

    fun activity(): String
    fun act(actor: Actor)
}
