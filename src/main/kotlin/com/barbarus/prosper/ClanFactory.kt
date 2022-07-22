package com.barbarus.prosper

import com.barbarus.prosper.behavior.AmbitionBehavior
import com.barbarus.prosper.behavior.AwakeBehavior
import com.barbarus.prosper.behavior.Behavior
import com.barbarus.prosper.behavior.ConsumptionBehavior
import com.barbarus.prosper.behavior.ExhaustionBehavior
import com.barbarus.prosper.behavior.IdleBehavior
import com.barbarus.prosper.behavior.RestBehavior
import com.barbarus.prosper.behavior.StarvationBehavior
import com.barbarus.prosper.behavior.WorkActivity
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import kotlin.random.Random

object ClanFactory {

    fun testClan(mockedBehaviors: List<Behavior> = listOf()) = Clan(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        behaviors = mockedBehaviors
    )

    fun poorGathererClan() = Clan(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        behaviors = clanBehavior()
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
        behaviors = clanBehavior()
    )

    private fun clanBehavior() = mutableListOf(
        AwakeBehavior(),
        WorkActivity(),
        ConsumptionBehavior(),
        RestBehavior(),
        IdleBehavior(),
        AmbitionBehavior(),
        ExhaustionBehavior(),
        StarvationBehavior()
    )
}
