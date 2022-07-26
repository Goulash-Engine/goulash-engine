package com.barbarus.prosper.script.loader

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ScriptLoaderTest {
    @AfterEach
    fun tearDown() {
        ScriptLoader.globalBlockingConditions = emptyList()
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

        ScriptLoader.load(tempDir.path)

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

        ScriptLoader.load(tempDir.path)

        assertThat(ScriptLoader.getGlobalBlockingConditionsOrDefault()).containsAll("dying", "starving")
    }
}
