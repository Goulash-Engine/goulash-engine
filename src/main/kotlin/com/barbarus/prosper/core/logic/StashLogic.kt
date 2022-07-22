package com.barbarus.prosper.core.logic

import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Resource

class StashLogic {
    fun process(resource: Resource, clan: Clan) {
        clan.stash.add(resource)
    }
}
