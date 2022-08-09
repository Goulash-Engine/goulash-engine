package com.goulash.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.core.DecisionEngine
import com.goulash.factory.BaseActorFactory
import com.goulash.script.loader.ScriptLoader
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ContainerTest {

    @BeforeEach
    fun setup() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should run the decision engine for every actor in a container on every tick`() {
        val actors: MutableList<Actor> = mutableListOf(BaseActorFactory.testActor())
        val decisionEngineMock: DecisionEngine = mockk(relaxed = true)
        val container = Container(actors = actors, decisionEngine = decisionEngineMock)

        container.tick()

        verify { decisionEngineMock.tick(actors[0]) }
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
        Container(actors = actors)

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

        container.tick()

        assertThat(actors.first().urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
