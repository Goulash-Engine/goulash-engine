package com.goulash.core.domain

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val id: String = ROOT_CONTAINER,
    val actors: MutableList<Actor> = mutableListOf()
) {
    companion object {
        const val ROOT_CONTAINER = "root"
    }
}
