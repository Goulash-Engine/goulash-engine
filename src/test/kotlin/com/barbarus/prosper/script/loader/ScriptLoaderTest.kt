package com.barbarus.prosper.script.loader

import assertk.assertThat
import assertk.assertions.containsAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

internal class ScriptLoaderTest {

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

        ScriptLoader.load()

        assertThat(ScriptLoader.getGlobalBlockingConditionsOrDefault()).containsAll("dying", "starving")
    }
}
