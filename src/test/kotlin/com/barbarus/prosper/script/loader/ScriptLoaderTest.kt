package com.barbarus.prosper.script.loader

import ScriptLoader
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ScriptLoaderTest {
    @BeforeEach
    fun setup() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should load activity script file`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("activity.pros")
        config.writeText(
            """ 
            activity eating {
                trigger_urges ["eat"]
                logic act {
                    actor::urge(eat).minus(1);
                }
            }
            """.trimIndent()
        )

        ScriptLoader.loadActivityScripts(tempDir.path)

        assertThat(ScriptLoader.getActivityScripts()).isNotEmpty()
    }

    @Test
    fun `should load logic script file`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("logic.pros")
        config.writeText(
            """ 
            logic myfoo {
                actors::urge(eat).plus(1);
            }
            """.trimIndent()
        )

        ScriptLoader.loadLogicScripts(tempDir.path)

        assertThat(ScriptLoader.getLogicScripts()).isNotEmpty()
    }

    @Test
    fun `should execute even if script contains errors with multiple configs `(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("config.pros")
        config.writeText(
            """ 
            [GlobalBlocker]
            - foo
            - bar
            - baz
            ###
            [Foo]
            - what
            - up
            """.trimIndent()
        )

        ScriptLoader.loadConfigurations(tempDir.path)

        assertThat(ScriptLoader.getGlobalBlockingConditionsOrDefault()).isEmpty()
    }

    @Test
    fun `should execute even if script contains errors `(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("config.pros")
        config.writeText(
            """ 
            [WrongIdentifier]        
            - foo   
            - bar   
            """.trimIndent()
        )

        ScriptLoader.loadConfigurations(tempDir.path)

        assertThat(ScriptLoader.getGlobalBlockingConditionsOrDefault()).isEmpty()
    }

    @Test
    fun `should load global blocking conditions from script file`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("config.pros")
        config.writeText(
            """
            [GlobalBlocker]        
            - dying   
            - starving   
            """.trimIndent()
        )

        ScriptLoader.loadConfigurations(tempDir.path)

        assertThat(ScriptLoader.getGlobalBlockingConditionsOrDefault()).containsAll("dying", "starving")
    }
}
