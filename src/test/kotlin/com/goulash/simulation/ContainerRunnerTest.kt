package com.goulash.simulation

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.core.ActivityRunner
import com.goulash.core.ActivitySelector
import com.goulash.core.activity.Activity
import com.goulash.core.domain.Actor
import com.goulash.core.domain.Container
import com.goulash.factory.BaseActorFactory
import com.goulash.script.loader.ScriptLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ContainerRunnerTest {
    private val activitySelectorMock = mockk<ActivitySelector>(relaxed = true)
    private val containerRunner = ContainerRunner(activitySelector = activitySelectorMock)

    @BeforeEach
    fun setup() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should call activity init logic once when activity has started`() {
        val actorMock: Actor = mockk(relaxed = true)
        val activityMock: Activity = mockk(relaxed = true)
        every { activitySelectorMock.select(actorMock) } returns activityMock
        val container = Container(actors = mutableListOf(actorMock))

        containerRunner.register(container)
        containerRunner.tick()

        verifyOrder {
            activitySelectorMock.select(actorMock)
            activityMock.init(actorMock)
            actorMock.tick()
        }
    }

    @Test
    fun `should not resolve an activity if there is one still running`() {
        val actorMock: Actor = mockk(relaxed = true)
        val activityRunnerMock: ActivityRunner = mockk(relaxed = true)
        every { activitySelectorMock.select(actorMock) } returns mockk()
        every { activityRunnerMock.isRunning() } returns true
        every { actorMock.activityRunner } returns activityRunnerMock
        val container = Container(actors = mutableListOf(actorMock))

        containerRunner.register(container)
        containerRunner.tick()

        verifyOrder {
            activityRunnerMock.isRunning()
            actorMock.tick()
        }
        verify(inverse = true) { activitySelectorMock.select(actorMock) }
    }

    @Test
    fun `should run the activity manager for every actor in a container on every tick`() {
        val actorMock: Actor = mockk(relaxed = true)
        val container = Container(actors = mutableListOf(actorMock))

        containerRunner.register(container)
        containerRunner.tick()

        verifyOrder {
            activitySelectorMock.select(actorMock)
            actorMock.tick()
        }
    }

    @Test
    fun `should init container logic`(@TempDir tempDir: java.io.File) {
        val actors: MutableList<Actor> = mutableListOf(BaseActorFactory.testActor())
        val config = tempDir.resolve("logic.gsh")
        config.writeText(
            """ 
            container myfoo {
                logic container {
                    actors::urge(eat).plus(1);
                }
                logic init {
                    actors::state(health).set(20);
                }
            }
            """.trimIndent()
        )
        ScriptLoader.loadContainerScripts(tempDir.path)
        val container = Container(actors = actors)

        containerRunner.register(container)

        assertThat(actors[0].state["health"]).isEqualTo(20.0)
    }

    @Test
    fun `should invoke scripted logic`(@TempDir tempDir: java.io.File) {
        val actors: MutableList<Actor> = mutableListOf(BaseActorFactory.testActor())
        val config = tempDir.resolve("logic.gsh")
        config.writeText(
            """ 
            container myfoo {
                logic container {
                    actors::urge(eat).plus(1);
                }
            }
            """.trimIndent()
        )
        ScriptLoader.loadContainerScripts(tempDir.path)
        val container = Container(actors = actors)

        containerRunner.register(container)
        containerRunner.tick()

        assertThat(actors.first().urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
