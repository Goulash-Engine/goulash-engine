package com.barbarus.prosper.actor.activity

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

    /**
     * The activity that should be set as current activity for the [Actor]
     */
    fun activity(): String

    /**
     * Duration of the activity to last in ticks
     */
    fun duration(): Int
    fun act(actor: Actor)
}
