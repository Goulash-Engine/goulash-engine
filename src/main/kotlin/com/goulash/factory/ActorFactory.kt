package com.goulash.factory

import com.goulash.core.activity.Activity
import com.goulash.core.domain.BaseActor
import com.goulash.script.loader.ScriptLoader
import kotlin.random.Random

object ActorFactory {

    fun testActor(mockedActivities: List<Activity> = listOf()) = BaseActor(
        key = "",
        // TODO remove stash
        stash = mutableListOf(
            ResourceFactory.woodenMaterial()
        ).also {
            it.addAll(
                generateSequence { ResourceFactory.food() }.take(Random.nextInt(1, 3))
            )
        },
        state = mutableMapOf(),
        activities = mockedActivities
    )

    fun newActor(key: String) = BaseActor(
        key = key,
        activities = actorActivities()
    )

    private fun actorActivities() = mutableListOf<Activity>().also { it.addAll(ScriptLoader.getActivityScripts()) }
}
