package com.goulash.core.domain

import com.goulash.core.DecisionEngine
import com.goulash.core.activity.Activity
import com.goulash.factory.ActorNameFactory
import java.util.UUID

/**
 * Base implementation of the [Actor] interface.
 */
class BaseActor(
    override val name: String = ActorNameFactory.randomName(),
    override val id: String = UUID.randomUUID().toString(),
    override val key: String,
    override val activities: List<Activity> = listOf(),
    override val conditions: MutableSet<String> = mutableSetOf(),
    override val state: MutableMap<String, Double> = mutableMapOf()
) : Actor {
    override val urges: Urges = Urges()
    private val decisionEngine = DecisionEngine()
    override var currentActivity: String = ""

    override fun tick() {
        decisionEngine.tick(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseActor

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
