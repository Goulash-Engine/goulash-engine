package com.goulash.factory

import com.goulash.core.activity.Activity
import com.goulash.core.domain.DemoActor
import com.goulash.core.domain.Profession
import com.goulash.core.domain.ProfessionType
import com.goulash.script.loader.ScriptLoader
import kotlin.random.Random

object ActorFactory {

    fun testActor(mockedActivities: List<Activity> = listOf()) = DemoActor(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        state = initializeStateValues(),
        activities = mockedActivities
    )

    fun poorActor() = DemoActor(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        state = initializeStateValues(),
        activities = actorActivities()
    )

    fun simpleGathererActor() = DemoActor(
        primaryProfession = Profession(ProfessionType.GATHERER, experience = 1.0),
        stash = mutableListOf(
            ResourceFactory.woodenMaterial(),
            ResourceFactory.woodenMaterial(),
            ResourceFactory.woodenMaterial(),
            ResourceFactory.food(),
            ResourceFactory.food()
        ),
        state = initializeStateValues(),
        activities = actorActivities()
    )

    private fun initializeStateValues(): MutableMap<String, Double> {
        return mutableMapOf(
            "health" to 100.0,
            "nourishment" to 0.0
        )
    }

    private fun actorActivities() = mutableListOf<Activity>().also { it.addAll(ScriptLoader.getActivityScripts()) }
}
