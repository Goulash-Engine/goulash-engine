package com.goulash.core.domain

import com.goulash.core.ActivityRunner
import com.goulash.core.ActivityManager
import com.goulash.script.loader.ScriptLoader

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val id: String = ROOT_CONTAINER,
    val actors: MutableList<Actor> = mutableListOf(),
    private val activityManager: ActivityManager = ActivityManager(),
    private val activityRunner: ActivityRunner = ActivityRunner()
) {
    init {
        ScriptLoader.containerScripts.forEach { it.init(this) }
    }

    fun tick() {
        ScriptLoader.containerScripts.forEach { it.process(this) }
        actors.forEach { actor ->
            activityManager.tick(actor)
            activityRunner.tick(actor)
        }
    }

    companion object {
        const val ROOT_CONTAINER = "root"
    }
}
