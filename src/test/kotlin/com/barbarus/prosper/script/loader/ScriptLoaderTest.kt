package com.barbarus.prosper.script.loader

import com.barbarus.prosper.script.ConditionLogicParser
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ScriptLoaderTest {

    @Test
    fun `should load`() {
        val mockedConditionLogicParser: ConditionLogicParser = mockk(relaxed = true)
        ScriptLoader.conditionLogicParser = mockedConditionLogicParser

        ScriptLoader.load()

        verify { mockedConditionLogicParser.parse(any()) }
    }
}
