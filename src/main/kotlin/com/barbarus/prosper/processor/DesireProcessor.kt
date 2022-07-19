package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Clan

class DesireProcessor {
    fun process(clan: Clan) {
        if (clan.health < 60) clan.addDesire("sleep")
        if (clan.stamina < 30) clan.addDesire("sleep")
        if (clan.hunger > 30) clan.addDesire("hunger")
        if (clan.stamina > 60) clan.addDesire("work")
    }
}
