package com.goulash.factory

import com.goulash.core.activity.Activity
import com.goulash.core.domain.BaseActor
import com.goulash.script.loader.ScriptLoader

object BaseActorFactory {

    fun testActor(mockedActivities: List<Activity> = listOf()) = BaseActor(
        key = "",
        state = mutableMapOf(),
        activities = mockedActivities
    )

    fun newActor(key: String) = BaseActor(
        key = key,
        activities = actorActivities()
    )

    private fun actorActivities() = mutableListOf<Activity>().also { it.addAll(ScriptLoader.getActivityScripts()) }
}
