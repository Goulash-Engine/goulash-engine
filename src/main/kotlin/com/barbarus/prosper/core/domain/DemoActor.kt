package com.barbarus.prosper.core.domain

import com.barbarus.prosper.actor.logic.ConditionLogic
import com.barbarus.prosper.actor.logic.InventoryLogic
import com.barbarus.prosper.actor.logic.StateLogic
import com.barbarus.prosper.core.DecisionEngine
import com.barbarus.prosper.core.activity.Activity
import com.barbarus.prosper.core.logic.Logic
import com.barbarus.prosper.factory.ActorNameFactory
import org.slf4j.LoggerFactory
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
    override val conditions: MutableSet<String> = mutableSetOf()
) : Actor {
    override val urges: Urges = Urges().also { it.increaseUrge("think", 1.0) }
    private val _state: State = State()
    private val actorLogics: List<Logic<Actor>> = listOf(
        DecisionEngine(),
        ConditionLogic(),
        StateLogic(),
        InventoryLogic()
    )
    override var currentActivity: String = ""

    override val state: State
        get() = _state

    override fun inventory(): MutableList<Resource> {
        return this.stash
    }

    override fun act() {
        actorLogics.forEach { it.process(this) }
        LOG.debug("Actor $name conditions: $conditions and state: $state")
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
        return "Actor(id='$id', primaryProfession=$primaryProfession, stash=$stash, activitys=$activities, conditions=$conditions, _state=$_state)"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("DemoActor")
    }
}
