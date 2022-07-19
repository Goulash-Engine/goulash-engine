package com.barbarus.prosper.core.domain

import java.util.UUID

/**
 * The core element of the village. A village is build upon multiple clans that provide a cycle for self-sufficiency.
 */
data class Clan(
    val id: String = UUID.randomUUID().toString(),
    val primaryProfession: Profession,
    val stash: MutableList<Resource> = mutableListOf()
) {
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
