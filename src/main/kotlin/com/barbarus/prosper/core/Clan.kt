package com.barbarus.prosper.core

import com.barbarus.prosper.core.domain.Profession

/**
 * The core element of the village. A village is build upon multiple clans that provide a cycle for self-sufficiency.
 */
data class Clan(
    val primaryProfession: Profession
)
