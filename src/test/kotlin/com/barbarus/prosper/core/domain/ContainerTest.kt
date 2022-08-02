package com.barbarus.prosper.core.domain

import ScriptLoader
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ActorFactory
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
        val config = tempDir.resolve("logic.pros")
        config.writeText(
            """ 
            logic myfoo {
                actors::urge(eat).plus(1);
            }
            """.trimIndent()
        )
        ScriptLoader.loadContainerScripts(tempDir.path)
        val container = Container(actors)

        container.act()

        assertThat(actors.first().urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
