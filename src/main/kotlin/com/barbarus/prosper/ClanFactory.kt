package com.barbarus.prosper

import com.barbarus.prosper.behavior.ConsumptionBehavior
import com.barbarus.prosper.behavior.WorkBehavior
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType

object ClanFactory {

    fun simpleGathererClan() = Clan(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial(),
            ResourceFactory.woodenMaterial(),
            ResourceFactory.woodenMaterial(),
            ResourceFactory.food(),
            ResourceFactory.food()
        ),
        behaviors = mutableListOf(
            ConsumptionBehavior(),
            WorkBehavior()
        )
    )
}
