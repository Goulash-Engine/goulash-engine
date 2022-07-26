package com.goulash.script.loader

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import assertk.assertions.isNotEmpty
import com.goulash.script.domain.ActivityScript
import com.goulash.script.domain.ContainerScript
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ScriptLoaderTest {
    @BeforeEach
    fun setup() {
        ScriptLoader.resetLoader()
    }

    @Test
    fun `should clear loaded scripts when loading`() {
        val containerScript = ContainerScript("test", { _ -> run {} }, { _ -> run {} })
        ScriptLoader.containerScripts = listOf(containerScript)
        ScriptLoader.activityScripts = listOf(
            ActivityScript(
                "test",
                mapOf(),
                { _ -> run {} },
                { _ -> false },
                { _ -> run {} },
                { _ -> run {} }
            )
        )
        ScriptLoader.load()

        assertThat(ScriptLoader.getContainerScripts()).isEmpty()
        assertThat(ScriptLoader.getActivityScripts()).isEmpty()
    }

    @Test
    fun `should load container script file`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("container.gsh")
        config.writeText(
            """ 
            container routine {
                logic container {
                    actor::urge(eat).minus(1);
                }
                logic init {
                    actor::urge(eat).minus(1);
                }
            }
            """.trimIndent()
        )

        ScriptLoader.loadContainerScripts(tempDir.path)

        assertThat(ScriptLoader.getContainerScripts()).isNotEmpty()
    }

    @Test
    fun `should load activity script file`(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("activity.gsh")
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

        assertThat(ScriptLoader.containerScripts).isNotEmpty()
    }

    @Test
    fun `should execute even if script contains errors with multiple configs `(@TempDir tempDir: java.io.File) {
        val config = tempDir.resolve("config.gsh")
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
        val config = tempDir.resolve("config.gsh")
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
        val config = tempDir.resolve("config.gsh")
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
