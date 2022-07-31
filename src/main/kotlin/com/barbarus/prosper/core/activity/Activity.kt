package com.barbarus.prosper.core.activity

import com.barbarus.prosper.core.domain.Actor
import com.barbarus.prosper.core.extension.toDuration

interface Activity {
    /**
     * The urges of an [Actor] that trigger this activity. Defaults to a wildcard trigger.
     * That means it will trigger always.
     */
    fun triggerUrges(): List<String> = listOf("*")

    /**
     * A blacklist conditions that prevents the [Activity] from being triggered
     */
    fun blockerConditions(): List<String> = listOf()

    /**
     * A list of conditions that will stop the acting of this
     * [Activity] if they are met. The abortion will take **before** the
     * [act] function will be called.
     */
    fun abortConditions(): List<String> = listOf()

    /**
     * If any of these conditions are met, the [Activity] will have priority in execution
     * regardless to the urge level.
     */
    fun priorityConditions(): List<String> = listOf()

    /**
     * Priority of this activity. Defaults to [Int.MAX_VALUE]
     */
    fun priority(): Int = Int.MAX_VALUE

    /**
     * The activity that should be set as current activity for the [Actor]
     */
    fun activity(): String

    /**
     * Duration of the activity to last in ticks. It's dependent on
     * what baseline [WorldDate] has been set to.
     * So a tick can either represent a minute, an hour and so on.
     */
    fun duration(): Duration = 1.0.toDuration()

    /**
     * Logic to be executed when the activity finishes hence it's duration is over
     */
    fun onFinish(actor: Actor) = Unit

    /**
     * Logic to be executed when the activity is being aborted.
     */
    fun onAbort(actor: Actor) = Unit

    /**
     * Logic to be executed when the activity is being started.
     * @return true if the [Activity] continues - otherwise false.
     */
    fun act(actor: Actor): Boolean = true
}
