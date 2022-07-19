package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Clan

class ConditionProcessor {
    fun process(clan: Clan) {
        if (clan.health < 60) clan.addCondition("sleep")
        if (clan.stamina < 30) clan.addCondition("sleep")
        if (clan.hunger > 30) clan.addCondition("hunger")
        if (clan.stamina > 60) clan.addCondition("work")
    }
}
