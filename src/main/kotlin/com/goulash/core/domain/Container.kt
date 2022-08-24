package com.goulash.core.domain

/**
 * The [Container] represents a logic execution context that holds a number of [Actor]s within.
 * The children [Actor]s of a [Container] all are target by the logic applied to this [Container].
 */
class Container(
    val id: String = ROOT_CONTAINER,
    private val actors: MutableList<Actor> = mutableListOf()
) {
    private var transactionalState: List<Actor>? = null

    fun addActor(actor: Actor) {
        actors.add(actor)
    }

    fun getActors(): List<Actor> = transactionalState ?: actors

    /**
     * Mutate actors transactionally.
     */
    fun mutateActors(mutation: (actors: List<Actor>) -> Unit) {
        transactionalState = actors.map { it.copy() }
        mutation(actors)
        transactionalState = actors
    }

    companion object {
        const val ROOT_CONTAINER = "root"
    }
}
