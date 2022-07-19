package com.barbarus.prosper.core.domain

import com.barbarus.prosper.behavior.Behavior
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
    private val _desires: MutableList<String> = mutableListOf()

    override val desires: MutableList<String>
        get() = _desires

    fun addDesire(desire: String) {
        _desires.add(desire)
    }

    override fun inventory(): MutableList<Resource> {
        return this.stash
    }

    override fun act() {
        behaviors.forEach { it.act(this) }
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
        return "Clan(id='$id', primaryProfession=$primaryProfession, stash=$stash)"
    }
}
