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
    private val activityRunnerMock = mockk<ActivityRunner>(relaxed = true)
    private val containerRunner = ContainerRunner(
        activitySelector = activitySelectorMock,
        activityRunner = activityRunnerMock
    )

    @BeforeEach
    fun setup() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should call activity init logic once when activity has started`() {
        val actorMock: Actor = mockk(relaxed = true)
        val activityMock: Activity = mockk(relaxed = true)
        every { activitySelectorMock.select(actorMock) } returns activityMock
        every { activityRunnerMock.hasEnded(any()) } returns true
        val container = Container(actors = mutableListOf(actorMock))

        containerRunner.register(container)
        containerRunner.tick()

        verifyOrder {
            activitySelectorMock.select(actorMock)
            activityMock.init(actorMock)
            activityRunnerMock.start(actorMock, activityMock)
        }
    }

    @Test
    fun `should not resolve an activity if there is one still running`() {
        val actorMock: Actor = mockk(relaxed = true)
        val activityMock: Activity = mockk(relaxed = true)
        every { activitySelectorMock.select(actorMock) } returns activityMock
        every { activityRunnerMock.hasEnded(actorMock) } returns false
        val container = Container(actors = mutableListOf(actorMock))

        containerRunner.register(container)
        containerRunner.tick()

        verify { activityRunnerMock.`continue`(actorMock) }
        verify(inverse = true) { activitySelectorMock.select(actorMock) }
    }

    @Test
    fun `should run the activity selector for every actor in a container on every tick`() {
        val actorMock: Actor = mockk(name = "foo", relaxed = true)
        val actorMock2: Actor = mockk(name = "bar", relaxed = true)
        val container = Container(actors = mutableListOf(actorMock, actorMock2))
        every { activityRunnerMock.hasEnded(any()) } returns true

        containerRunner.register(container)
        containerRunner.tick()

        verify { activitySelectorMock.select(actorMock) }
        verify { activitySelectorMock.select(actorMock2) }
        verify { activityRunnerMock.start(actorMock, any()) }
        verify { activityRunnerMock.start(actorMock2, any()) }
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
