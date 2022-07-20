package com.barbarus.prosper.core.domain

import com.barbarus.prosper.ClanNameFactory
import com.barbarus.prosper.behavior.Behavior
import com.barbarus.prosper.logic.Logic
import com.barbarus.prosper.logic.clan.BehaviorLogic
import com.barbarus.prosper.logic.clan.StateConditionLogic
import com.barbarus.prosper.logic.clan.DeathLogic
import com.barbarus.prosper.logic.clan.InventoryLogic
import com.barbarus.prosper.logic.clan.StarvationLogic
import org.slf4j.LoggerFactory
import java.util.UUID

/**
 * The core element of the village. A village is build upon multiple clans that provide a cycle for self-sufficiency.
 */
class Clan(
    val name: String = ClanNameFactory.randomName(),
    override val id: String = UUID.randomUUID().toString(),
    val primaryProfession: Profession,
    val stash: MutableList<Resource> = mutableListOf(),
    override val behaviors: List<Behavior> = listOf(),
    override val conditions: MutableSet<String> = mutableSetOf()
) : Actor {
    private val _state: State = State()
    private val actorLogics: List<Logic<Actor>> = listOf(
        BehaviorLogic(),
        StateConditionLogic(),
        InventoryLogic(),
        StarvationLogic(),
        DeathLogic()
    )

    override val state: State
        get() = _state

    val health: Double
        get() = _state.health

    val stamina: Double
        get() = _state.stamina

    val hunger: Double
        get() = _state.hunger

    override fun inventory(): MutableList<Resource> {
        return this.stash
    }

    override fun act() {
        actorLogics.forEach { it.process(this) }
        LOG.debug("Clan $name conditions: $conditions and state: $state")
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
        return "Clan(id='$id', primaryProfession=$primaryProfession, stash=$stash, behaviors=$behaviors, conditions=$conditions, _state=$_state)"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger("Clan")
    }
}
