package com.barbarus.prosper.core.domain

import com.barbarus.prosper.behavior.Behavior
import com.barbarus.prosper.processor.BehaviorProcessor
import com.barbarus.prosper.processor.ConditionProcessor
import org.slf4j.LoggerFactory
import java.util.UUID

/**
 * The core element of the village. A village is build upon multiple clans that provide a cycle for self-sufficiency.
 */
class Clan(
    override val id: String = UUID.randomUUID().toString(),
    val primaryProfession: Profession,
    val stash: MutableList<Resource> = mutableListOf(),
    val behaviors: MutableList<Behavior> = mutableListOf()
) : Actor {
    private val _conditions: MutableSet<String> = mutableSetOf()
    private val _state: State = State()

    override val conditions: List<String>
        get() = _conditions.toList()

    override val state: State
        get() = _state

    val health: Double
        get() = _state.health

    val stamina: Double
        get() = _state.stamina

    val hunger: Double
        get() = _state.hunger

    fun addCondition(desire: String) {
        _conditions.add(desire)
    }

    override fun inventory(): MutableList<Resource> {
        return this.stash
    }

    override fun act() {
        val behaviorProcessor = BehaviorProcessor()
        val conditionProcessor = ConditionProcessor()
        conditionProcessor.process(this)
        LOG.info("Clan $id conditions: $conditions")
        behaviorProcessor.process(this, behaviors)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Clan

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Clan(id='$id', primaryProfession=$primaryProfession, stash=$stash, behaviors=$behaviors, _conditions=$_conditions, _state=$_state)"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Clan")
    }
}

