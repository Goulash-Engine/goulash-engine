package com.barbarus.prosper

import com.barbarus.prosper.behavior.AwakeBehavior
import com.barbarus.prosper.behavior.ConsumptionBehavior
import com.barbarus.prosper.behavior.WorkBehavior
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import kotlin.random.Random

object ClanFactory {

    fun poorGathererClan() = Clan(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        behaviors = mutableListOf(
            AwakeBehavior(),
            WorkBehavior(),
            ConsumptionBehavior()
        )
    )

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
            AwakeBehavior(),
            WorkBehavior(),
            ConsumptionBehavior()
        )
    )
}
