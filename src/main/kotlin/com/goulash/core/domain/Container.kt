package com.goulash.core.domain

import com.goulash.core.ActivityManager
import com.goulash.script.extension.ActivityExtensions.createRunner
import com.goulash.script.loader.ScriptLoader

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val id: String = ROOT_CONTAINER,
    val actors: MutableList<Actor> = mutableListOf(),
    private val activityManager: ActivityManager = ActivityManager()
) {
    init {
        ScriptLoader.containerScripts.forEach { it.init(this) }
    }

    fun tick() {
        ScriptLoader.containerScripts.forEach { it.process(this) }
        actors.forEach { actor ->
            activityManager.resolve(actor) { activity ->
                if (actor.activityRunner.isRunning()) {
                    actor.tick()
                } else {
                    actor.activityRunner = activity.createRunner()
                    actor.tick()
                }
            }
        }
    }

    companion object {
        const val ROOT_CONTAINER = "root"
    }
}
