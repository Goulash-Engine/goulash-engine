package com.barbarus.prosper.script.loader

import assertk.assertThat
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Test

internal class ScriptLoaderTest {

    @Test
    fun `should load global blocking conditions from script file`() {
        ScriptLoader.load()

        assertThat(ScriptLoader.getGlobalBlockingConditionsLogicOrDefault(listOf())).isNotEmpty()
    }
}
