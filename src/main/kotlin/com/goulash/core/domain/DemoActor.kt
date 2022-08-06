package com.goulash.core.domain

import com.goulash.actor.logic.ConditionLogic
import com.goulash.actor.logic.InventoryLogic
import com.goulash.actor.logic.StateLogic
import com.goulash.core.DecisionEngine
import com.goulash.core.activity.Activity
import com.goulash.core.logic.Logic
import com.goulash.factory.ActorNameFactory
import java.util.UUID

/**
 * The core element of the village. A village is build upon multiple actors that provide a cycle for self-sufficiency.
 */
class DemoActor(
    override val name: String = ActorNameFactory.randomName(),
    override val id: String = UUID.randomUUID().toString(),
    val primaryProfession: Profession,
    val stash: MutableList<Resource> = mutableListOf(),
    override val activities: List<Activity> = listOf(),
    override val conditions: MutableSet<String> = mutableSetOf(),
    override val state: MutableMap<String, Double> = mutableMapOf()
) : Actor {
    override val urges: Urges = Urges().also { it.increaseUrge("think", 1.0) }
    private val actorLogics: List<Logic<Actor>> = listOf(
        DecisionEngine(),
        ConditionLogic(),
        StateLogic(),
        InventoryLogic()
    )
    override var currentActivity: String = ""

    override fun inventory(): MutableList<Resource> {
        return this.stash
    }

    override fun act() {
        actorLogics.forEach { it.process(this) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DemoActor

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "DemoActor(name='$name', id='$id', primaryProfession=$primaryProfession, stash=$stash, activities=$activities, conditions=$conditions, urges=$urges, state=$state, actorLogics=$actorLogics, currentActivity='$currentActivity')"
    }
}