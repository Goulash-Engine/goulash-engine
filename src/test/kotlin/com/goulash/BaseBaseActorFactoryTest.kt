package com.goulash

import assertk.assertThat
import assertk.assertions.containsSubList
import com.goulash.factory.BaseActorFactory
import com.goulash.script.loader.ScriptLoader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class BaseBaseActorFactoryTest {
    @AfterEach
    fun tearDown() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should create actor with explicity activities plus scriptloader activites`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("activity.gsh")
        config.writeText(
            """ 
            activity sleeping {
                logic act {actor::urge(eat).plus(1); }
            }
            """.trimIndent()
        )
        ScriptLoader.loadActivityScripts(tempDir.path)
        val activityScripts = ScriptLoader.getActivityScripts()

        val testActor = BaseActorFactory.newActor("")
        val activities = testActor.activities

        assertThat(activities).containsSubList(activityScripts)
    }
}
