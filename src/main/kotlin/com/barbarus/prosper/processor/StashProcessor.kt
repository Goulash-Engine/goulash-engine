package com.barbarus.prosper.processor

import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Resource

class StashProcessor {
    fun process(resource: Resource, clan: Clan) {
        clan.stash.add(resource)
    }
}
