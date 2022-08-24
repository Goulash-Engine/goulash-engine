package com.goulash.core.domain

import com.goulash.actor.activity.IdleActivity
import com.goulash.core.ActivityRunner
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
    override var urges: Urges = Urges()

    // TODO remove if [activity] is working
    override var currentActivity: String = ""
    override var activityRunner: ActivityRunner = ActivityRunner(IdleActivity(), 0.0)

    override fun tick() {
        // TODO: move script logic to ContainerRunner
        activityRunner.run(this)
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

    override fun copy() = BaseActor(name, id, key, activities.toList(), conditions.toMutableSet(), state.toMutableMap())
        .also {
            it.urges = this.urges.copy()
            it.currentActivity = this.currentActivity
            it.activityRunner = this.activityRunner
        }
}
