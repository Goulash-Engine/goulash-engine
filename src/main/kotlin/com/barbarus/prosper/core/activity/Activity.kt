package com.barbarus.prosper.core.activity

import com.barbarus.prosper.core.domain.Actor

interface Activity {
    /**
     * The urges of an [Actor] that trigger this activity
     */
    fun triggerUrges(): List<String>

    /**
     * A blacklist conditions that prevents the [Activity] from being triggered
     */
    fun blockerConditions(): List<String>

    /**
     * The activity that should be set as current activity for the [Actor]
     */
    fun activity(): String

    /**
     * Duration of the activity to last in ticks. It's dependent on
     * what baseline [WorldDate] has been set to.
     * So a tick can either represent a minute, an hour and so on.
     */
    fun duration(): Duration

    /**
     * Logic to be executed when the activity finishes hence it's duration is over
     */
    fun onFinish(actor: Actor) = Unit
    fun act(actor: Actor)
}
