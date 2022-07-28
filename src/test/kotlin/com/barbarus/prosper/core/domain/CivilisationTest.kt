package com.barbarus.prosper.core.domain

import ScriptLoader
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.barbarus.prosper.factories.ClanFactory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class CivilisationTest {
    private val actors: MutableList<Actor> = mutableListOf(ClanFactory.testClan())
    private val civilisation = Civilisation(actors)

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
        ScriptLoader.loadScripts(tempDir.path)

        civilisation.act()

        assertThat(actors.first().urges.getUrgeOrNull("eat")).isEqualTo(1.0)
    }
}
