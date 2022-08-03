package com.barbarus.prosper

import assertk.assertThat
import assertk.assertions.containsSubList
import assertk.assertions.isBetween
import com.barbarus.prosper.core.domain.ResourceType
import com.barbarus.prosper.factory.ActorFactory
import com.barbarus.prosper.script.loader.ScriptLoader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class DemoActorFactoryTest {
    @AfterEach
    fun tearDown() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should create actor with explicity activities plus scriptloader activites`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("activity.pros")
        config.writeText(
            """ 
            activity sleeping {
                logic act {actor::urge(eat).plus(1); }
            }
            """.trimIndent()
        )
        ScriptLoader.loadActivityScripts(tempDir.path)
        val activityScripts = ScriptLoader.getActivityScripts()

        val poorGathererActor = ActorFactory.poorActor()
        val activities = poorGathererActor.activities

        assertThat(activities).containsSubList(activityScripts)
    }

    @RepeatedTest(5)
    fun `should create a poor actor with 1 to 3 food`() {
        val poorGathererActor = ActorFactory.poorActor()
        assertThat(poorGathererActor.stash.filter { it.type == ResourceType.FOOD }.size).isBetween(1, 3)
    }
}
