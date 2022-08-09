package com.goulash.core.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.goulash.factory.ActorFactory
import com.goulash.script.loader.ScriptLoader
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ContainerTest {
    private val actors: MutableList<Actor> = mutableListOf(ActorFactory.testActor())

    @BeforeEach
    fun setup() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should invoke scripted logic`(@TempDir tempDir: java.io.File) {
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

        container.act()

        assertThat(actors.first().urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
