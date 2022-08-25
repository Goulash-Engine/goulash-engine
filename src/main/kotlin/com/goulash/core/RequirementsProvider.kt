package com.goulash.core

import com.goulash.core.domain.Actor

/**
 * Stores provided requirements to be accessed by individual [Actor]s
 * to satisfy their activity requirements.
 */
object RequirementsProvider {
    private val providedRequirements: MutableMap<Actor, MutableList<String>> = mutableMapOf()

    fun satisfy(actor: Actor, requirement: String) {
        val provided = providedRequirements.getOrDefault(actor, null)
        if (provided == null) {
            providedRequirements[actor] = mutableListOf(requirement)
        } else {
            provided.add(requirement)
        }
    }

    fun providesAll(actor: Actor): List<String> {
        return providedRequirements.getOrElse(actor) { listOf() }
    }

    fun isProvided(actor: Actor, requirement: String): Boolean {
        val provided = providedRequirements.getOrElse(actor) { mutableListOf() }
        if (provided.isEmpty()) return false
        return provided.contains(requirement)
    }

    fun consume(actor: Actor, requirement: String): Boolean {
        val provided = providedRequirements.getOrElse(actor) { mutableListOf() }
        if (provided.isEmpty()) return false
        return provided.remove(requirement)
    }

}
