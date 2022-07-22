package com.barbarus.prosper

import com.barbarus.prosper.activity.Activity
import com.barbarus.prosper.activity.AmbitionActivity
import com.barbarus.prosper.activity.AwakeActivity
import com.barbarus.prosper.activity.ConsumptionActivity
import com.barbarus.prosper.activity.ExhaustionActivity
import com.barbarus.prosper.activity.IdleActivity
import com.barbarus.prosper.activity.RestActivity
import com.barbarus.prosper.activity.StarvationActivity
import com.barbarus.prosper.activity.WorkActivity
import com.barbarus.prosper.core.domain.Clan
import com.barbarus.prosper.core.domain.Profession
import com.barbarus.prosper.core.domain.ProfessionType
import kotlin.random.Random

object ClanFactory {

    fun testClan(mockedActivities: List<Activity> = listOf()) = Clan(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        activities = mockedActivities
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
        activities = clanBehavior()
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
        activities = clanBehavior()
    )

    private fun clanBehavior() = mutableListOf(
        AwakeActivity(),
        WorkActivity(),
        ConsumptionActivity(),
        RestActivity(),
        IdleActivity(),
        AmbitionActivity(),
        ExhaustionActivity(),
        StarvationActivity()
    )
}
